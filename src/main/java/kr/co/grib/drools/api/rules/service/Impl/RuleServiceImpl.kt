package kr.co.grib.drools.api.rules.service.Impl

import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.druleManager.dto.ActionResultDto
import kr.co.grib.drools.api.druleManager.dto.RuleFactDto
import kr.co.grib.drools.api.druleManager.service.DroolsManagerService
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.dto.RuleResponseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleResponseDto
import kr.co.grib.drools.api.rules.service.RuleService
import kr.co.grib.drools.define.StatusCode
import kr.co.grib.drools.utils.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class RuleServiceImpl (
    private val droolsManagerService: DroolsManagerService,
    @Value("\${drools.rule-path}") private val rulePath: String,
) : RuleService{

    private val logger = getLogger()

    override
    fun doGetRules(req: RuleRequestDto) :BaseCtlDto{
        val rtn = BaseCtlDto();
        return rtn;
    }

    override fun doEvaluateRules(
        req: RuleRequestDto
    ): RuleResponseCtlDto {
        val rtn = RuleResponseCtlDto();
        val result = ActionResultDto()
        try {

            if (req.groupId.isEmpty()){
                rtn.success = false
                rtn.code = StatusCode.GROUP_ID_IS_EMPTY
                return  rtn
            }

            if (req.deviceId.isEmpty()){
                rtn.success = false
                rtn.code = StatusCode.DEVICE_ID_IS_EMPTY
                return rtn
            }

            droolsManagerService.initRule(req.groupId,rulePath)
            val kieSession = droolsManagerService.initKieSession(req.groupId)
            if (kieSession == null){
                rtn.success = false
                rtn.code = StatusCode.INIT_ERROR_KIESESSION
                return rtn
            }

            val fact = RuleFactDto(
                groupId = req.groupId,
                deviceId = req.deviceId,
                functionName = req.functionName,
                functionValue = req.functionValue
            )

            kieSession.insert(fact)
            kieSession.insert(result)
            kieSession.fireAllRules()
            kieSession.dispose()

            rtn.success = true
            rtn.code = StatusCode.SUCCESS
            rtn.body = result.action?.let {
                RuleResponseDto(
                    groupId = req.groupId,
                    action = it,
                    result =  result.result
                )
            }
            return rtn
        }catch (e: Exception){
            logger.error("doEvaluateRules.Error.$e")
        }
        return rtn
    }

    override fun doEvaluateThymeleafRules(
        req: RuleRequestDto
    ): RuleResponseCtlDto
    {
        val rtn = RuleResponseCtlDto()
        try{




        }catch (e: Exception){
            logger.error("error.$e")
        }
        return rtn
    }


}