package kr.co.grib.drools.api.CRules.dto

import io.swagger.v3.oas.annotations.media.Schema

//DB에서 나온 response
data class RuleResponseDto (
    @Schema(description = "rule group 명")
    val ruleGroup: String,

    @Schema(description = "conditions , 룰 text")
    val conditions: String,

    @Schema(description = "rule action")
    val actions: String,

    @Schema(description = "활성 여부")
    val active: Boolean,

    @Schema(description = "우선 순위")
    val priority: Int,
)