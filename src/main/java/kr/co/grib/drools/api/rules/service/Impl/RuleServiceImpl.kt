package kr.co.grib.drools.api.rules.service.Impl

import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.service.RuleService
import org.springframework.stereotype.Service

@Service
class RuleServiceImpl : RuleService{

    override
    fun doGetRules(req: RuleRequestDto) :BaseCtlDto{
        val rtn = BaseCtlDto();
        return rtn;
    }
}