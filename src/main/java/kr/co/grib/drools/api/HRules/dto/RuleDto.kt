package kr.co.grib.drools.api.HRules.dto

import kr.co.grib.drools.api.HRules.define.Operator

data class RuleDto (
    val ruleName: String,
    val type : String, // String, default, range, compare
    val conditions: List<Conditions>,
    val result: RuleResult
)

data class Conditions(
    val function: String,
    val operator: Operator,
    val value: Any? = null,
    val value1: Any? = null,
    val value2: Any? = null,
    val targetFunction: String? = null
)

data class RuleResult(
    val code: String,
    val messsage: String
)
