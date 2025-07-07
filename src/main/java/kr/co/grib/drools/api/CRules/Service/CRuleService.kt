package kr.co.grib.drools.api.CRules.Service

import kr.co.grib.drools.api.CRules.dto.CRuleDataRequest
import kr.co.grib.drools.api.CRules.dto.CRuleResponseCtlDto

interface CRuleService {
    fun doPostCRuleExecute(req: CRuleDataRequest): CRuleResponseCtlDto
}