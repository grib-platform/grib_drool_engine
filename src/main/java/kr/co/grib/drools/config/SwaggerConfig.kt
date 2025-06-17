package kr.co.grib.drools.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                 Info()
                     .title("Drools Rule API")
                     .version("v1.0.0")
                     .description("Drools 기반 룰 관리 시스템 API 명세서")
            )
    }
}