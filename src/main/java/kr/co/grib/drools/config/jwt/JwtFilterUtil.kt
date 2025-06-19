package kr.co.grib.drools.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.utils.getLogger
import org.apache.http.HttpHeaders
import org.springframework.http.MediaType

object JwtFilterUtil {

    private val logger = getLogger()

    private val objectMapper = ObjectMapper()
    private val swaggerPaths = setOf(
        "/v3/api-docs",
        "/swagger-ui",
        "/api-docs",
        "/swagger-ui.html"
    )

    //<editor-fold desc="요청 경로가 Swagger인지 체크">
    fun isSwaggerPath(
        request: HttpServletRequest
    ): Boolean {
        val path = request.requestURI
        return swaggerPaths.any { path.startsWith(it) || path == "/swagger-ui.html" }
    }
    //</editor-fold desc="요청 경로가 Swagger인지 체크">

    //<editor-fold desc="Authorization 해더가 존재 하며 "Bearer  "로 시작 하는지 여부 확인">
    fun hasValidAuthorizationHeader(
        request: HttpServletRequest
    ): Boolean {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return false
        logger.info(" header.check.$header")
        return header.startsWith("Bearer ")
    }
    //</editor-fold desc="Authorization 해더가 존재 하며 "Bearer  "로 시작 하는지 여부 확인">


    //<editor-fold desc="Authorization 해더에서 token 문자열만 추출 "Bearer " 제거 반환">
    fun doExtractToken(
        request: HttpServletRequest
    ): String? {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        if(!header.startsWith("Bearer ")) return null

        val tokenPart = header.substring(7).trim()
        val token = tokenPart
            .replace(Regex("^\"?access_token\"?:\"?"), "")
            .replace("\".*$", "")  // 쌍따옴표 뒤부터 제거

        return token
    }
    //</editor-fold desc="Authorization 해더에서 token 문자열만 추출 "Bearer " 제거 반환">


    //<editor-fold desc="토큰 유효성 검사">
    fun isTokenValid(
        jwtTokenProvider: JwtTokenProvider,
        token: String
    ): Boolean {
        return !token.isNullOrBlank() && jwtTokenProvider.validateToken(token)
    }
    //</editor-fold desc="토큰 유효성 검사">

    //<editor-fold desc="인증 실패 응답">
    fun doErrorResponse(
        response: HttpServletResponse,
        errorResponse: BaseCtlDto
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.setHeader("Cache-Control", "no-store")
        response.setHeader("Pragma", "no-cache")
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
        response.writer.flush()
    }
    //</editor-fold desc="인증 실패 응답">

}