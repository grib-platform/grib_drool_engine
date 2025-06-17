package kr.co.grib.drools.utils

import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import kotlin.reflect.full.memberProperties

//logger  관련
inline fun <reified T> T.getLogger(): Logger = LoggerFactory.getLogger(T::class.java)

object Utiles {

    //<editor-fold desc="variable to Map">
    fun getVariableToMap(data: Any): Map<String,Any?>{
        return data::class.memberProperties.associate {
            prop -> prop.name to prop.getter.call(data)
        }
    }
    //</editor-fold desc="variable to Map">

    //<editor-fold desc="Drools 템플릿 유효성 검사">
    fun isValidDroolsTemplate(template: String): Boolean {
        return template.isNotEmpty() && template.isNotBlank()
    }

    //</editor-fold desc="Drools 템플릿 유효성 검사">





}