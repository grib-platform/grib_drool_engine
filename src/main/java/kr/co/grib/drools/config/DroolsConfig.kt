package kr.co.grib.drools.config

import org.kie.api.KieServices
import org.kie.api.builder.KieFileSystem
import org.kie.api.builder.Message
import org.kie.api.runtime.KieContainer
import org.kie.api.runtime.KieSession
import org.kie.internal.io.ResourceFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DroolsConfig (
    @Value("\${drools.rule-path}") private val rulePath: String
){

    //<editor-fold desc="Drools 규칙을 실행 하게 해주는 핵심 객체 Bean 등록">
    @Bean
    fun kieSession(kieContainer: KieContainer): KieSession {
        return kieContainer.newKieSession()
    }
    //</editor-fold desc="Drools 규칙을 실행 하게 해주는 핵심 객체 Bean 등록">

    //<editor-fold desc="Drools 전반적인 서비스 팩토리, 다른 객체 생성의 진입점">
    @Bean
    fun kieService(): KieServices = KieServices.Factory.get()
    //</editor-fold desc="Drools 전반적인 서비스 팩토리, 다른 객체 생성의 진입점">

    //<editor-fold desc="Drools .drl 파일을 Drools 빌드 시스템에 등록 하는 가상의 파일 시스템">
    @Bean
    fun kieFileSystem(kieServices: KieServices): KieFileSystem {
        val kieFileSystem = kieServices.newKieFileSystem()
        val resource = ResourceFactory.newClassPathResource(rulePath, "UTF-8")
        kieFileSystem.write(rulePath, resource)
        return kieFileSystem
    }
    //</editor-fold desc="Drools .drl 파일을 Drools 빌드 시스템에 등록 하는 가상의 파일 시스템">





    //<editor-fold desc="Drools 관련 drlr 파일 셋팅">
    @Bean
    fun kieContainer(): KieContainer{
        val kieService = KieServices.Factory.get()
        val kiefileSystem = kieService.newKieFileSystem()

        val dslr  = ResourceFactory.newClassPathResource("rules/rule.drl", "UTF-8")
        kiefileSystem.write("rules/rule.drl", dslr)

        val kieBuilder = kieService.newKieBuilder(kiefileSystem).buildAll()
        val results = kieBuilder.results
        if(results.hasMessages(Message.Level.ERROR)) {
            throw RuntimeException("Drools Rule Build Error: ${results.messages}")
        }

        return kieService.newKieContainer(kieService.repository.defaultReleaseId)
    }
    //</editor-fold desc="Drools 관련 drlr 파일 셋팅">
}