package kr.co.grib.drools.api.CRules.dto

import io.swagger.v3.oas.annotations.media.Schema

class CRuleUpdateRequestDto (

    @Schema(description = "ruleID")
    val ruleId: Int,
    @Schema(description = "ruleGroup")
    val ruleGroup: String,

    @Schema(description = "conditions")
    val conditions: CRuleConditionRequestDto,
    @Schema(description = "actions")
    val actions: CRuleActionRequestDto,
    @Schema(description = "active 여부" , example = "active/deactivate")
    val active: String,


)

class CRuleConditionRequestDto (
    @Schema(description = "conditionType" , example = "defalut, string, range")
    val type: String,
    @Schema(description = "functionName")
    val functionName: String,
    @Schema(description = "functionValue")
    val functionValue: Long,
    @Schema(description = "operator" , example = "string (MATCH) , default (LT, LTE, E , GTE, GT) , range (OUTSIDE, INSIDE)" )
    val operator: String,
    @Schema(description = "minValue" , example = "operator가 range 일때만 , 나머진 null ")
    val minValue: Long ?= null,
    @Schema(description = "maxValue")
    val maxValue: Long ?= null,
)

class CRuleActionRequestDto(
    @Schema(description = "actionType")
    val actionType: String,
    @Schema(description = "message")
    val message: String
)