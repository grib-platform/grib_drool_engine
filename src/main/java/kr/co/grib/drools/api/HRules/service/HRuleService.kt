package kr.co.grib.drools.api.HRules.service

import kr.co.grib.drools.api.HRules.dto.HRuleDataRequest
import kr.co.grib.drools.api.HRules.dto.HRuleResponseCtlDto

interface HRuleService {
    fun doPostHRuleExecute(req: HRuleDataRequest) : HRuleResponseCtlDto
}