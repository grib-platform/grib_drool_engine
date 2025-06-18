package kr.co.grib.drools.api.rules.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleCreateRequestDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.dto.RuleResponseCtlDto
import kr.co.grib.drools.api.rules.service.RuleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@Tag(name="Service to Rule Engine API" , description = "service 에서 rule engine 처리 api를 기술 합니다. ")
@RequestMapping("/api/v1/rule")
class RuleController (
    private val ruleService: RuleService
){

    //<editor-fold desc="test : rule 생성 및 evaluation , version thymeleaf template 파일 ">
    @Operation(summary = "Rule 평가" , description = "Rule 생성")
    @PostMapping("/createRules")
    fun doEvaluateRulesTest(
        @Parameter(required = true, description = "Ask rule 객체")
        @RequestBody req: RuleRequestDto
    ): ResponseEntity<RuleResponseCtlDto> =
        ResponseEntity(
            ruleService.doEvaluateRulesT(req),
            HttpStatus.OK
        )

    //<editor-fold desc="test : rule 생성 및 evaluation , version thymeleaf template 파일 ">

    //<editor-fold desc="[POST] /create Drool 생성">
    @Operation(summary = "RULE 생성" , description = "Template 기반으로 rule을 생성 합니다.")
    @PostMapping("/create")
    fun doPostCreateRule(
        @Parameter(required = true, description = "rule 객체")
        @RequestBody req: RuleCreateRequestDto
    ): ResponseEntity<BaseCtlDto> =
        ResponseEntity(
            ruleService.doPostCreateRule(req),
            HttpStatus.OK
        )
    //</editor-fold desc="[POST] /create Drool 생성">





    //<editor-fold desc="rule 조회">


    //</editor-fold desc="rule 조회">


    //<editor-fold desc="rule 수정">

    //</editor-fold desc="rule 수정">

    //<editor-fold desc="rule 삭제">

    //</editor-fold desc="rule 삭제">






}