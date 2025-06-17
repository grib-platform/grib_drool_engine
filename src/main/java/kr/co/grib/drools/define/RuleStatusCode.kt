package kr.co.grib.drools.define

enum class RuleStatusCode(@JvmField val code: String){
    HIGH ("temperature.is.high"),
    LOW ("temperature.is.low"),
    MIDIUM ("temerature.is.midium"),

}