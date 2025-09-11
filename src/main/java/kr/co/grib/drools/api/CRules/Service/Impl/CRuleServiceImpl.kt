package kr.co.grib.drools.api.CRules.Service.Impl

import com.fasterxml.jackson.core.TreeCodec
import kr.co.grib.drools.api.CRules.Service.CRuleService
import kr.co.grib.drools.api.CRules.define.CStatusCode
import kr.co.grib.drools.api.CRules.dto.*
import kr.co.grib.drools.api.CRules.repository.IotRulesRepo
import kr.co.grib.drools.api.CRules.rules.RuleCacheLoader
import kr.co.grib.drools.api.rules.define.StatusCode
import kr.co.grib.drools.config.RequestInfoProvider
import kr.co.grib.drools.utils.Utiles
import kr.co.grib.drools.utils.getLogger
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class CRuleServiceImpl(
    private val requestInfoProvider: RequestInfoProvider,
    private val stringRedisTemplate: StringRedisTemplate,
    private val ruleCacheLoader: RuleCacheLoader,
    private val iotRulesRepo: IotRulesRepo,
    private val treeCodec: TreeCodec
) : CRuleService {

    private val logger = getLogger()

    override fun doGetCRuleSelect()
    : CRuleListResponseCtlDto {
        var rtn = CRuleListResponseCtlDto()
        try {
            val ruleData = iotRulesRepo.selectRulesList() ?: emptyList()
            val cRuleList: List<CRuleListResponseDto> = ruleData.map { rule ->
                CRuleListResponseDto(
                    id = rule.id,
                    ruleGroup =  rule.ruleGroup,
                    conditions = rule.conditions,
                    actions = rule.actions,
                    priority = rule.priority,
                    active = rule.active,
                    createdBy = rule.createdBy,
                    createdAt = rule.createdAt
                )
            }

            rtn.data = cRuleList
            rtn.success = true
            rtn.code = CStatusCode.SUCCESS.name
            rtn.message = CStatusCode.SUCCESS
        }catch (e: Exception){
            logger.error("doGetCRuleSelect.Exception.e.{}", e)
        }
        return rtn
    }

    override fun doPostCRuleSelectList(
        req: CRuleListRequestDto
    ): CRuleListResponseCtlDto {
        var rtn = CRuleListResponseCtlDto()
        var totalCount: Int = 0
        try {

            val list = iotRulesRepo.selectRuleListPaging(req) ?: emptyList()
            val data: List<CRuleListResponseDto> =  list.map {  rule ->
                CRuleListResponseDto(
                    id = rule.id,
                    ruleGroup = rule.ruleGroup,
                    conditions = rule.conditions,
                    actions = rule.actions,
                    priority = rule.priority,
                    active = rule.active,
                    createdBy = rule.createdBy,
                    createdAt = rule.createdAt,
                )
            }
            val paging = CRuleListPagingDto(
                pageNumber =  req.pageNumber,
                pageSize = req.pageSize,
                totalCount = iotRulesRepo.countCRulesInfo(),
                totalPages = ceil((iotRulesRepo.countCRulesInfo() / req.pageSize).toDouble()).toLong()
            )

            rtn.data = data
            rtn.pagingInfo = paging
            rtn.success = true
            rtn.code = CStatusCode.SUCCESS.name
            rtn.message = CStatusCode.SUCCESS

        }catch (e: Exception){
            logger.error("doPostCRuleSelectList.Exception.e.{}", e)
        }
        return rtn
    }

    override fun doPostCRuleExecute(
        req: CRuleDataRequest
    ): CRuleResponseCtlDto {
        var  rtn = CRuleResponseCtlDto()
        try {

            if (req.ruleGroup.isNullOrEmpty())
            {
                rtn.success = false
                rtn.code = CStatusCode.RULE_GROUP_IS_EMPTY.name
                rtn.message = CStatusCode.RULE_GROUP_IS_EMPTY
                return rtn
            }

            if (req.functionInfo.isNullOrEmpty())
            {
                rtn.success = false
                rtn.code = CStatusCode.RULE_INFO_IS_EMPTY.name
                rtn.message = CStatusCode.RULE_INFO_IS_EMPTY
                return rtn
            }
            val ruleGroup = "ruleGroup:"+req.ruleGroup
            val jsonString: String = stringRedisTemplate.opsForValue().get(ruleGroup)
            if (jsonString.isNullOrEmpty())
            {
                rtn.success = false
                rtn.code = CStatusCode.REDIS_RULE_IS_EMPTY.name
                rtn.message = CStatusCode.REDIS_RULE_IS_EMPTY
                return rtn

            }

            val theRule = Utiles.getStringParseRule(Utiles.getJsonToListDto(jsonString))

            val checkRule = req.functionInfo.map { fn ->
                Utiles.getCRuleMatchedAction(fn, theRule)
            }

            //평가 결과가 없는 경우
            if (checkRule.isNullOrEmpty()){
                rtn.response = listOf(
                    CRuleResponseDto(
                        code = CStatusCode.EVALUATE_RULE_IS_EMPTY.name,
                        message = CStatusCode.EVALUATE_RULE_IS_EMPTY.toString()
                    )
                )
            }

            val result = checkRule.flatten()
            rtn.response = result.map { act ->
                CRuleResponseDto(
                    code =  act.actionType,
                    message =  act.message
                )

            }
            rtn.success = true
            rtn.code = CStatusCode.SUCCESS.name
            rtn.message = CStatusCode.SUCCESS
        }catch (e: Exception){
            logger.error("doPostCRuleExecute.error.{}", e)
        }
        return rtn
    }

    //<editor-fold desc="[POST] /create cash rule 생성">
    override fun doPostCRuleCreate(
        req: CRuleCreateRequest
    ): CRuleResponseCtlDto {
        val username = requestInfoProvider.getUserName()
        var rtn = CRuleResponseCtlDto()
        try {
            if (req.ruleGroup.isNullOrEmpty()) {
                rtn.success = false
                rtn.code = CStatusCode.RULE_GROUP_IS_EMPTY.name
                rtn.message = CStatusCode.RULE_GROUP_IS_EMPTY
                return rtn
            }

            if (req.conditions == null) {
                rtn.success = false
                rtn.code = CStatusCode.RULE_REQ_CONDITIONS_IS_EMPTY.name
                rtn.message = CStatusCode.RULE_REQ_CONDITIONS_IS_EMPTY
                return rtn
            }

            if (req.actions == null) {
                rtn.success = false
                rtn.code = CStatusCode.RULE_REQ_ACTIONS_IS_EMPTY.name
                rtn.message = CStatusCode.RULE_REQ_ACTIONS_IS_EMPTY
                return rtn
            }

            // 기존의  coditions와 같은지
            val checkRule = iotRulesRepo.selectCRulesInfoByGroupRule(req.ruleGroup)
            if(checkRule != null){
                checkRule.let { it ->
                    it.forEach { item ->
                        val check = Utiles.getChkConditions(req.conditions, item.conditions)
                        if (check)
                        {
                            rtn.success = false
                            rtn.code = CStatusCode.RULE_REQ_CONDITIONS_EXISTS.name
                            rtn.message = CStatusCode.RULE_REQ_CONDITIONS_EXISTS
                            return rtn
                        }
                    }
                }
            }

            // 등록
            iotRulesRepo.insertCRules(req, username)
            //redis  refresh
            ruleCacheLoader.loadRulesToRedis()


            rtn.success = true
            rtn.code = CStatusCode.RULE_CREATE_SUCCESS.code
            rtn.message = CStatusCode.RULE_CREATE_SUCCESS
        } catch (e: Exception) {
            logger.error("doPostCRuleCreate.error.{}", e)
        }

        return rtn
    }
    //</editor-fold desc="[POST] /create cash rule 생성">

    //<editor-fold desc="[DELETE] /remove cash rule 삭제">
    override fun doDeleteCRule(
        ruleId: Int
    ): CRuleResponseCtlDto {
        val rtn = CRuleResponseCtlDto()
        try {
           if (ruleId == null){
               rtn.success = false
               rtn.code = CStatusCode.RULE_ID_IS_EMPTY.name
               rtn.message = CStatusCode.RULE_ID_IS_EMPTY
               return rtn
           }

            iotRulesRepo.deleteRule(ruleId.toLong())
            //redis  refresh
            ruleCacheLoader.loadRulesToRedis()

            rtn.success = true
            rtn.code = CStatusCode.SUCCESS.name
            rtn.message = CStatusCode.SUCCESS
        }catch (e: Exception){
            logger.error("doDeleteCRule.error.{}", e)
        }
        return rtn
    }
    //</editor-fold desc="[DELETE] /remove cash rule 삭제">

}