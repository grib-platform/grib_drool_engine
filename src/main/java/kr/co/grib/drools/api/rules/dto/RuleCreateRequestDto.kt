package kr.co.grib.drools.api.rules.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.grib.drools.api.rules.define.RuleEnableType
import kr.co.grib.drools.api.rules.define.RuleOperationCode
import kr.co.grib.drools.api.rules.define.RuleTypeCode

data class RuleCreateRequestDto (

    @Schema(description = "룰 그룹 ID")
    val ruleGroup: String,

    @Schema(description = "룰 활성 여부(Y/N)")
    val enable: RuleEnableType,

    @Schema(description = "string rule conditions")
    val stringRules: List<StringRuleDto> ?= null,

    @Schema(description = "default rule conditions")
    val defaultRules: List<DefaultRuleDto> ?= null,

    @Schema(description = "range rule conditions")
    val rangeRules: List<RangeRuleDto> ?= null
)

data class StringRuleDto(
    val ruleName: RuleTypeCode,
    @Schema(description = "ruleType (NONE,MATCH,GT(>),GTE(>=),LT(<),LTE(<=),INSIDE(&),OUTSIDE(||))", example = "GT")
    val ruleType: RuleOperationCode,
    val deviceId: String,
    val functionName: String,
    val functionValue: Double
)

data class DefaultRuleDto(
    val ruleName: RuleTypeCode,
    @Schema(description = "ruleType (NONE,MATCH,GT(>),GTE(>=),LT(<),LTE(<=))", example = "GT")
    val ruleType: RuleOperationCode,
    val deviceId: String,
    val functionName: String,
    val functionValue: Double
)

data class RuleConditionDto(
    val deviceId: String,
    @Schema(description = "ruleType (INSIDE(&&), OUTSIDE(||))", example = "INSIDE")
    val ruleRangeType: RuleOperationCode,
    val minValue: Double,
    val maxValue: Double
)

data class RangeRuleDto(
    val ruleName: RuleTypeCode,
    val functionName: String,
    val conditions: RuleConditionDto? = null
)











