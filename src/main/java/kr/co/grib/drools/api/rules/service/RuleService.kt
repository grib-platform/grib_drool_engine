package kr.co.grib.drools.api.rules.service

import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.dto.RuleResponseCtlDto

interface RuleService {

    //<editor-fold desc="[POST] /create Drool 생성">
    fun doPostCreateRule(req: RuleRequestDto):BaseCtlDto
    //</editor-fold desc="[POST] /create Drool 생성">


    fun doGetRules(req: RuleRequestDto):BaseCtlDto

//    fun doEvaluateRules(req: RuleRequestDto): RuleResponseCtlDto

    fun doEvaluateRulesT(req: RuleRequestDto): RuleResponseCtlDto

}