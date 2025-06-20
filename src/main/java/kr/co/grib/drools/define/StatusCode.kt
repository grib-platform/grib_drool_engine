package kr.co.grib.drools.define

import com.fasterxml.jackson.annotation.JsonValue

enum class StatusCode(@get:JsonValue val code: String) {
    INIT("init"),
    SUCCESS(""),

    // RULE
    RULE_GROUP_IS_EMPTY("rule.group.is.empty"),
    RULE_SET_NAME_IS_EMPTY("rule.set.name.is.empty"),
    RULE_ALREADY_EXISTS("rule.already.exists"),
    RULE_ID_IS_EMPTY("rule.id.is.empty"),
    RULE_CODITION_IS_EMPTY("rule.condition.is.empty"),

    //RULE FILE
    INIT_ERROR_KIESESSION("init.error.kiesession"),
    GROUP_ID_IS_EMPTY("groupId.is.empty"),
    FACTS_IS_EMPTY("facts.is.empty"),

    //TOKEN
    JWT_TOKEN_HEADER_ERROR("jwt.token.header.error"),
    JWT_TOKEN_INVALID_EXPIRED("jwt.token.invalid.expired"),
    JWT_TOKEN_IS_EMPTY("jwt.token.is.empty"),
    JWT_HAS_NO_USER_NAME("jwt.has.no.user.name"),

}