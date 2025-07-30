package kr.co.grib.drools.api.CRules.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.co.grib.drools.api.CRules.Entity.QIotRules
import kr.co.grib.drools.api.CRules.dto.RuleResponseDto
import org.springframework.stereotype.Repository

@Repository
class IotRulesRepo (
    private val queryFactory: JPAQueryFactory,
    private val em: EntityManager
){
    //Q class
    private val iotRules = QIotRules.iotRules

    //<editor-fold desc="select  rules">
    fun selectRuleText()
    : List<RuleResponseDto> ?{
        return queryFactory
            .select(
                Projections.constructor(
                    RuleResponseDto::class.java,
                    iotRules.ruleGroup,
                    iotRules.conditions,
                    iotRules.actions,
                    iotRules.active,
                    iotRules.priority
                )
            )
            .from(iotRules)
            .fetch()
    }
    //</editor-fold desc="select  rules">

    //<editor-fold desc="select groupRules">
    fun selectRulesInfo(
        ruleGroup: String
    ) :List<RuleResponseDto> ? {

        return queryFactory
            .select(
                Projections.constructor(
                    RuleResponseDto::class.java,
                    iotRules.ruleGroup,
                    iotRules.conditions,
                    iotRules.actions,
                    iotRules.active,
                    iotRules.priority
                )
            )
            .from(iotRules)
            .where(
                iotRules.ruleGroup.eq(ruleGroup),
                iotRules.active.eq(true)
            )
            .fetch()
    }

    //</editor-fold desc="select groupRules">


}