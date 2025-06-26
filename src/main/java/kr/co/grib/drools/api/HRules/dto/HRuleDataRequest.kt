package kr.co.grib.drools.api.HRules.dto

data class HRuleDataRequest (
    val deviceId : String,
    val functionInfo:  List<FnData>
)

data class FnData(
    val functionName: String,
    val type: String,
    val functionValue: Int = 0
)



