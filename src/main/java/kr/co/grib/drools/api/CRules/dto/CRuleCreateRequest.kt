package kr.co.grib.drools.api.CRules.dto

class CRuleCreateRequest (
    val ruleGroup: String,
    val conditions: Conditions,
    val actions: Actions,
    val priority: Int ,
    val active: Int
)

data class Conditions(
    val type: String,
    val functionName: String,
    val operation: String,
    val functionValue: String
)

data class Actions(
    val actionType: String,
    val message: String
)

