package kr.co.grib.drools.api.rules.druleManager.dto

data class ActionResultDto (
    @get:JvmName("getResult")
    var result: String = "NO_ACTION",
    @get:JvmName("getAction")
    var action: String? = null,
    @get:JvmName("setRuleFired")
    var ruleFired: String? = null,
)