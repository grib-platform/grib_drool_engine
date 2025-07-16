package kr.co.grib.drools.api.CRules.rules

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.grib.drools.api.CRules.repository.IotRulesRepo
import kr.co.grib.drools.utils.getLogger
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RuleCacheLoader (
    private val iotRulesRepo: IotRulesRepo,
    private val redisTemplate: RedisTemplate<String,String>
): ApplicationRunner{

    private val logger = getLogger()
    private val ojbectMapper = ObjectMapper()

    //<editor-fold desc="DB에서 rule 가져 오기">
    override fun run(args: ApplicationArguments?) {

        //TODO. 만약에 db에 저장된 rule이 10만개 이면 10만개 모두를 서비스가 시작이 될때, 불러 와야 하는 점이 있다.
        //TODO. 이 부분에 대해서는 좀 더 고려 해 볼 필요가 있다.
        //TODO. 변경 해야 하는 부분은 execute 할때 redis에 질의 해서 가져 올 수 있게 해준다.
        val ruleInfo  = iotRulesRepo.selectRuleText()
        if(ruleInfo.isNullOrEmpty()){
            logger.warn("No rule info")
            return
        }

        //redis 저장
        val ruleGroup = ruleInfo.groupBy { it.ruleGroup }
        ruleGroup.forEach { (group, rules) ->
            val redisKey = "ruleGroup:$group"
            val redisValueList = rules.map {  rule ->
                mapOf(
                    "conditions" to rule.conditions,
                    "actions" to rule.actions,
                    "active" to rule.active
                )
            }
            val redisValue = ojbectMapper.writeValueAsString(redisValueList)
            redisTemplate.opsForValue().set(redisKey, redisValue)
        }
    }
    //</editor-fold desc="DB에서 rule 가져 오기">
}