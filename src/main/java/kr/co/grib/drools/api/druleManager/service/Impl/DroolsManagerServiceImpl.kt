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
    private val kieContainers = mutableMapOf<String,KieContainer>()
     private val logger = getLogger()

    //<editor-fold desc="Drool Create">
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
    //</editor-fold desc="Drool Create">













}