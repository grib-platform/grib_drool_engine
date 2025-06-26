package kr.co.grib.drools.api.rules.druleManager.dto

data class RuleFactDto (
    val groupId:  String,
    val deviceId: String,
    val functionName: String,
    val functionValue: Double
)