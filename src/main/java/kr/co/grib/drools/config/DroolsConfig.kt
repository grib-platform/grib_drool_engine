package kr.co.grib.drools.config

import org.kie.api.KieServices
import org.kie.api.builder.Message
import org.kie.api.runtime.KieContainer
import org.kie.internal.io.ResourceFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DroolsConfig {

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