package kr.co.grib.drools.api.rules.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.service.RuleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@Tag(name="Service to Rule Engine API" , description = "service 에서 rule engine 처리 api를 기술 합니다. ")
@RequestMapping("/api/v1/rule")
class RuleController (
    private val ruleService: RuleService
){
    @Operation(summary = "RULE 조회" , description = "사용 가능 한 rule 이 있는지 조회 합니다.")
    @GetMapping
    fun doGetRules(
        @Parameter(required = true, description = "Ask rule 객체")
        @RequestBody req: RuleRequestDto
    ): ResponseEntity<BaseCtlDto> =
        ResponseEntity(
            null
        )



}