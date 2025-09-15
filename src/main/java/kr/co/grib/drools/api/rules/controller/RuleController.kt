package kr.co.grib.drools.api.rules.controller

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.grib.drools.api.base.dto.BaseCtlDto
import kr.co.grib.drools.api.rules.dto.RuleAddRequestDto
import kr.co.grib.drools.api.rules.dto.RuleCreateRequestDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.dto.RuleResponseCtlDto
import kr.co.grib.drools.api.rules.service.RuleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
@Tag(name="Service to Drool Engine API" , description = "service 에서 Drools engine 처리 api를 기술 합니다. ")
@RequestMapping("/api/v1/rule")
class RuleController (
    private val ruleService: RuleService
){

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

    //<editor-fold desc="[PATCH] /addRule rule 추가 및 수정">
    @Operation(summary = "RULE 추가 & 수정" , description = "Template 기반으로 rule을 추가 합니다.")
    @PatchMapping("/addRule")
    fun doPatchAddRule(
        @Parameter(required = true, description = "rule 객체")
        @RequestBody req: RuleAddRequestDto
    ): ResponseEntity<BaseCtlDto> =
         ResponseEntity(
            ruleService.doPatchAddRule(req),
            HttpStatus.OK
        )
    //</editor-fold desc="[PATCH] /addRule rule 추가 및 수정">

    //<editor-fold desc="[DELETE] /deleteRule rule 삭제">
    @Operation(summary = "RULE 삭제" , description = "Rule을 삭제 합니다.")
    @DeleteMapping("/deleteRule")
    fun doDeleteRule(
        @Parameter(required = true, description = "rule group")
        @RequestParam ruleGroup: String,
        @Parameter(required = true, description = "rule name")
        @RequestParam ruleName: List<String>,
        ): ResponseEntity<BaseCtlDto> =
        ResponseEntity(
            ruleService.doDeleteRule(ruleGroup, ruleName),
            HttpStatus.OK
        )
    //</editor-fold desc="[DELETE] /deleteRule rule 삭제">

    //<editor-fold desc="[DELETE] /remove rule 자체 삭제">
    @Operation(summary = "RULE 자체 삭제" , description = "Rule 자체를 삭제 합니다.")
    @DeleteMapping("/remove")
    fun doRemoveRule(
        @Parameter(required = true, description = "rule Group")
        @RequestParam ruleGroup: String
    ): ResponseEntity<BaseCtlDto> =
        ResponseEntity(
            ruleService.doRemoveRule(ruleGroup),
            HttpStatus.OK
        )
    //</editor-fold desc="[DELETE] /remove rule 자체 삭제">

    //<editor-fold desc="[POST] /execute rule 파일 실행">
    @Operation(summary = "Rule 실행" , description = "[DB] 저장된 rule 실행")
    @PostMapping("/execute")
    fun doPostExecute(
        @Parameter(required = true, description = "Ask rule 객체")
        @RequestBody req: RuleRequestDto
    ): ResponseEntity<RuleResponseCtlDto> =
        ResponseEntity(
            ruleService.doPostExecute(req),
            HttpStatus.OK
        )
    //</editor-fold desc="[POST] /execute rule 파일 실행">

}