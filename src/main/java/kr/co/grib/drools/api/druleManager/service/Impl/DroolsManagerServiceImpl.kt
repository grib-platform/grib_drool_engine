package kr.co.grib.drools.api.druleManager.service.Impl

import kr.co.grib.drools.api.druleManager.dto.ActionResultDto
import kr.co.grib.drools.api.druleManager.dto.RuleFactDto
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


     //<editor-fold desc="룰 최초 등록 하거나 새롭게 다시 빌드 하여 교체 할때 사용">
     override fun initRule(groupId: String, ruleFilePath: String) {
          //drl 파일에서 읽기
          val kieFileSystem = kieServices.newKieFileSystem()
          try {
              if (ruleFilePath.isEmpty()){
                   logger.error("ruleFilePath.isEmpty.$ruleFilePath")
                   return
              }

              // TODO. 수정이 필요해
              logger.info("file.path.$ruleFilePath")
              val resource = ResourceFactory.newClassPathResource(ruleFilePath, "UTF-8")
              kieFileSystem.write(resource)

              val kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll()
              logger.info("kieBuilder.$kieBuilder")
               if (kieBuilder.results.hasMessages(Message.Level.ERROR)){
                    logger.error("Rule.build.error.${kieBuilder.results}")
                    throw IllegalStateException("Rule.Build.Error.${kieBuilder.results}")
               }
              val releaseId = kieBuilder.kieModule.releaseId
              val kieContainer = kieServices.newKieContainer(releaseId)
              kieContainers[groupId] = kieContainer

          }catch (e: Exception){
               logger.error("init.Rule.error.$e")
          }
     }
     //</editor-fold desc="룰 최초 등록 하거나 새롭게 다시 빌드 하여 교체 할때 사용">

     //<editor-fold desc="룰 삭제">
     override fun deleteRule(groupId: String) {
          try {
              if (groupId.isEmpty()){
                   logger.error("groupId.isEmpty")
                   return
              }
               kieContainers.remove(groupId)
          }catch (e: Exception){
               logger.error("delete.Rule.error.$e")
          }
     }
     //</editor-fold desc="룰 삭제">

     //<editor-fold desc="룰 그룹별 분리된 세션 처리, 동적으로 rule 관리 하고자 할때">
     override fun initKieSession(groupId: String): KieSession? {
          try {
              if (groupId.isEmpty() || groupId.isBlank()){
                   logger.error("groupId.isEmpty")
                   return null
              }
              val kieContainer = kieContainers[groupId]

              if(kieContainer == null ){
                  logger.error("kieConatiner.null.$kieContainer")
                  return null
              }
              return kieContainer.newKieSession()
          }catch (e: Exception){
               logger.error("initKieSession.error.$e")
               throw IllegalStateException("initKieSession.error.groupId.$groupId")
          }
     }
    //</editor-fold desc="룰 그룹별 분리된 세션 처리, 동적으로 rule 관리 하고자 할때">

    //<editor-fold desc="kieHelper를 이용해 동적 .drl 문자열을 처리 할때">
    override fun getRoleFromDrl(
        drl: String,
        fact: RuleFactDto
    ): ActionResultDto {
        val rtn = ActionResultDto()
        val kieHelper = KieHelper()
        try {
            kieHelper.addContent(drl, ResourceType.DRL)
            val kieSession = kieHelper.build().newKieSession()
            if (kieSession == null){
                logger.debug("kieSession.is.null")
                return rtn
            }

            kieSession.insert(fact)
            kieSession.insert(rtn)
            kieSession.fireAllRules()
            kieSession.dispose()

            return  rtn
        }catch (e: Exception){
            logger.error("Error.kieSession.{}", e)
        }

        return rtn
    }
    //</editor-fold desc="kieHelper를 이용해 동적 .drl 문자열을 처리 할때">





}