package kr.co.grib.drools.api.rules.service

import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleAddRequestDto
import kr.co.grib.drools.api.rules.dto.RuleCreateRequestDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.dto.RuleResponseCtlDto

interface RuleService {

    //<editor-fold desc="[POST] /create Drool 생성">
    fun doPostCreateRule(req: RuleCreateRequestDto):BaseCtlDto
    //</editor-fold desc="[POST] /create Drool 생성">

    //<editor-fold desc="[POST] /addRule rule 추가">
    fun doPostAddRule(req: RuleAddRequestDto):BaseCtlDto
    //</editor-fold desc="[POST] /addRule rule 추가">

//    fun doEvaluateRules(req: RuleRequestDto): RuleResponseCtlDto

    fun doEvaluateRulesT(req: RuleRequestDto): RuleResponseCtlDto

}