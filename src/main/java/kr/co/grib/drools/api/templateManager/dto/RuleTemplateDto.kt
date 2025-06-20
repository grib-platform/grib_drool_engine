package kr.co.grib.drools.api.templateManager.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.grib.drools.api.rules.dto.AndRuleDto
import kr.co.grib.drools.api.rules.dto.OrRuleDto
import kr.co.grib.drools.api.rules.dto.SingleRuleDto

data class RuleTemplateDto (

    @Schema(description = "룰 그룹 ID")
    val ruleGroup: String,

    @Schema(description = "single rule conditions")
    val singleRules: List<SingleRuleDto> ?= null,

    @Schema(description = "And rule conditions")
    val andRules: List<AndRuleDto> ?= null,

    @Schema(description = "Or rule conditions")
    val orRules: List<OrRuleDto> ?= null
)