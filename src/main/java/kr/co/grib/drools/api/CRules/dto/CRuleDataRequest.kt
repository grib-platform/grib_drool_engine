package kr.co.grib.drools.api.CRules.dto

class CRuleDataRequest (
    val deviceId: String,
    val functionInfo:  List<CFnData>
)

data class CFnData(
    val functionName: String,
    val type: String,
    val functionValue: Int = 0
)