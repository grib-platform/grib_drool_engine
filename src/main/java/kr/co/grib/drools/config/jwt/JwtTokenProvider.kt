package kr.co.grib.drools.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kr.co.grib.drools.utils.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * 모든 HTTP 요청마다 실행되는 JWT 검증 필터
 * 요청 헤더에서 Bearer 토큰을 꺼내서 유효성 검증 후
 * 유저 정보를 request attribute에 넣어둠
 */
//<editor-fold desc="Jwt token filter">
@Component
class JwtTokenProvider (
    @Value("\${spring.jwt.secret}")
    private val secretKeystr: String
){

    private val logger = getLogger()

    /**
     * JWT 토큰에서 Claims (정보집합)를 파싱해서 반환
     * @param token JWT 토큰 문자열
     * @return Claims 토큰의 클레임(정보) 객체
     * @throws io.jsonwebtoken.JwtException 서명 오류, 만료 등 예외 발생 가능
     */
    fun getClaimsFromToken(token: String): Claims =
        Jwts.parser()
            .setSigningKey(secretKeystr)
            .parseClaimsJws(token)      // 토큰 파싱 및 서명 검증 수행
            .body                      // 검증 완료 시 Claims 반환

    /**
     * 토큰에서 username(subject) 추출
     */
    fun getUsernameFromToken(token: String): String =
        getClaimsFromToken(token).subject

    /**
     * 토큰에서 사용자 역할(role) 정보 추출 (claim 이름 "role" 로 가정)
     */
    fun getRoleFromToken(token: String): String? =
        getClaimsFromToken(token).get("role", String::class.java)

    /**
     * 토큰 만료 일자 추출
     */
    fun getExpirationDateFromToken(token: String): Date =
        getClaimsFromToken(token).expiration

    /**
     * 토큰 유효성 검사
     * - 서명 검증 성공 여부
     * - 만료 여부 체크 (현재 시간과 비교)
     */
    fun validateToken(token: String): Boolean {
        return try {
            val claims = getClaimsFromToken(token)
            logger.info("claims.$claims")
            // 토큰 만료일자가 현재 시간 이후인지 체크
            !claims.expiration.before(Date())
        } catch (ex: Exception) {
            logger.info("check.ex.${ex.javaClass.name}: ${ex.message}")
            // 서명 오류, 만료, 형식 오류 등 예외 시 false 반환
            false
        }
    }

}


//</editor-fold desc="Jwt token filter">