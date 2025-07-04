package kr.co.grib.drools.api.CRules.Service.Impl

import kr.co.grib.drools.api.CRules.Service.CRuleService
import kr.co.grib.drools.api.CRules.define.CStatusCode
import kr.co.grib.drools.api.CRules.dto.CRuleDataRequest
import kr.co.grib.drools.api.CRules.dto.CRuleResponseCtlDto
import kr.co.grib.drools.api.CRules.dto.RedisRuleDto
import kr.co.grib.drools.utils.Utiles
import kr.co.grib.drools.utils.getLogger
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class CRuleServiceImpl(
    private val stringRedisTemplate: StringRedisTemplate
) : CRuleService {

    private val logger = getLogger()

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

           // val check = Utiles.getJsonToDto(jsonString, RedisRuleDto::class.java)
            val checkList: List<RedisRuleDto> = Utiles.getJsonToListDto(jsonString)




        }catch (e: Exception){
            logger.error("doPostCRuleExecute.error.{}", e)
        }
        return rtn
    }


}