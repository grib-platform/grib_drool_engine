package kr.co.grib.drools.api.CRules.dto

import io.swagger.v3.oas.annotations.media.Schema

data class RuleDto (
    @Schema(description = "conditions, 룰 json text")
    val conditions: String,

    @Schema(description = "rule 결과 , json text")
    val actions: String,

    @Schema(description = "rule 활성 여부")
    val active: Boolean
)

data class ConditionsDto(
    val type: String ,
    val functionName: String,
    val operation: String,
    val functionValue: Double? = null,
    val minValue: Double?= null,
    val maxValue: Double?= null
)

data class actionDto(
    val actionType: String,
    val message: String
)