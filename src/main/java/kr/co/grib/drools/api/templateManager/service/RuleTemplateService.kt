package kr.co.grib.drools.api.templateManager.service

import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.templateManager.dto.RuleTemplateDto

interface RuleTemplateService {

    //<editor-fold desc="Thymeleaf text 파일 rendering">
    fun initThymeleafRendering(data: RuleRequestDto, templateFileName: String): String
    //</editor-fold desc="Thymeleaf text 파일 rendering">

    //<editor-fold desc="Thymeleaf text 파일 rendering">
    fun initThymeleafRenderAllRules(data: RuleTemplateDto, header:Boolean): String
    //</editor-fold desc="Thymeleaf text 파일 rendering">

    //<editor-fold desc="Thymeleaf text 파일 rendering">

    //<editor-fold desc="Thymeleaf text 파일 rendering">


}