package kr.co.grib.drools.api.rules.service

import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto

interface RuleService {
    fun doGetRules(req: RuleRequestDto):BaseCtlDto
}