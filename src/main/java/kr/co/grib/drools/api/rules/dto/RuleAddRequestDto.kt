package kr.co.grib.drools.api.rules.dto

import io.swagger.v3.oas.annotations.media.Schema

data class RuleAddRequestDto (

    @Schema(description = "룰 group 명")
    val ruleGroup:String,

    @Schema(description = "String rule conditions")
    val stringRules: List<StringRuleDto> ?= null,

    @Schema(description = "Default rule conditions")
    val defaultRules: List<DefaultRuleDto> ?= null,

    @Schema(description = "Range rule conditions")
    val rangeRules: List<RangeRuleDto> ?= null
)

