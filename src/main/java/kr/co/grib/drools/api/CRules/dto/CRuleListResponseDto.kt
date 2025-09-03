package kr.co.grib.drools.api.CRules.dto

import java.time.LocalDateTime

data class CRuleListResponseDto(
    val id: Long,
    val ruleGroup: String,
    val conditions: String,
    val actions: String,
    val priority: Int,
    val active: Boolean,
    val createdBy: String,
    val createdAt: LocalDateTime
)