package kr.co.grib.drools.api.templateManager.service

import kr.co.grib.drools.api.rules.dto.RuleCreateRequestDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto

interface RuleTemplateService {

    //<editor-fold desc="Thymeleaf text 파일 rendering">
    fun initThymeleafRendering(data: RuleRequestDto, templateFileName: String): String
    //</editor-fold desc="Thymeleaf text 파일 rendering">

    //<editor-fold desc="Thymeleaf text 파일 rendering">
    fun initThymeleafRenderAllRules(data: RuleCreateRequestDto): String
    //</editor-fold desc="Thymeleaf text 파일 rendering">

}