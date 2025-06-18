package kr.co.grib.drools.api.rules.service.Impl

import com.fasterxml.jackson.databind.ser.Serializers.Base
import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.druleManager.dto.ActionResultDto
import kr.co.grib.drools.api.druleManager.dto.RuleFactDto
import kr.co.grib.drools.api.druleManager.service.DroolsManagerService
import kr.co.grib.drools.api.rules.dto.RuleCreateRequestDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.dto.RuleResponseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleResponseDto
import kr.co.grib.drools.api.rules.service.RuleService
import kr.co.grib.drools.api.templateManager.service.RuleTemplateService
import kr.co.grib.drools.config.RequestInfoConfig
import kr.co.grib.drools.define.StatusCode
import kr.co.grib.drools.utils.Utiles
import kr.co.grib.drools.utils.getLogger
import org.springframework.stereotype.Service

@Service
class RuleServiceImpl (
    private val requestInfoConfig: RequestInfoConfig,
    private val droolsManagerService: DroolsManagerService,
    private val ruleTemplateService: RuleTemplateService,
) : RuleService{

    private val logger = getLogger()


//    override fun doEvaluateRules(
//        req: RuleRequestDto
//    ): RuleResponseCtlDto {
//        val rtn = RuleResponseCtlDto();
//        val result = ActionResultDto()
//        try {
//
//            if (req.groupId.isEmpty()){
//                rtn.success = false
//                rtn.code = StatusCode.GROUP_ID_IS_EMPTY
//                return  rtn
//            }
//
//            if (req.deviceId.isEmpty()){
//                rtn.success = false
//                rtn.code = StatusCode.DEVICE_ID_IS_EMPTY
//                return rtn
//            }
//
//            val kieSession = droolsManagerService.initKieSession(req.groupId)
//            if (kieSession == null){
//                rtn.success = false
//                rtn.code = StatusCode.INIT_ERROR_KIESESSION
//                return rtn
//            }
//
//            val fact = RuleFactDto(
//                groupId = req.groupId,
//                deviceId = req.deviceId,
//                functionName = req.functionName,
//                functionValue = req.functionValue
//            )
//
//            kieSession.insert(fact)
//            kieSession.insert(result)
//            kieSession.fireAllRules()
//            kieSession.dispose()
//
//            rtn.success = true
//            rtn.code = StatusCode.SUCCESS
//            rtn.body = result.action?.let {
//                RuleResponseDto(
//                    groupId = req.groupId,
//                    action = it,
//                    result =  result.result
//                )
//            }
//            return rtn
//        }catch (e: Exception){
//            logger.error("doEvaluateRules.Error.$e")
//        }
//        return rtn
//    }

    // thymeleaf  예제
    override fun doEvaluateRulesT(
        req: RuleRequestDto
    ): RuleResponseCtlDto {
        val rtn = RuleResponseCtlDto()
        val result = ActionResultDto()
        try {

            // data 에 대한
            if (req.groupId.isEmpty() || req.groupId.isBlank()){
                logger.error("groupId.is.null")
                rtn.success = false
                rtn.code = StatusCode.GROUP_ID_IS_EMPTY
                return rtn
            }

            if (req.facts.isEmpty()) {
                logger.error("fact.is.null")
                rtn.success = false
                rtn.code = StatusCode.FACTS_IS_EMPTY
                return rtn
            }

            val chk = ruleTemplateService.initThymeleafRendering(req,"rule-template-test")
            logger.info("chk.$chk")
            val kieSession = droolsManagerService.initTemplateToDrools(chk)

            logger.info("kieSession.$kieSession")
            if (kieSession == null){
                rtn.success = false
                rtn.code = StatusCode.INIT_ERROR_KIESESSION
                return rtn
            }

            /**
             * 기준 값 발화 시점
             * 실시간 측정 값은 어디에 둬야 하는 건가?
             * */
            kieSession.insert(result)
            req.facts.map { it ->
                val fact = RuleFactDto(
                    groupId = req.groupId,
                    deviceId = it.deviceId,
                    functionName = it.functionName,
                    functionValue = it.functionValue
                )
                kieSession.insert(fact)
            }

            kieSession.fireAllRules()
            val fireRulesCount = kieSession.fireAllRules();
            logger.info("kieSession.$fireRulesCount ")
            kieSession.dispose()

            logger.info("check.$result")



        }catch (e: Exception){
            logger.error("error.$e")
        }
        return rtn
    }

    //<editor-fold desc="[POST] /create Drool 생성">
    override fun doPostCreateRule(
        req: RuleCreateRequestDto
    ): BaseCtlDto {
        val rtn  = BaseCtlDto()
        try {
            logger.info("req.$req")
            if (req.createdBy.isEmpty()){
                rtn.success = false
                rtn.code = StatusCode.CREATED_BY_IS_EMPTY
                return rtn
            }

            if (req.ruleGroup.isEmpty()){
                rtn.success = false
                rtn.code = StatusCode.RULE_GROUP_IS_EMPTY
                return rtn
            }

            val chk = ruleTemplateService.initThymeleafRenderAllRules(req)
            logger.info("chk.$chk")





        }catch (e: Exception){
            logger.error("doPostCreateRule.error.$e")
        }
        return rtn
    }
    //<editor-fold desc="[POST] /create Drool 생성">

}