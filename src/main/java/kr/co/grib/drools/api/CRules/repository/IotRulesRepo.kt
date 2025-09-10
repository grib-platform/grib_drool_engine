package kr.co.grib.drools.api.CRules.repository

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.co.grib.drools.api.CRules.Entity.IotRules
import kr.co.grib.drools.api.CRules.Entity.QIotRules
import kr.co.grib.drools.api.CRules.dto.CRuleCreateRequest
import kr.co.grib.drools.api.CRules.dto.CRuleListRequestDto
import kr.co.grib.drools.api.CRules.dto.RuleListResponseDto
import kr.co.grib.drools.api.CRules.dto.RuleResponseDto
import kr.co.grib.drools.utils.Utiles
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class IotRulesRepo (
    private val queryFactory: JPAQueryFactory,
    private val em: EntityManager
){
    //Q class
    private val iotRules = QIotRules.iotRules

    //<editor-fold desc="select All rules List">
    fun selectRulesList()
    : List<RuleListResponseDto> ?{
        return queryFactory
            .select(
                Projections.constructor(
                    RuleListResponseDto::class.java,
                    iotRules.id,
                    iotRules.ruleGroup,
                    iotRules.conditions,
                    iotRules.actions,
                    iotRules.priority,
                    iotRules.active,
                    iotRules.createdBy,
                    iotRules.createdAt,
                    iotRules.updatedBy,
                    iotRules.updatedAt

                )
            )
            .from(iotRules)
            .fetch()
    }
    //</editor-fold desc="select All rules List">

//    fun selectRuleListPaging(
//        req: CRuleListRequestDto
//    ): List<RuleListResponseDto>{
//        // 정렬
//        val sortOrder = if (req.orderBy.equals("desc",  ignoreCase = true)) Order.DESC else Order.ASC
//        val orderSpecifier = when(req.sortBy) {
//            "created_at" -> OrderSpecifier(sortOrder, iotRules.createdAt)
//            "updated_at" -> OrderSpecifier(sortOrder, iotRules.updatedAt)
//            "priority" -> OrderSpecifier(sortOrder, iotRules.priority)
//            else -> OrderSpecifier(sortOrder, iotRules.createdAt)
//        }
//
//        // 조건 빌더
//
//
//
//
//    }
//



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
    fun selectCRulesInfoByGroupRule(
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

    //<editor-fold desc="count groupRules">
    fun countCRulesInfoByGroupRule(
        ruleGroup: String
    ): Int {
        val count = queryFactory
            .select(iotRules.count())
            .from(iotRules)
            .where(iotRules.ruleGroup.eq(ruleGroup))
            .fetchOne()
        val result: Int = count?.toInt() ?:0
        return result
    }
    //</editor-fold desc="count groupRules">


    //<editor-fold desc="insert rules">
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun insertCRules (
        req: CRuleCreateRequest,
        userName: String
    ) {
        val iotRule = IotRules(
            ruleGroup = req.ruleGroup,
            conditions = Utiles.getObjectToJson(req.conditions),
            actions = Utiles.getObjectToJson(req.actions),
            active = req.active,
            priority = countCRulesInfoByGroupRule(req.ruleGroup) + 1,
            createdAt = LocalDateTime.now(),
            createdBy = userName,
            updatedAt = LocalDateTime.now(),
            updatedBy = userName
        )
        em.persist(iotRule)
        em.flush()
    }
    //</editor-fold desc="insert rules">

}