package kr.co.grib.drools.api.CRules.dto

import io.swagger.v3.oas.annotations.media.Schema


//<editor-fold desc="For redis string to object">
data class RedisRuleDto (
    @Schema(description = "conditions, 룰 json text")
    val conditions: String ="",

    @Schema(description = "rule 결과 , json text")
    val actions: String ="",

    @Schema(description = "rule 활성 여부")
    val active: Boolean = false
)

data class ConditionsDto(
    val type: String="",
    val functionName: String="",
    val operator: String ="",
    val functionValue: Double? = null,
    val minValue: Double?= null,
    val maxValue: Double?= null
)

data class ActionDto(
    val actionType: String = "",
    val message: String =""
)
//</editor-fold desc="For redis string to object">