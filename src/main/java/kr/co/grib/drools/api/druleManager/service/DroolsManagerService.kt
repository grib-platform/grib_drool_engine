package kr.co.grib.drools.api.druleManager.service

import org.kie.api.runtime.KieSession

interface DroolsManagerService {

    //<editor-fold desc="Drool Create">
    //<editor-fold desc="template text to Drools로 실행">
    fun initTemplateToDrools(str: String): KieSession?
    //</editor-fold desc="Thymeleaf text to Drools로 실행">
    //</editor-fold desc="Drool Create">

    //<editor-fold desc="Drool Read">



    //</editor-fold desc="Drool Read">



    // update


    //delete


}