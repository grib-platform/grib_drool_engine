package kr.co.grib.drools.config

import kr.co.grib.drools.config.jwt.JwtTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Spring Security 설정
 * - JWT 토큰 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
 * - 인증 필요 경로, CSRF 비활성화 등 설정
 */
@Configuration
class SecurityConfig (
    private val jwtTokenFilter: JwtTokenFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }  // REST API이므로 CSRF 비활성화
            .cors(Customizer.withDefaults())
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }  // 세션 상태 비저장
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/auth/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/api-docs/**",
                        "/api/v1/rule/**",
                        "/api/v1/hRule/**",
                        "/api/v1/cRule/**"
                    ).permitAll() // 인증없이 접근 허용
                    .anyRequest().authenticated() // 그 외 요청은 인증 필요
            }
            // TODO. 1.  test 로 일시 주석
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java) // JWT 필터 등록

        return http.build()
    }

    //<editor-fold desc="화면 cors 문제">
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = listOf(
                "https://192.168.50.25:3000", // React 기본 포트
                "https://localhost:3000",
                "https://192.168.30.52:3000",
                "https://huring.grib-iot.com:9515",
                "https://huring.grib-iot.com:9516",
                "https://huring.grib-iot.com:9502",
                "https://192.168.0.240:9000",

            )
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS")
            allowedHeaders = listOf("*")
            // 프론트에서 읽어야 하는 응답 헤더(파일명/리다이렉트/토큰 등)
            exposedHeaders = listOf("Content-Disposition", "Authorization", "Location")
            // 프리플라이트 캐시(초)
            maxAge = 3600
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }
//</editor-fold desc="화면 cors 문제">

}