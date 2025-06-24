package kr.co.grib.drools.api.gRules.dto

data class RuleDto (
    val ruleName: String,
    val type : String,
    val conditions: List<Conditions>,
    val result: RuleResult
)

data class Conditions(
    val function: String,
    val operator: String,
    val value: Any? = null,
    val value1: Any? = null,
    val value2: Any? = null,
    val targetFunction: String? = null
)

data class RuleResult(
    val code: String,
    val messsage: String
)
