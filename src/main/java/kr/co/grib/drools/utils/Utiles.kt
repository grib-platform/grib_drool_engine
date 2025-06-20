package kr.co.grib.drools.utils

import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.templateManager.dto.RuleTemplateDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    //<editor-fold desc="Thymeleaf placeholder : single">
    fun convertToTemplateVariable(
        request: RuleRequestDto
    ): Map<String,Any?> {
        val result = mutableMapOf<String,Any?>()
        //groupId 고정으로 넣기
        result["groupId"] = request.groupId

        request.facts.forEachIndexed{
            index, fact ->
            val idx = index + 1
            result["deviceId$idx"] = fact.deviceId
            result["functionName$idx"] = fact.functionName
            result["functionValue$idx"] = fact.functionValue
        }
        return result
    }
    //<editor-fold desc="Thymeleaf placeholder : single">

    //<editor-fold desc="Thymeleaf placeholder : every rules">
    fun convertToTemplateEveryVariable(
        request: RuleTemplateDto
    ): Map<String,Any?> {
        val result = mutableMapOf<String,Any?>()
        //groupId 고정으로 넣기
        result["groupId"] = request.ruleGroup
        result["singleRules"] = request.singleRules
        result["andRules"] = request.andRules
        result["orRules"] = request.orRules
        return result
    }
    //<editor-fold desc="Thymeleaf placeholder : every rules">




}