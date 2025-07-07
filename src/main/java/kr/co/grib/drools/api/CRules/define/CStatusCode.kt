package kr.co.grib.drools.api.CRules.define

import com.fasterxml.jackson.annotation.JsonValue

enum class CStatusCode (@get:JsonValue val code: String){
    INIT("init"),
    SUCCESS(""),

    RULE_GROUP_IS_EMPTY("rule.group.is.empty"),
    RULE_INFO_IS_EMPTY("rule.info.is.empty"),
    REDIS_RULE_IS_EMPTY("redis.rule.is.empty"),

    EVALUATE_RULE_IS_EMPTY("evaluate.rule.is.empty")

}