package kr.co.grib.drools.api.rules.dto

import io.swagger.v3.oas.annotations.media.Schema

data class RuleAuditHistoryDto (

    @Schema(description = "룰 명")
    val ruleName: String = "",

    @Schema(description = "룰 실행 IP")
    val ruleAditIp: String = "",

    @Schema(description = "룰 실행 API 메소드")
    val ruleApiMethod: String = "",

    @Schema(description = "룰 실행 URI")
    val ruleApi: String = "",

    @Schema(description = "룰 입력 Fact")
    val ruleFact: String = "",

    @Schema(description = "를 실행 결과")
    val ruleResult: String = "",

    @Schema(description = "룰 실행 상태")
    val ruleAditStatus: String = "",

    @Schema(description = "룰 실행 메세지")
    val ruleMsg: String = "",

    @Schema(description = "룰 실행 한 사람")
    val userName: String = ""
)