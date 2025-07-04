package kr.co.grib.drools.api.CRules.Service

import kr.co.grib.drools.api.CRules.dto.CRuleDataRequest
import kr.co.grib.drools.api.HRules.dto.HRuleResponseCtlDto

interface CRuleService {
    fun doPostCRuleExecute(req: CRuleDataRequest): HRuleResponseCtlDto
}