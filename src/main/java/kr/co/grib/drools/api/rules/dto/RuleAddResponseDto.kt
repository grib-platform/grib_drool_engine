package kr.co.grib.drools.api.rules.dto

import io.swagger.v3.oas.annotations.media.Schema

data class RuleAddResponseDto (
    @Schema(description = "룰 ID")
    val ruleId: Long,

    @Schema(description = "룰 group")
    val ruleGroup: String,

    @Schema(description = "룰 Text")
    val ruleText: String,
)