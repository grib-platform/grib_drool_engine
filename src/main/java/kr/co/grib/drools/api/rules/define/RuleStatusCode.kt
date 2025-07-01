package kr.co.grib.drools.api.rules.define

/**
 *  RULE 파일에 들어가는
 *  RULE 관련 텍스트 코드
 * **/
enum class RuleStatusCode(@JvmField val code: String){

    MATCH("string value match"),
    GT("default value greater than"),
    GTE("default value greater than to"),
    LT("default value less than"),
    LTE("default value less than or equal to"),
    RANGE("value range"),

}