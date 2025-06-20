package kr.co.grib.drools.api.rules.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.grib.drools.define.RuleEnableType
import kr.co.grib.drools.define.RuleTypeCode

data class RuleCreateRequestDto (

    @Schema(description = "룰 그룹 ID")
    val ruleGroup: String,

    @Schema(description = "전체 룰 세트 명")
    val ruleSetName: String,

    @Schema(description = "룰 활성 여부(Y/N)")
    val enable: RuleEnableType ,

    @Schema(description = "single rule conditions")
    val singleRules: List<SingleRuleDto> ?= null,

    @Schema(description = "And rule conditions")
    val andRules: List<AndRuleDto> ?= null,

    @Schema(description = "Or rule conditions")
    val orRules: List<OrRuleDto> ?= null
)

data class SingleRuleDto(
    val ruleName: String,
    val ruleType: RuleTypeCode = RuleTypeCode.SINGLE,
    val deviceId: String,
    val functionName: String,
    val functionValue: Double
)

data class AndRuleDto(
    val ruleName: String,
    val ruleType: RuleTypeCode = RuleTypeCode.AND,
    val deviceId: String,
    val functionName: String,
    val minValue: Double,
    val maxValue: Double
)

data class OrRuleConditionDto(
    val deviceId: String,
    val functionName: String
)

data class OrRuleDto(
    val ruleName: String,
    val ruleType: RuleTypeCode = RuleTypeCode.OR,
    val conditions: List<OrRuleConditionDto> ?= null
)












