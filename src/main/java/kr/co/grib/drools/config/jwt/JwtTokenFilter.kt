package kr.co.grib.drools.config.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.define.StatusCode
import kr.co.grib.drools.utils.getLogger
import org.apache.http.HttpHeaders
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import kotlin.math.log

@Component
class JwtTokenFilter (
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

        // 유효한 토큰인 경우 username과 roles 추출
        val username = jwtTokenProvider.getUsernameFromToken(token)
        val roles = jwtTokenProvider.getRoleFromToken(token)

        logger.info("username.$username")
        logger.info("roles.$roles")

        // 인증 성공 상태 및 정보 request attribute에 세팅
        request.setAttribute("apiStatus", "NORMAL")
        request.setAttribute("username", username)
        request.setAttribute("roles", roles)

        // 다음 필터 또는 컨트롤러 실행
        filterChain.doFilter(request, response)
    }

}