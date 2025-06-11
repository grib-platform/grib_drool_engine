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
    @Value("\${drools.rule-path}") private val rulePath: String,
){

    //<editor-fold desc="Drools 규칙을 실행 하게 해주는 핵심 객체 Bean 등록 , 실제로 룰을 실행 할 세션">
    @Bean
    fun kieSession(kieContainer: KieContainer): KieSession {
        val kieSession = kieContainer.newKieSession()
        return kieSession

    }
    //</editor-fold desc="Drools 규칙을 실행 하게 해주는 핵심 객체 Bean 등록 , 실제로 룰을 실행 할 세션">

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


    //<editor-fold desc="Drools 규칙을 담은 컨테이너, 규칙 세션을 만들 수 있다. 룰 빌드 ">
    @Bean
    fun kieContainer(
        kieServices: KieServices,
        kieFileSystem: KieFileSystem
    ): KieContainer {
        val kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll()
        val error = kieBuilder.results.messages.filter {
            it.level == Message.Level.ERROR
        }
        if (error.isNotEmpty()){
            throw IllegalStateException("Drools rule error. $error")
        }
        val releaseId = kieServices.repository.defaultReleaseId
        return kieServices.newKieContainer(releaseId)
    }
    //</editor-fold desc="Drools 규칙을 담은 컨테이너, 규칙 세션을 만들 수 있다. 룰 빌드 ">
}