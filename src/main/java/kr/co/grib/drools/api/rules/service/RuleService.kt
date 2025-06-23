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

    //<editor-fold desc="[PATCH] /addRule rule 추가">
    fun doPatchAddRule(req: RuleAddRequestDto):BaseCtlDto
    //</editor-fold desc="[PATCH] /addRule rule 추가">

    //<editor-fold desc="[DELETE] /deleteRule rule 삭제">
    fun doDeleteRule(ruleId: Int, ruleName: List<String>):BaseCtlDto
    //</editor-fold desc="[DELETE] /deleteRule rule 삭제">

    //<editor-fold desc="[DELETE] /remove rule 자체 삭제">
    fun doRemoveRule(ruleId: Int):BaseCtlDto
    //</editor-fold desc="[DELETE] /remove rule 자체 삭제">

    //<editor-fold desc="[POST] /execute rule 파일 실행">
    fun doPostExecute(req: RuleRequestDto) : RuleResponseCtlDto
    //</editor-fold desc="[POST] /execute rule 파일 실행">


}