package kr.co.grib.drools.define

import com.fasterxml.jackson.annotation.JsonValue

enum class RuleTypeCode(@get:JsonValue val code: String) {
    SINGLE("single"),
    AND("and"),
    OR("or")
}