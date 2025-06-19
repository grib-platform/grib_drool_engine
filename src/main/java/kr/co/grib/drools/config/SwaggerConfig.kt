package kr.co.grib.drools.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    companion object {
        private const val SECURITY_SCHEME_NAME = "BearerAuth"
    }

    @Bean
    fun customOpenAPI(): OpenAPI = OpenAPI()
        .components(
            Components()
                .addSecuritySchemes(
                SECURITY_SCHEME_NAME,
                SecurityScheme()
                    .name("Authorization")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("Bearer")
                    .bearerFormat("JWT")
            )
        )
        .addSecurityItem(
            SecurityRequirement().addList(SECURITY_SCHEME_NAME)
        )
        .info(apiInfo())

    private fun apiInfo(): Info =
        Info()
            .title("Drools Rule API")
            .description("Drools 기반 룰 관리 시스템 API 명세서")
            .version("v1.0.0")
}