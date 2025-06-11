package kr.co.grib.drools.api.rules.service

import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.dto.RuleResponseCtlDto

interface RuleService {
    fun doGetRules(req: RuleRequestDto):BaseCtlDto

    fun doEvaluateRules(req: RuleRequestDto): RuleResponseCtlDto

}