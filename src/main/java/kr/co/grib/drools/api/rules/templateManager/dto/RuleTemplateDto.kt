package kr.co.grib.drools.api.rules.templateManager.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.grib.drools.api.rules.dto.*

data class RuleTemplateDto (

    @Schema(description = "룰 그룹 ID")
    val ruleGroup: String,

    @Schema(description = "single rule conditions")
    val stringRules: List<StringRuleDto> ?= null,

    @Schema(description = "And rule conditions")
    val defaultRules: List<DefaultRuleDto> ?= null,

    @Schema(description = "Or rule conditions")
    val rangeRules: List<RangeRuleDto> ?= null
)