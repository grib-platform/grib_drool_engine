package kr.co.grib.drools.api.rules.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.grib.drools.api.base.dto.BaseCtlDto

class RuleResponseCtlDto (
    @Schema(description = "객체 참조")
    var body: RuleResponseDto? = null
):BaseCtlDto()