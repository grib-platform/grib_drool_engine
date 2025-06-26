package kr.co.grib.drools.api.rules.templateManager.service.Impl

import kr.co.grib.drools.api.rules.dto.RuleCreateRequestDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.templateManager.dto.RuleTemplateDto
import kr.co.grib.drools.api.rules.templateManager.service.RuleTemplateService
import kr.co.grib.drools.config.ThymeleafConfig
import kr.co.grib.drools.utils.Utiles
import kr.co.grib.drools.utils.getLogger
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class RuleTemplateServiceImpl(
    private val templateEngine: TemplateEngine,
    private val properties: ThymeleafConfig.DroolsTemplateProperties
): RuleTemplateService {
    //logger
    private val logger = getLogger()

    //<editor-fold desc="Thymeleaf text 파일 rendering">
    override fun initThymeleafRendering(
        data: RuleRequestDto,
        templateFileName: String,
    ):String {
        var result: String;
        try {
            val context = Context().apply{
                logger.info("data.$data")
                setVariables(Utiles.convertToTemplateVariable(data))
            }
            result = templateEngine.process(templateFileName, context)
        }catch (e: Exception){
            throw IllegalStateException("thymeleaf.rule.context.error.$e")
        }

        return result
    }
    //</editor-fold desc="Thymeleaf text 파일 rendering">

    //<editor-fold desc="Thymeleaf text 파일 rendering">
    override fun initThymeleafRenderAllRules(
        data: RuleTemplateDto,
        header: Boolean
    ): String {
        var result: String;
        try {
            val context = Context().apply{
                logger.info("data.$data")
                setVariables(Utiles.convertToTemplateEveryVariable(data, header))
            }
            result = templateEngine.process(properties.fileName, context)
        }catch (e: Exception){
            throw IllegalStateException("Thymeleaf.rule.context.error.$e")
        }
        return result
    }
    //</editor-fold desc="Thymeleaf text 파일 rendering">


}