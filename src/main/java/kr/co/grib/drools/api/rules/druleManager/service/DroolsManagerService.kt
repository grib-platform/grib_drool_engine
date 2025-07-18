package kr.co.grib.drools.api.rules.druleManager.service

import org.kie.api.runtime.KieSession

interface DroolsManagerService {

    //<editor-fold desc="template text to Drools로 실행">
    fun initTemplateToDrools(str: String): KieSession?
    //</editor-fold desc="Thymeleaf text to Drools로 실행">

    //<editor-fold desc="template text 에서 특정 Rule 가져와서 특정  rule만 실행 하기">
    fun getTemplateRules(ruleName: String, ruleString: String): KieSession?
    //</editor-fold desc="template text 에서 특정 Rule 가져와서 특정  rule만 실행 하기">

    //<editor-fold desc="Drool rule 생성">

    //</editor-fold desc="Drool rule 생성">

    //<editor-fold desc="Drool rule 추가">
    //</editor-fold desc="Drool rule 추가">


    //<editor-fold desc="Drool rule 삭제">
    //</editor-fold desc="Drool rule 삭제">

}