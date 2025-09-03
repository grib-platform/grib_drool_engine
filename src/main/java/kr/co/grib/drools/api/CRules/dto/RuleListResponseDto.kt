package kr.co.grib.drools.api.CRules.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class RuleListResponseDto (
    @Schema(description = "tb_iot_rule 고유 ID")
    val id: Long ,

    @Schema(description = "ruleGroup")
    val ruleGroup: String,

    @Schema(description = "rule 조건")
    val conditions: String,

    @Schema(description = "rule 결과")
    val actions: String,

    @Schema(description = "priority")
    val priority: Int,

    @Schema(description = "active")
    val active: Boolean,

    @Schema(description = "createdBy")
    val createdBy: String,

    @Schema(description = "createdAt")
    val createdAt: LocalDateTime,

    @Schema (description = "updatedBy")
    val updatedBy: String,

    @Schema(description = "updatedAt")
    val updatedAt: LocalDateTime
)