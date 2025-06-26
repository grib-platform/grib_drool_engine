package kr.co.grib.drools.config

import kr.co.grib.drools.config.jwt.JwtTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
                        "/api/v1/hRule/**").permitAll() // 인증없이 접근 허용
                    .anyRequest().authenticated() // 그 외 요청은 인증 필요
            }
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java) // JWT 필터 등록

        return http.build()
    }

}