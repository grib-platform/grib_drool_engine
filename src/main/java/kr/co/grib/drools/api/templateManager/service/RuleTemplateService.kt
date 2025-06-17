package kr.co.grib.drools.api.templateManager.service

interface RuleTemplateService {

    //<editor-fold desc="Thymeleaf text 파일 rendering">
    fun initThymeleafRendering(data: Any): String
    //</editor-fold desc="Thymeleaf text 파일 rendering">

}