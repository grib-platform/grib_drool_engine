package kr.co.grib.drools.api.druleManager.service

import kr.co.grib.drools.api.druleManager.dto.ActionResultDto
import kr.co.grib.drools.api.druleManager.dto.RuleFactDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import org.kie.api.runtime.KieSession
import org.thymeleaf.context.Context

interface DroolsManagerService {
    //<editor-fold desc="룰 최초 등록 하거나 새롭게 다시 빌드 하여 교체 할때 사용">
    fun initRule(groupId: String, ruleFilePath: String)
    //</editor-fold desc="룰 최초 등록 하거나 새롭게 다시 빌드 하여 교체 할때 사용">

    //<editor-fold desc="룰 삭제">
    fun deleteRule(groupId: String)
    //</editor-fold desc="룰 삭제">

    //<editor-fold desc="룰 그룹별 분리된 세션 처리, 동적으로 rule 관리 하고자 할때">
    fun initKieSession(groupId: String): KieSession?
    //<editor-fold desc="룰 그룹별 분리된 세션 처리, 동적으로 rule 관리 하고자 할때">

    //<editor-fold desc="template ->  drools ">
    fun initKieTemplateSession(context: Context)
    //</editor-fold desc="template -> drools">


}