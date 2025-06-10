package kr.co.grib.drools.define

import com.fasterxml.jackson.annotation.JsonValue

enum class StatusCode(@get:JsonValue val code: String) {
    INIT("init"),

    SUCCESS(""),



}