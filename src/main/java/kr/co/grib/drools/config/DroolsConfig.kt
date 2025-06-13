package kr.co.grib.drools.config

import org.kie.api.KieServices
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DroolsConfig (){

    //<editor-fold desc="Drools 전반적인 서비스 팩토리, 다른 객체 생성의 진입점">
    @Bean
    fun kieService(): KieServices = KieServices.Factory.get()
    //</editor-fold desc="Drools 전반적인 서비스 팩토리, 다른 객체 생성의 진입점">

}