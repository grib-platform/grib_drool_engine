package kr.co.grib.drools.config.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.define.StatusCode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtTokenFilter (
    @Value("\${spring.profiles.active:}")
    private val activeProfile: String,
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtTokenFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        //swegger 관련 요청은 필터에서 제외
        if(JwtFilterUtil.isSwaggerPath(request)){
            filterChain.doFilter(request,response)
            return
        }

        //TODO. 삭제 요망
        //<editor-fold desc="JWT 로그인 연동 제외 :  test 용 ">
//        if(activeProfile == "local"){
//            //TODO. test를 위해 임의 지정
//            val username = "GIRB_CURRENT_000002"
//            request.setAttribute("username", username)
//            filterChain.doFilter(request,response)
//            return
//        }
        //</editor-fold desc="JWT 로그인 연동 제외 :  test 용 ">


        // Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 에러 처리
        if (!JwtFilterUtil.hasValidAuthorizationHeader(request)){
            JwtFilterUtil.doErrorResponse(
                response,
                BaseCtlDto(
                    success =  false,
                    code = StatusCode.JWT_TOKEN_HEADER_ERROR.name,
                    message = StatusCode.JWT_TOKEN_HEADER_ERROR)
            )
            logger.warn(StatusCode.JWT_TOKEN_HEADER_ERROR)
            return
        }

        // 토큰 유효성 검증 실패 시 에러 처리
        val token = JwtFilterUtil.doExtractToken(request)
        logger.info("token.$token")
        if (token == null){
            JwtFilterUtil.doErrorResponse(
                response,
                BaseCtlDto(
                success =  false,
                code = StatusCode.JWT_TOKEN_IS_EMPTY.name,
                message = StatusCode.JWT_TOKEN_IS_EMPTY)
            )
            logger.warn(StatusCode.JWT_TOKEN_IS_EMPTY)
            return
        }

        if (!JwtFilterUtil.isTokenValid(jwtTokenProvider, token)) {
            logger.info("token.isTokenValid.before.$token")
            JwtFilterUtil.doErrorResponse(
                response,
                BaseCtlDto(
                success = false,
                code = StatusCode.JWT_TOKEN_INVALID_EXPIRED.name,
                message = StatusCode.JWT_TOKEN_INVALID_EXPIRED)
            )
            logger.warn(StatusCode.JWT_TOKEN_INVALID_EXPIRED)
            return
        }


        //TODO. 유효한 토큰인 경우 username과 roles 추출
        val username = jwtTokenProvider.getUsernameFromToken(token)

        //username
        logger.info("username.$username")

        if (username.isNullOrBlank()){
            logger.info("token.has.username.$username")
            JwtFilterUtil.doErrorResponse(
                response,
                BaseCtlDto(
                    success = false,
                    code = StatusCode.JWT_HAS_NO_USER_NAME.name,
                    message = StatusCode.JWT_HAS_NO_USER_NAME)
            )
            logger.warn(StatusCode.JWT_HAS_NO_USER_NAME)
            return
        }

        // 인증 성공 상태 및 정보 request attribute에 세팅
        request.setAttribute("apiStatus", "NORMAL")
        request.setAttribute("username", username)
        // 다음 필터 또는 컨트롤러 실행
        filterChain.doFilter(request, response)
    }
}