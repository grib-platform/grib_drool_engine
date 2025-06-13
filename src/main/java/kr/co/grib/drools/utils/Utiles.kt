package kr.co.grib.drools.utils

import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
//logger  관련
inline fun <reified T> T.getLogger(): Logger = LoggerFactory.getLogger(T::class.java)

object Utiles {
    //<editor-fold desc="thymeleaf to drl 문자열 생성">
    fun generateDrlFromTemplate(
        text: String ,
        variables: Map<String,Any>,
        templateEngine: TemplateEngine
    ): String {
        val context  = Context().apply{
            setVariables(variables)
        }
        return templateEngine.process(text,context)
    }
    //</editor-fold desc="thymeleaf to drl 문자열 생성">




}