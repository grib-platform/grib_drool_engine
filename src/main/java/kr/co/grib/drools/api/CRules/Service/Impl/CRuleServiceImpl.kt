package kr.co.grib.drools.api.CRules.Service.Impl

import kr.co.grib.drools.api.CRules.Service.CRuleService
import kr.co.grib.drools.api.CRules.dto.CRuleDataRequest
import kr.co.grib.drools.api.HRules.dto.HRuleResponseCtlDto
import kr.co.grib.drools.utils.getLogger
import org.springframework.stereotype.Service

@Service
class CRuleServiceImpl : CRuleService {

    private val logger = getLogger()

    override fun doPostCRuleExecute(
        req: CRuleDataRequest
    ): HRuleResponseCtlDto {
        var  rtn = HRuleResponseCtlDto()
        try {
            logger.info("check")


        }catch (e: Exception){
            logger.error("doPostCRuleExecute.error.{}", e)
        }
        return rtn
    }


}