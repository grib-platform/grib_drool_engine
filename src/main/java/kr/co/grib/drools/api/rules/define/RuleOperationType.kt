package kr.co.grib.drools.api.rules.define

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class RuleOperationType {
    CREATE,
    UPDATE,
    DELETE
}

enum class RuleTypeCode(@get:JsonValue val code: String) {
    NONE(""),
    STRING("string"),
    DEFAULT("default"),
    RANGE("range"),
}

enum class RuleOperationCode(@get:JsonValue val code: String) {
    NONE(""),
    MATCH("match"),
    GT("greater than"),
    GTE("greater than or equal to"),
    LT("less than"),
    LTE("less than or equal to"),
    INSIDE("inside"),
    OUTSIDE("outside"),
}

enum class RuleEnableType(@get:JsonValue val code: Boolean) {
    Y(true),
    N(false);

    companion object {
        @JvmStatic
        @JsonCreator
        fun from(value: String): RuleEnableType {
            return when (value.uppercase()) {
                "Y" -> Y
                "N" -> N
                else -> throw IllegalArgumentException("Invalid RuleEnableType: $value")
            }
        }
    }
}

enum class RuleExecuteType(@get:JsonValue val code: String) {
    SUCCESS("RULE.FIRE.SUCCESS"),
    FAILE("RULE.FIRE.FAIL")
}

