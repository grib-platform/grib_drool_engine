package kr.co.grib.drools.api.CRules.Service

import kr.co.grib.drools.api.CRules.dto.CRuleCreateRequest
import kr.co.grib.drools.api.CRules.dto.CRuleDataRequest
import kr.co.grib.drools.api.CRules.dto.CRuleResponseCtlDto

interface CRuleService {
    //<editor-fold desc="[POST] /execute cash rule 실행 ">
    fun doPostCRuleExecute(req: CRuleDataRequest): CRuleResponseCtlDto
    //</editor-fold desc="[POST] /execute cash rule 실행 ">

    //<editor-fold desc="[POST] /create cash rule 생성">
    fun doPostCRuleCreate(req: CRuleCreateRequest): CRuleResponseCtlDto
    //</editor-fold desc="[POST] /create cash rule 생성">
}