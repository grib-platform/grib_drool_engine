package kr.co.grib.drools.define

/**
 *  RULE 파일에 들어가는
 *  RULE 관련 텍스트 코드
 * **/
enum class RuleStatusCode(@JvmField val code: String){

    HIGH ("temperature.is.high"),
    LOW ("temperature.is.low"),
    NORMAL("temperature.is.normal"),
    MIDIUM ("temerature.is.midium"),

}