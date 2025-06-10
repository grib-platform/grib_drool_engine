package kr.co.grib.drools.api.base.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.grib.drools.define.StatusCode

open class BaseCtlDto (
    @Schema(description = "API 성공 여부 (true:  오류 없음, false: 오류 발생")
    var success: Boolean = false,
    @Schema(description = "오류 발생 시 오류 코드")
    var code: StatusCode = StatusCode.INIT,
    @Schema(description = "상황에 대한 추가 메세지 (성공/실패와 상관 없으며, 각 컨트롤러에 추가 안내)")
    var message: String = ""
)