package kr.co.grib.drools.api.HRules.define

import com.fasterxml.jackson.annotation.JsonValue

enum class HStatusCode(@get:JsonValue val code: String) {

    INIT("init"),
    SUCCESS(""),

    //Request
    NO_DEVICE_ID("no.device.id"),
    NO_FUNCTION_INFO("no.function.info"),
    NO_RULE_FIRE("no.rule.fire")
}