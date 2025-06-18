package kr.co.grib.drools.define

import com.fasterxml.jackson.annotation.JsonValue

enum class StatusCode(@get:JsonValue val code: String) {
    INIT("init"),

    SUCCESS(""),

    GROUP_ID_IS_EMPTY("groupId.is.empty"),
    FACTS_IS_EMPTY("facts.is.empty"),
    DEVICE_ID_IS_EMPTY("deviceId.is.empty"),

    CREATED_BY_IS_EMPTY("created.by.is.empty"),
    RULE_GROUP_IS_EMPTY("rule.group.is.empty"),

    INIT_ERROR_KIESESSION("init.error.kiesession"),




}