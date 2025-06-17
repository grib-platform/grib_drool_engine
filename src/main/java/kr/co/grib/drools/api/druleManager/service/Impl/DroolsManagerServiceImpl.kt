package kr.co.grib.drools.api.druleManager.service.Impl

import kr.co.grib.drools.api.druleManager.service.DroolsManagerService
import kr.co.grib.drools.utils.getLogger
import org.kie.api.KieServices
import org.kie.api.builder.Message
import org.kie.api.io.ResourceType
import org.kie.api.runtime.KieContainer
import org.kie.api.runtime.KieSession
import org.kie.internal.io.ResourceFactory
import org.kie.internal.utils.KieHelper
import org.springframework.stereotype.Service

@Service
class DroolsManagerServiceImpl(
    private val kieServices: KieServices
): DroolsManagerService
{
     private val logger = getLogger()
    //<editor-fold desc="variable to Map">
    override fun initTemplateToDrools(
        str: String
    ): KieSession? {
       if (str.isBlank()){
           logger.error("str.is.empty.or.null")
           return  null
       }
        return try {
            createKeySession(str)
        }catch (e: Exception){
            logger.error("Fail.to.Create.KieSession.$e")
            throw IllegalStateException("KieSession.creation.failed.$e")
        }

    }
    //</editor-fold desc="variable to Map">

    //<editor-fold desc="template text 에서 특정 Rule 가져와서 특정  rule 실행 하기">
    override fun getTemplateRules(
        ruleName: String,
        ruleString: String
    ): KieSession? {

        if (ruleName.isBlank()) {
            logger.error("ruleName.is.empty.or.null")
            return null
        }

        val pattern = Regex("""rule\s+"$ruleName"\s+(.|\n)*?end""")
        if (ruleString.isBlank()){
            logger.error("ruleString.is.empty.or.null")
            return null
        }
        return try {
            // 조건으로 찾았을때 null 일 수도 있으니까.
            pattern.find(ruleString)?.value?.let { createKeySession(it) }

        }catch (e: Exception) {
            logger.error("Fail.to.getTemplateRules.$e")
            throw IllegalStateException("KieSession.select.failed.$e")
        }

    }
    //</editor-fold desc="template text 에서 특정 Rule 가져와서 특정  rule 실행 하기">


    //<editor-fold desc="KieSession 생성">
    private fun createKeySession(
        str: String
    ): KieSession {
        val kieHelper = KieHelper()
        kieHelper.addContent(str, ResourceType.DRL)
        val kieBase = kieHelper.build()
        return kieBase.newKieSession()
    }
    //</editor-fold desc="KieSession 생성">
}