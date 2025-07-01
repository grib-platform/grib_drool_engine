package kr.co.grib.drools.api.rules.service.Impl

import com.fasterxml.jackson.databind.ser.Serializers.Base
import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.druleManager.dto.ActionResultDto
import kr.co.grib.drools.api.rules.druleManager.dto.RuleFactDto
import kr.co.grib.drools.api.rules.druleManager.service.DroolsManagerService
import kr.co.grib.drools.api.rules.dto.*
import kr.co.grib.drools.api.rules.repository.DroolsAuditHistoryRepo
import kr.co.grib.drools.api.rules.repository.DroolsModifyHistoryRepo
import kr.co.grib.drools.api.rules.repository.DroolsRepo
import kr.co.grib.drools.api.rules.service.RuleService
import kr.co.grib.drools.api.rules.templateManager.dto.RuleTemplateDto
import kr.co.grib.drools.api.rules.templateManager.service.RuleTemplateService
import kr.co.grib.drools.config.RequestInfoProvider
import kr.co.grib.drools.api.rules.define.RuleExecuteType
import kr.co.grib.drools.api.rules.define.RuleOperationType
import kr.co.grib.drools.api.rules.define.StatusCode
import kr.co.grib.drools.utils.Utiles
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

    //<editor-fold desc="[POST] /create Drool 생성">
    override fun doPostCreateRule(
        req: RuleCreateRequestDto
    ): BaseCtlDto {
        val rtn  = BaseCtlDto()
        val username = requestInfoProvider.getUserName()
        var ruleDrl: String
        try {
            logger.info("req.$req")
            if (req.ruleGroup.isEmpty()){
                rtn.success = false
                rtn.code = StatusCode.RULE_GROUP_IS_EMPTY.name
                rtn.message = StatusCode.RULE_GROUP_IS_EMPTY
                return rtn
            }

            //Rule 파일 만들기
            ruleDrl = ruleTemplateService.initThymeleafRenderAllRules(
                RuleTemplateDto(
                    ruleGroup = req.ruleGroup,
                    stringRules = req.stringRules,
                    defaultRules = req.defaultRules,
                    rangeRules = req.rangeRules
                ),
                true
            )

            logger.info("rule.Drl.create.info.$ruleDrl")
            val ruleChk = droolsRepo.countDrools(req.ruleGroup)
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
                        ruleText = ruleDrl,
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

    //<editor-fold desc="[PATCH] /addRule rule 추가">
    override fun doPatchAddRule(
        req: RuleAddRequestDto
    ): BaseCtlDto {
        var rtn = BaseCtlDto()
        var addDrl: String
        var mergedText: String
        val username = requestInfoProvider.getUserName()
        try {
            //ruleId 가 없을때
            if (req.ruleGroup.isNullOrEmpty()){
                rtn.success = false
                rtn.code = StatusCode.RULE_GROUP_IS_EMPTY.name
                rtn.message = StatusCode.RULE_GROUP_IS_EMPTY
                return rtn
            }

            //추가 rule이 없을때
            if(req.stringRules.isNullOrEmpty() &&
                req.defaultRules.isNullOrEmpty() &&
                req.rangeRules.isNullOrEmpty()){
                rtn.success = false
                rtn.code = StatusCode.RULE_CODITION_IS_EMPTY.name
                rtn.message = StatusCode.RULE_CODITION_IS_EMPTY
                return rtn
            }

           val ruleInfo = droolsRepo.selectRulesText(req.ruleGroup)
           if (ruleInfo == null){
                rtn.success = false
                rtn.code = StatusCode.RULE_INFO_IS_NULL.name
                rtn.message = StatusCode.RULE_INFO_IS_NULL
               return rtn
           }

           addDrl = ruleTemplateService.initThymeleafRenderAllRules(
               RuleTemplateDto(
                   ruleGroup = ruleInfo.ruleGroup,
                   stringRules = req.stringRules,
                   defaultRules = req.defaultRules,
                   rangeRules = req.rangeRules
               ),
               false
           )

            /**
             * 같은 이름의 rule이 존재 하면 덮어쓰고
             * 그렇지 않으면 신규 추가
             * **/
            mergedText = Utiles.mergeDrlRuleOverwrite(ruleInfo.ruleText, addDrl)
            val update = droolsRepo.updateRule(req.ruleGroup ,  mergedText )

            logger.info("check.rule.id.{}", ruleInfo.ruleId)

            //HISTORY 추가
            if (update >=1){
                droolsModifyHistoryRepo.saveDroolsModifyHistory(
                    RuleInsertReqDto(
                        ruleId = ruleInfo.ruleId.toInt(),
                        ruleActionType = RuleOperationType.UPDATE,
                        ruleText = addDrl,
                        createdBy = username
                    )
                )
            }
            rtn.success = true
            rtn.code = StatusCode.SUCCESS.name
            rtn.message = StatusCode.SUCCESS
        }catch (e: Exception){
            logger.error("doPostAddRule.error.$e")
        }
        return rtn
    }

    //</editor-fold desc="[PATCH] /addRule rule 추가">

    //<editor-fold desc="[DELETE] /deleteRule rule 삭제">
    override fun doDeleteRule(
        ruleGroup: String,
        ruleName: List<String>
    ):BaseCtlDto {
        val rtn = BaseCtlDto()
        val username = requestInfoProvider.getUserName()
        try {
            //ruleId 체크
            if (ruleGroup.isNullOrEmpty()) {
                rtn.success = false
                rtn.code = StatusCode.RULE_GROUP_IS_EMPTY.name
                rtn.message = StatusCode.RULE_GROUP_IS_EMPTY
                return  rtn
            }

            val drl = droolsRepo.selectRulesText(ruleGroup)
            if (drl == null) {
                rtn.success = false
                rtn.code = StatusCode.RULE_IS_EMPTY.name
                rtn.message = StatusCode.RULE_IS_EMPTY
                return rtn
            }

            //삭제할 rulename 목록 여부
            if(ruleName.isNullOrEmpty()){
                rtn.success = false
                rtn.code = StatusCode.RULE_NAME_IS_EMPTY.name
                rtn.message = StatusCode.RULE_NAME_IS_EMPTY
                return rtn
            }

            // check ruleName 이 있는지 없는지
            val check = Utiles.countExistingRules(drl.ruleText, ruleName)
            logger.info("check.$check")
            if (check <=0){
                rtn.success = false
                rtn.code = StatusCode.RULE_NO_EXISTS.name
                rtn.message = StatusCode.RULE_NO_EXISTS
                return rtn
            }

            val deleteDrlStr =  Utiles.removeRuleByRuleName(drl.ruleText, ruleName)
            val updated = droolsRepo.updateRule(ruleGroup, deleteDrlStr)
            logger.info("drl.check.ruleId.{}", drl.ruleId)
            if (updated >=1){
                droolsModifyHistoryRepo.saveDroolsModifyHistory(
                    RuleInsertReqDto(
                        ruleId = drl.ruleId.toInt(),
                        ruleActionType = RuleOperationType.DELETE,
                        ruleText = Utiles.extractRulesByNames(drl.ruleText, ruleName),
                        createdBy = username
                    )
                )
            }

            rtn.success = true
            rtn.code = StatusCode.SUCCESS.name
            rtn.message = StatusCode.SUCCESS
        }catch (e: Exception){
            logger.error("doDeleteRule.error.$e")
        }
        return rtn
    }
    //</editor-fold desc="[DELETE] /deleteRule rule 삭제">

    //<editor-fold desc="[DELETE] /remove rule 자체 삭제">
    override fun doRemoveRule(
        ruleGroup: String
    ):BaseCtlDto {
        val rtn = BaseCtlDto()
        try{
            //ruleId 체크
            if (ruleGroup.isNullOrEmpty()) {
                rtn.success = false
                rtn.code = StatusCode.RULE_GROUP_IS_EMPTY.name
                rtn.message = StatusCode.RULE_GROUP_IS_EMPTY
                return  rtn
            }

            //해당 rule 이 존재 하는지 확인
            val check = droolsRepo.selectRulesText(ruleGroup)
            logger.info("check.{}", check)
            if (check == null){
                rtn.success = false
                rtn.code = StatusCode.RULE_ID_IS_EMPTY.name
                rtn.message = StatusCode.RULE_ID_IS_EMPTY
                return rtn
            }

            //history를 먼저 삭제를 하고, Drools 삭제
            val history = droolsModifyHistoryRepo.deleteDroolsModifyHistory(check.ruleId)
            if (history>=1){
                droolsRepo.deleteRule(check.ruleId)
            }

            rtn.success = true
            rtn.code = StatusCode.SUCCESS.name
            rtn.message = StatusCode.SUCCESS
        }catch (e: Exception){
            logger.error("doRemove.error.$e")
        }
        return  rtn
    }
    //</editor-fold desc="[DELETE] /remove rule 자체 삭제">

    //<editor-fold desc="[POST] /execute rule 파일 실행">
    override fun doPostExecute(
        req: RuleRequestDto
    ): RuleResponseCtlDto {
        val rtn = RuleResponseCtlDto()
        val result = ActionResultDto()
        try {
            if (req.ruleGroup.isNullOrEmpty()){
                rtn.success = false
                rtn.code = StatusCode.RULE_GROUP_IS_EMPTY.name
                rtn.message = StatusCode.RULE_GROUP_IS_EMPTY
                return rtn
            }

            if (req.facts.isEmpty()) {
                logger.error("fact.is.null")
                rtn.success = false
                rtn.code = StatusCode.FACTS_IS_EMPTY.name
                return rtn
            }

           val chk = droolsRepo.selectRulesText(req.ruleGroup)
           if (chk == null)
           {
               rtn.success = false
               rtn.code = StatusCode.RULE_NO_EXISTS.name
               rtn.message = StatusCode.RULE_NO_EXISTS
               return rtn
           }


            val kieSession = droolsManagerService.initTemplateToDrools(chk.ruleText)

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
                    groupId = chk.ruleGroup,
                    deviceId = it.deviceId,
                    functionName = it.functionName,
                    functionValue = it.functionValue
                )
                kieSession.insert(fact)
            }

            kieSession.fireAllRules()
            kieSession.dispose()

            if (result.result.isNullOrBlank() ||
                result.ruleFired.isNullOrBlank() ||
                result.action.isNullOrBlank()){

                rtn.success = false
                rtn.code = StatusCode.FAIL_TO_RULE_FIRE.code
                rtn.message = StatusCode.FAIL_TO_RULE_FIRE
                return rtn
            }

            droolsAuditHistoryRepo.saveDroolsAuditHistory(
                 RuleAuditHistoryDto(
                     ruleName = result.ruleFired!!,
                     ruleAditIp = requestInfoProvider.getClientIp()!!,
                     ruleApiMethod = requestInfoProvider.getApiMethod()!!,
                     ruleApi = requestInfoProvider.getApiUri()!!,
                     ruleFact = req.facts.toString(),
                     ruleResult = result.result,
                     ruleAditStatus = RuleExecuteType.SUCCESS.code,
                     ruleMsg =  result.action!!,
                     userName = requestInfoProvider.getUserName()
                 )
            )

            rtn.body = RuleResponseDto(
                ruleGroup = chk.ruleGroup,
                result = result.ruleFired.toString(),
            )
            rtn.success = true
            rtn.code = StatusCode.SUCCESS.code
            rtn.message =  StatusCode.SUCCESS

        }catch (e: Exception){
            logger.error("error.$e")
        }
        return rtn

    }
    //</editor-fold desc="[POST] /execute rule 파일 실행">
}