package kr.co.grib.drools.api.CRules.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat

class CRuleListRequestDto (

    @Schema(description = "page start number ", example = "1", defaultValue = "1")
    val pageNumber: Int = 1,
    @Schema(description = "page당 갯수", example = "10", defaultValue = "10")
    val pageSize: Int = 10,

    @Schema(description = "정렬 기준" , example = "created_at", defaultValue = "createdAt")
    val sortBy: String ="createdAt",

    @Schema(description = "정렬 순서 (asc, desc)", defaultValue = "asc")
    val orderBy: String = "asc",

    @Schema(description = "periodFrom , startedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "YYYY.mm.dd")
    val periodFrom: String,

    @Schema(description = "periodFrom , endDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "YYYY.mm.dd")
    val periodTo: String,

    @Schema(description = "keywordColumn, 검색 조건 기준(id, ruleGroup, message, priority, activate)")
    val keywordColumn: String,

    @Schema(description = "keyword, 검색어")
    val keyword:String,

    @Schema(description = "periodColumn , 기간 검색 (오늘(now), 1주일(week), 1개월(month), 전체('')")
    val periodColumn: String
)