#!/usr/bin/env bash
#
# local_test_compose.sh
#
# 로컬에서 "잘 돌아가는지"만 확인해보기 위한 스크립트입니다. 실제 개발서버
# 배포는 이 파일이 아니라 iot-infra-dev/bin/deploy_stack.sh 가 담당합니다.
#
# 설정 주입 방식:
#   docker-compose.yml의 configs/secrets가 ./config/grib-model/application.yml,
#   ./config/grib-model/secrets.yml을 컨테이너의 /app/config/application.yml,
#   /app/config/secrets.yml로 마운트합니다. 이 스크립트는 그 파일들을 건드리지
#   않고, docker compose up만으로 서비스가 뜹니다(그 외 부대 작업 없음).
#
# 연결 테스트:
#   http/conn_test.http를 고치지 않고 그대로, IntelliJ HTTP Client CLI
#   (jetbrains/intellij-http-client) docker 이미지로 실행합니다.
#     - http/http-client.env.json의 "docker" environment
#       (SERVICE_BASE_URL=http://localhost:19996)를 씁니다.
#     - http/http-client.private.env.json(gitignore 대상, 로컬에 직접 준비)의
#       "docker" environment에서 ACTIVE_JWT를 읽습니다. 없으면
#       http-client.private.env.json.example을 복사해서 채워주세요.
#     - -D 옵션으로 컨테이너 안에서도 호스트의 localhost(19996)를 그대로
#       찾아갑니다.
#     - --report로 JUnit XML(report.xml)을 남기고, 그 안의 failures/errors
#       개수로 성공/실패를 판정합니다(conn_test.http의 client.test 응답
#       핸들러가 만드는 값입니다).
#   OAUTH_BASE_URL 연결 여부는 HTTP status만으로는 확실히 알 수 없어서(잘못된
#   client_id로 인한 실패와 구분이 안 됨), conn_test.http의 주석대로 컨테이너
#   로그에서 연결 에러(ConnectTimeoutException 등)도 별도로 확인합니다.
#
# 사용법:
#   ./local_test_compose.sh            # 기본 동작: 빌드 + 기동 + 연결 테스트
#   ./local_test_compose.sh test       # 위와 동일
#   ./local_test_compose.sh up -d      # 그 외 인자는 docker compose 서브커맨드로 그대로 전달
#   ./local_test_compose.sh down

set -uo pipefail

### 사용자 수정 가능 설정 [시작]
SERVICE_NAME="rule-engine"
IMAGE_NAME="rule-engine:latest"
HOST_PORT=19627 # docker-compose.yml의 ports 매핑(호스트 쪽)과 일치해야 함
CHECK_OAUTH_LOG=false # spring.oauth.baseUrl 연결 에러를 컨테이너 로그로 확인할지 여부(해당 없는 서비스는 false로)
### 사용자 수정 가능 설정 [끝]

HTTP_CLIENT_IMAGE="jetbrains/intellij-http-client"
HTTP_ENV="docker" # http-client.env.json / http-client.private.env.json의 environment 이름

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
HTTP_DIR="$REPO_DIR/http"
PRIVATE_ENV_FILE="$HTTP_DIR/http-client.private.env.json"

run_compose() {
  (cd "$SCRIPT_DIR" && docker compose "$@")
}

cmd="${1:-test}"

if [ "$cmd" != "test" ]; then
  run_compose "$@"
  exit $?
fi

if [ ! -f "$PRIVATE_ENV_FILE" ]; then
  echo "ERROR $PRIVATE_ENV_FILE 이 없습니다. http-client.private.env.json.example을 복사해서 \"$HTTP_ENV\" environment에 실제 ACTIVE_JWT를 채워주세요"
  exit 1
fi

echo "1) gradle build + 이미지 빌드 ($REPO_DIR 기준, docker-test 바깥)"
(cd "$REPO_DIR" && ./gradlew build -x test) || { echo "ERROR gradle build 실패"; exit 1; }
docker build -t "$IMAGE_NAME" "$REPO_DIR" || { echo "ERROR 이미지 빌드 실패"; exit 1; }

echo "2) 컨테이너 기동 (docker-compose.yml 기준, 별도 설정 조립 없음)"
run_compose up -d || { echo "ERROR 컨테이너 기동 실패"; exit 1; }
trap 'run_compose down >/dev/null 2>&1' EXIT

BASE_URL="http://localhost:${HOST_PORT}"

echo "3) 앱 기동 대기"
ready=false
for _ in $(seq 1 30); do
  if curl -sS -o /dev/null "$BASE_URL" 2>/dev/null; then
    ready=true
    break
  fi
  sleep 1
done
if [ "$ready" != "true" ]; then
  echo "ERROR ${HOST_PORT}포트가 30초 안에 응답하지 않습니다"
  run_compose logs "$SERVICE_NAME"
  exit 1
fi

echo "4) http/conn_test.http 실행 (IntelliJ HTTP Client CLI, environment=$HTTP_ENV)"
rm -rf "$HTTP_DIR/reports"
docker run --rm \
  -v "$HTTP_DIR:/workdir" \
  -w /workdir \
  --add-host=host.docker.internal:host-gateway \
  "$HTTP_CLIENT_IMAGE" \
  conn_test.http \
  -D \
  --env-file http-client.env.json \
  --private-env-file http-client.private.env.json \
  --env "$HTTP_ENV" \
  --report=reports
http_client_status=$?

fail=0
REPORT_FILE="$HTTP_DIR/reports/report.xml"

if [ ! -f "$REPORT_FILE" ]; then
  echo "ERROR report.xml이 생성되지 않았습니다 (ijhttp 실행 자체가 실패했을 수 있습니다, exit=$http_client_status)"
  fail=1
else
  failures="$(grep -o 'failures="[0-9]*"' "$REPORT_FILE" | head -1 | grep -o '[0-9]*')"
  errors="$(grep -o 'errors="[0-9]*"' "$REPORT_FILE" | head -1 | grep -o '[0-9]*')"
  echo "--- report.xml 결과: failures=${failures:-?}, errors=${errors:-?} ---"
  if [ "${failures:-1}" != "0" ] || [ "${errors:-1}" != "0" ]; then
    fail=1
  fi
fi

if [ "$CHECK_OAUTH_LOG" = "true" ]; then
  echo "5) 컨테이너 로그에서 spring.oauth.baseUrl 연결 에러 확인"
  logs="$(run_compose logs "$SERVICE_NAME" 2>&1)"
  if echo "$logs" | grep -qE "ConnectTimeoutException|Connection refused"; then
    echo "FAIL 로그에 연결 에러가 있습니다 (spring.oauth.baseUrl 문제로 보임)"
    fail=1
  else
    echo "OK 로그에 연결 에러 없음"
  fi
fi

echo ""
if [ "$fail" -eq 0 ]; then
  echo "=== 전체 통과 ==="
else
  echo "=== 일부 실패 ==="
fi
exit "$fail"
