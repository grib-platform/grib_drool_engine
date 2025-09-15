package kr.co.grib.drools.api.CRules.dto

import java.time.LocalDateTime

data class CRuleListResponseDto(
    val ruleId: Long,
    val ruleGroup: String,
    val conditions: String,
    val actions: String,
    val priority: Int,
    val active: Boolean,
    val functionName: String,
    val functionValue: String,
    val actionType: String,
    val operator: String,
    val message: String,
    val createdBy: String,
    val createdAt: LocalDateTime
)