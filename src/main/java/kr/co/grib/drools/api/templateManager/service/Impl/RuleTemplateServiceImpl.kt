package kr.co.grib.drools.api.templateManager.service.Impl

import kr.co.grib.drools.api.templateManager.service.RuleTemplateService
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
        data: Any
    ):String {
        var result = "";
        try {
            val context = Context().apply{
                setVariables(Utiles.getVariableToMap(data))
            }
            result = templateEngine.process(properties.fileName, context)
        }catch (e: Exception){
            logger.error("thymeleaf.rule.context.error.$e")
        }

        return result
    }
    //</editor-fold desc="Thymeleaf text 파일 rendering">


}