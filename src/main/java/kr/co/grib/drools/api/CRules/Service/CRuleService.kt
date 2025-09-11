package kr.co.grib.drools.api.CRules.Service

import kr.co.grib.drools.api.CRules.dto.*

interface CRuleService {
    //<editor-fold desc="[GET] /select  전체 목록 조회 ">
    fun doGetCRuleSelect(): CRuleListResponseCtlDto
    //</editor-fold desc="[GET] /select 전체 목록 조회">

    //<editor-fold desc="[POST] /selectRulelist  cash rule list 조회">
    fun doPostCRuleSelectList(req: CRuleListRequestDto):  CRuleListResponseCtlDto
    //</editor-fold desc="[POST] /selectRulelist cash rule list 조회">

    //<editor-fold desc="[POST] /execute cash rule 실행 ">
    fun doPostCRuleExecute(req: CRuleDataRequest): CRuleResponseCtlDto
    //</editor-fold desc="[POST] /execute cash rule 실행 ">

    //<editor-fold desc="[POST] /create cash rule 생성">
    fun doPostCRuleCreate(req: CRuleCreateRequest): CRuleResponseCtlDto
    //</editor-fold desc="[POST] /create cash rule 생성">

    //<editor-fold desc="[DELETE] /remove cash rule 삭제">
    fun doDeleteCRule(ruleId: Int): CRuleResponseCtlDto
    //</editor-fold desc="[DELETE] /remove cash rule 삭제">



}