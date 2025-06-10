package kr.co.grib.drools.api.rules.dto

import io.swagger.v3.oas.annotations.media.Schema

data class RuleRequestDto (
    @Schema(description = "groupId")
    val groupId: String,
    @Schema(description = "deviceId")
    val deviceId: String,
    @Schema(description = "function 이름")
    val functionName: String,
    @Schema(description = "function 값")
    val functionValue: Double,
)