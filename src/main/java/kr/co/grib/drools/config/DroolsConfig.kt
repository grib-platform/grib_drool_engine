package kr.co.grib.drools.config

import org.kie.api.KieServices
import org.kie.api.runtime.KieContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DroolsConfig {

    @Bean
    fun kieContainer(): KieContainer{
        val kieService = KieServices.Factory.get()
        return kieService.newKieClasspathContainer()
    }
}