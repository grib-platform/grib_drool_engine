package kr.co.grib.drools.api.CRules.define

import com.fasterxml.jackson.annotation.JsonValue

enum class CStatusCode (@get:JsonValue val code: String){
    INIT("init"),
    SUCCESS(""),

    RULE_ID_IS_EMPTY("rule.id.is.empty"),
    RULE_GROUP_IS_EMPTY("rule.group.is.empty"),
    RULE_INFO_IS_EMPTY("rule.info.is.empty"),
    RULE_CONDITIONS_FUNCTIONNAME_IS_EMPTY("rule.conditions.FunctionName.is.empty"),
    RULE_CONDITIONS_FUNCTIONVALUE_IS_EMPTY("rule.conditions.FunctionValue.is.empty"),

    RULE_CONDITIONS_MINVALUES_IS_EMPTY("rule.conditions.min.is.empty"),
    RULE_CONDITIONS_MAXVALUES_IS_EMPTY("rule.conditions.max.is.empty"),

    RULE_REQ_CONDITIONS_IS_EMPTY("rule.request.conditions.is.empty"),
    RULE_REQ_ACTIONS_IS_EMPTY("rule.request.actions.is.empty"),
    RULE_ACTIONS_ACTION_TYPE_IS_EMPTY("rule.request.actionType.is.empty"),
    RULE_ACTIONS_MESSAGE_IS_EMPTY("rule.request.actionMessage.is.empty"),

    RULE_REQ_CONDITIONS_EXISTS("rule.request.conditions.exists"),

    RULE_CREATE_SUCCESS("rule.create.success"),

    REDIS_RULE_IS_EMPTY("redis.rule.is.empty"),

    EVALUATE_RULE_IS_EMPTY("evaluate.rule.is.empty")

}