package kr.co.grib.drools.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Configuration
@EnableConfigurationProperties(ThymeleafConfig.DroolsTemplateProperties::class)
class ThymeleafConfig(
    private val properties: DroolsTemplateProperties
) {
    @Bean
    fun droolsTemplateResolver(): ClassLoaderTemplateResolver {
        return ClassLoaderTemplateResolver().apply {
            prefix = properties.prefix
            suffix = properties.suffix
            templateMode = TemplateMode.TEXT
            characterEncoding = properties.encoding
            order = properties.order
            checkExistence = true
        }
    }
    @Bean
    fun droolsTemplateEngine(
        droolsTemplateResolver: ClassLoaderTemplateResolver
    ): TemplateEngine {
        return TemplateEngine().apply {
            setTemplateResolver(droolsTemplateResolver)
        }
    }
    //nested class : 중첩 클래스
    @ConfigurationProperties(prefix = "drools.template")
    class DroolsTemplateProperties{
        lateinit var prefix: String
        lateinit var suffix: String
        lateinit var encoding: String
        var order: Int = 1
    }
}