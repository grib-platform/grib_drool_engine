package kr.co.grib.drools.api.rules.dto

import io.swagger.v3.oas.annotations.media.Schema

data class RuleAddRequestDto (

    @Schema(description = "룰 ID")
    val ruleId: Int,

    @Schema(description = "single rule conditions")
    val singleRules: List<SingleRuleDto> ?= null,

    @Schema(description = "And rule conditions")
    val andRules: List<AndRuleDto> ?= null,

    @Schema(description = "Or rule conditions")
    val orRules: List<OrRuleDto> ?= null
)

