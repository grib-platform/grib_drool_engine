package kr.co.grib.drools.api.CRules.Controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.grib.drools.api.CRules.Service.CRuleService
import kr.co.grib.drools.api.CRules.dto.CRuleDataRequest
import kr.co.grib.drools.api.CRules.dto.CRuleResponseCtlDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Rule is in the Redis cash  API", description = "redis에 룰을 저장하고 룰을 조회 하는 api를 기술 합니다.")
@RequestMapping("/api/v1/cRule")
class CRuleController (
    private val cRuleService: CRuleService
){
    //<editor-fold desc="[POST] /execute cash rule 실행 ">
    @Operation(summary = "조건 실행" , description = "cash에 저장한  rule 실행")
    @PostMapping("/execute")
    fun doPostCRuleExecute(
        @Parameter(required = true, description = "Ask rule 객체")
        @RequestBody req: CRuleDataRequest
    ): ResponseEntity<CRuleResponseCtlDto> =
        ResponseEntity(
            cRuleService.doPostCRuleExecute(req),
            HttpStatus.OK
        )
    //</editor-fold desc="[POST] /execute rule 파일 실행">

}