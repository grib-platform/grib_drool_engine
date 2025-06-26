package kr.co.grib.drools.api.HRules.service.Impl

import kr.co.grib.drools.api.HRules.define.HStatusCode
import kr.co.grib.drools.api.HRules.dto.HRuleDataRequest
import kr.co.grib.drools.api.HRules.dto.HRuleResponseCtlDto
import kr.co.grib.drools.api.HRules.dto.RuleDto
import kr.co.grib.drools.api.HRules.dto.RuleResult
import kr.co.grib.drools.api.HRules.rules.HRuleFile
import kr.co.grib.drools.api.HRules.service.HRuleService
import kr.co.grib.drools.utils.Utiles
import kr.co.grib.drools.utils.getLogger
import org.springframework.stereotype.Service

@Service
class HRuleServiceImpl : HRuleService {
    private val logger = getLogger()

    //<editor-fold desc="HFile에 있는 Rule List">
    private val ruleList = listOf(
        HRuleFile.stringMatchRule,
        HRuleFile.greaterThanRule,
        HRuleFile.greaterThanOrEqualRule,
        HRuleFile.lessThanRule,
        HRuleFile.lessThanOrEqualRule,
        HRuleFile.equalToRule,
        HRuleFile.insideRangeRule,
        HRuleFile.outsideRangeRule
    )
    //</editor-fold desc="HFile에 있는 Rule List">


    override fun doPostHRuleExecute(
        req: HRuleDataRequest
    ): HRuleResponseCtlDto {
        val rtn = HRuleResponseCtlDto()
        val matchRules: List<RuleDto>
        val matchRuleResult: List<RuleResult>
        try {
            if (req.deviceId.isNullOrEmpty()){
                rtn.code = HStatusCode.NO_DEVICE_ID.name
                rtn.message = HStatusCode.NO_DEVICE_ID
                return rtn
            }
            if (req.functionInfo.isNullOrEmpty()){
                rtn.code  = HStatusCode.NO_FUNCTION_INFO.name
                rtn.message = HStatusCode.NO_FUNCTION_INFO
                return rtn
            }

            matchRules = ruleList.filter { rule ->
                logger.info("rule.$rule")
                Utiles.setEvaluationRules(rule, req.functionInfo)
            }

            if (matchRules.isNullOrEmpty()){
                rtn.code = HStatusCode.NO_RULE_FIRE.name
                rtn.message = HStatusCode.NO_RULE_FIRE
                return rtn
            }
            matchRuleResult = matchRules.map { it.result }

            rtn.code = HStatusCode.SUCCESS.name
            rtn.message = HStatusCode.SUCCESS
            rtn.response = matchRuleResult

        }catch (e: Exception){
            logger.error("doPostHRuleExecute.Error.$e")
        }
        return rtn
    }
}