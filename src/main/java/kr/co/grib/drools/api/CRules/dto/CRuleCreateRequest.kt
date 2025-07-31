package kr.co.grib.drools.api.CRules.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class CRuleCreateRequest (
    val ruleGroup: String,
    val conditions: Conditions,
    val actions: Actions,
    val active: Boolean
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Conditions(
    val type: String,
    val functionName: String,
    val operator: String,
    val functionValue: Int?,
    val minValue: Int?,
    val maxValue: Int?
)

data class Actions(
    val actionType: String,
    val message: String
)

