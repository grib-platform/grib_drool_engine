package kr.co.grib.drools.api.CRules.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.grib.drools.api.base.dto.CBaseCtlDto

class CRuleListResponseCtlDto (
    @Schema(description = "pageNumber")
    var pageNumber: Int ?= null,
    @Schema(description = "pageSize")
    var pageSize: Int ?= null,
    @Schema(description = "totalCount")
    var totalCount: Int ?= null,
    @Schema(description = "totalPages")
    var totalPages: Int ?= null,

    @Schema(description = "data")
    var data: List<CRuleListResponseDto> ?= null
): CBaseCtlDto()