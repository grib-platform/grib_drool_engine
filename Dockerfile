# 1. Base Image
FROM eclipse-temurin:17-jre-alpine

# 2. WORK DIR 설정
WORKDIR /app

# 3. 빌드된 jar 파일 복사
COPY build/libs/*.jar app.jar

# 4. 실행 환경 고정값 (실행 위치/wrapper script 변경에 영향받지 않도록 명시)
ENV SERVER_PORT=8080
ENV SPRING_CONFIG_ADDITIONAL_LOCATION=file:/app/config/

# 5. 컨테이너 포트 노출
EXPOSE $SERVER_PORT

# 6. 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]

# 7. Git Commit 정보 전달 (빌드 시 --build-arg GIT_COMMIT=$(git rev-parse HEAD) 옵션으로 전달)
ARG GIT_COMMIT=unknown
LABEL org.opencontainers.image.revision="${GIT_COMMIT}"
