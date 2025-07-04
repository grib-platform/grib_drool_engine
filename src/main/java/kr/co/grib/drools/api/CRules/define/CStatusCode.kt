package kr.co.grib.drools.api.CRules.define

import com.fasterxml.jackson.annotation.JsonValue

enum class CStatusCode (@get:JsonValue val code: String){
    INIT("init"),
    SUCCESS(""),


}