package kr.co.grib.drools.api.HRules.Controller

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.grib.drools.api.HRules.dto.HRuleDataRequest
import kr.co.grib.drools.api.HRules.dto.HRuleResponseCtlDto
import kr.co.grib.drools.api.HRules.service.HRuleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
@Tag(name="Without Drools engine API" , description = "service 에서 Drools engine 사용하지 않는 api를 기술 합니다.")
@RequestMapping("/api/v1/hRule")
class HRuleController (
    private val hRuleService: HRuleService
) {

    //<editor-fold desc="[POST] /execute rule 파일 실행">
    @Operation(summary = "조건 실행" , description = "하드 코딩된 Rule 실행 ")
    @PostMapping("/execute")
    fun doPostHRuleExecute(
        @Parameter(required = true, description = "Ask rule 객체")
        @RequestBody req: HRuleDataRequest
    ): ResponseEntity<HRuleResponseCtlDto> =
        ResponseEntity(
            hRuleService.doPostHRuleExecute(req),
            HttpStatus.OK
        )
    //</editor-fold desc="[POST] /execute rule 파일 실행">




}