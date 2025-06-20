package kr.co.grib.drools.api.rules.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.grib.drools.define.RuleEnableType
import kr.co.grib.drools.define.RuleOperationType

data class RuleInsertReqDto (
    @Schema(description = "룰 ID")
    val ruleId: Int = 0,

    @Schema(description = "룰 그룹 ID")
    val ruleGroup: String ="",

    @Schema(description = "전체 룰 세트 명")
    val ruleSetName: String ="",

    @Schema(description = "룰 Text")
    val ruleText: String ="",

    @Schema(description = "룰 활성 여부")
    val enabled: RuleEnableType = RuleEnableType.Y,

    @Schema(description = "룰 변경 타입")
    val ruleActionType: RuleOperationType = RuleOperationType.UPDATE,

    @Schema(description = "룰 등록자")
    val createdBy: String =""
)