package kr.co.grib.drools.define

import com.fasterxml.jackson.annotation.JsonValue

enum class StatusCode(@get:JsonValue val code: String) {
    INIT("init"),

    SUCCESS(""),

    GROUP_ID_IS_EMPTY("groupId.is.empty"),
    DEVICE_ID_IS_EMPTY("deviceId.is.empty"),

    INIT_ERROR_KIESESSION("init.error.kiesession"),




}