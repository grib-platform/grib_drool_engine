package kr.co.grib.drools.api.HRules.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.grib.drools.api.base.dto.HBaseCtlDto

class HRuleResponseCtlDto (
    @Schema(description = "deviceId 값")
    var response: List<RuleResult> ?= null
): HBaseCtlDto()