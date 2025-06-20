package kr.co.grib.drools.api.rules.service.Impl

import com.fasterxml.jackson.databind.ser.Serializers.Base
import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.druleManager.dto.ActionResultDto
import kr.co.grib.drools.api.druleManager.dto.RuleFactDto
import kr.co.grib.drools.api.druleManager.service.DroolsManagerService
import kr.co.grib.drools.api.rules.dto.*
import kr.co.grib.drools.api.rules.repository.DroolsAuditHistoryRepo
import kr.co.grib.drools.api.rules.repository.DroolsModifyHistoryRepo
import kr.co.grib.drools.api.rules.repository.DroolsRepo
import kr.co.grib.drools.api.rules.service.RuleService
import kr.co.grib.drools.api.templateManager.dto.RuleTemplateDto
import kr.co.grib.drools.api.templateManager.service.RuleTemplateService
import kr.co.grib.drools.config.RequestInfoProvider
import kr.co.grib.drools.define.RuleOperationType
import kr.co.grib.drools.define.StatusCode
import kr.co.grib.drools.utils.getLogger
import org.springframework.stereotype.Service

@Service
class RuleServiceImpl (
    private val requestInfoProvider: RequestInfoProvider,
    private val droolsManagerService: DroolsManagerService,
    private val ruleTemplateService: RuleTemplateService,

    private val droolsRepo: DroolsRepo,
    private val droolsModifyHistoryRepo: DroolsModifyHistoryRepo,
    private val droolsAuditHistoryRepo: DroolsAuditHistoryRepo,
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
                rtn.code = StatusCode.GROUP_ID_IS_EMPTY.name
                return rtn
            }

            if (req.facts.isEmpty()) {
                logger.error("fact.is.null")
                rtn.success = false
                rtn.code = StatusCode.FACTS_IS_EMPTY.name
                return rtn
            }

            val chk = ruleTemplateService.initThymeleafRendering(req,"rule-template-test")
            logger.info("chk.$chk")
            val kieSession = droolsManagerService.initTemplateToDrools(chk)

            logger.info("kieSession.$kieSession")
            if (kieSession == null){
                rtn.success = false
                rtn.code = StatusCode.INIT_ERROR_KIESESSION.name
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
            logger.info(requestInfoProvider.getUserName())

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
        val username = requestInfoProvider.getUserName()
        var ruleDrl  = ""
        try {
            logger.info("req.$req")
            if (req.ruleGroup.isEmpty()){
                rtn.success = false
                rtn.code = StatusCode.RULE_GROUP_IS_EMPTY.name
                rtn.message = StatusCode.RULE_GROUP_IS_EMPTY
                return rtn
            }

            if (req.ruleSetName.isEmpty()){
                rtn.success = false
                rtn.code = StatusCode.RULE_SET_NAME_IS_EMPTY.name
                rtn.message = StatusCode.RULE_SET_NAME_IS_EMPTY
                return rtn
            }

            //Rule 파일 만들기
            ruleDrl = ruleTemplateService.initThymeleafRenderAllRules(
                RuleTemplateDto(
                    ruleGroup = req.ruleGroup,
                    singleRules = req.singleRules,
                    andRules = req.andRules,
                    orRules = req.orRules
                )
            )

            logger.info("rule.Drl.create.info.$ruleDrl")
            val ruleChk = droolsRepo.countDrools(req.ruleGroup, req.ruleSetName)
            logger.info("rule.in.DB.check.$ruleChk")
            if (ruleChk >=1){
                rtn.success = false
                rtn.code = StatusCode.RULE_ALREADY_EXISTS.name
                rtn.message = StatusCode.RULE_ALREADY_EXISTS
                return rtn
            }

            val rule = droolsRepo.saveRule(
                RuleInsertReqDto(
                    ruleGroup = req.ruleGroup,
                    ruleText = ruleDrl,
                    createdBy = username,
                    ruleSetName = req.ruleSetName,
                    ruleActionType = RuleOperationType.CREATE,
                    enabled = req.enable
                )
            )

            // 정상적으로 적재 됬다는 가정 하에 적재
            if (rule > 0) {
                droolsModifyHistoryRepo.saveDroolsModifyHistory(
                    RuleInsertReqDto(
                        ruleId = rule,
                        ruleActionType = RuleOperationType.CREATE,
                        createdBy = username
                    )
                )
            }
            rtn.success = true
            rtn.code = StatusCode.SUCCESS.name
            rtn.message = StatusCode.SUCCESS
        }catch (e: Exception){
            logger.error("doPostCreateRule.error.$e")
        }

        return rtn
    }
    //<editor-fold desc="[POST] /create Drool 생성">

    //<editor-fold desc="[POST] /addRule rule 추가">
    override fun doPostAddRule(
        req: RuleAddRequestDto
    ): BaseCtlDto {
        var rtn = BaseCtlDto()
        var addDrl  = ""
        try {
            //ruleId 가 없을때
            if (req.ruleId == null || req.ruleId == 0){
                rtn.success = false
                rtn.code = StatusCode.RULE_ID_IS_EMPTY.name
                rtn.message = StatusCode.RULE_ID_IS_EMPTY
                return rtn
            }

            //추가 rule이 없을때
            if(req.singleRules.isNullOrEmpty() &&
                req.andRules.isNullOrEmpty() &&
                req.orRules.isNullOrEmpty()){
                rtn.success = false
                rtn.code = StatusCode.RULE_CODITION_IS_EMPTY.name
                rtn.message = StatusCode.RULE_CODITION_IS_EMPTY
                return rtn
            }



//           addDrl = ruleTemplateService.initThymeleafRenderAllRules(
//               RuleTemplateDto(
//                   ruleGroup = req.ruleGroup,
//                   singleRules = req.singleRules,
//                   andRules = req.andRules,
//                   orRules = req.orRules
//               )
//           )







        }catch (e: Exception){
            logger.error("doPostAddRule.error.$e")
        }
        return rtn
    }
    //</editor-fold desc="[POST] /addRule rule 추가">

}