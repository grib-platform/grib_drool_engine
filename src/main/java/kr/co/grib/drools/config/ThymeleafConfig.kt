package kr.co.grib.drools.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.templateresolver.StringTemplateResolver

@Configuration
@EnableConfigurationProperties(ThymeleafConfig.DroolsTemplateProperties::class)
class ThymeleafConfig(
    private val properties: DroolsTemplateProperties
) {
    // 1. 클래스 path 기반 템플릿 로더 ( /templates/rule.drl)
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

    //2. DB 기반 문자열  템플릿 로더( DB에서 템블릿 문자열로 로드 할때 사용)
    @Bean
    fun stringTemplateResolver(): StringTemplateResolver{
        return StringTemplateResolver().apply {
            templateMode = TemplateMode.TEXT
            order = properties.order +1 // 파일 리졸버 보다 우선 순위를 낮게 설정
            isCacheable = false //  동적 템플릿 처리시 꺼야 함
        }
    }

    //3. TemplateEngine 등록 (파일 + 문자열 둘 다 처리)
    @Bean
    fun droolsTemplateEngine(
        droolsTemplateResolver: ClassLoaderTemplateResolver,
        stringTemplateResolver: StringTemplateResolver
    ): TemplateEngine {
        return TemplateEngine().apply {
            val resolvers: Set<ITemplateResolver> = linkedSetOf(droolsTemplateResolver, stringTemplateResolver)
            setTemplateResolvers(resolvers)
        }
    }

    //프로퍼티 클래스  ,  nested class : 중첩 클래스
    @ConfigurationProperties(prefix = "drools.template")
    class DroolsTemplateProperties{
        lateinit var prefix: String
        lateinit var suffix: String
        lateinit var encoding: String
        var order: Int = 1
        lateinit var fileName: String
    }



}