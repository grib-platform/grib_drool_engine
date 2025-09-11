package kr.co.grib.drools.api.CRules.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

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

    fun selectRuleListPaging(
        req: CRuleListRequestDto
    ): List<RuleListResponseDto> ?{
        // 정렬
        val sortOrder = if (req.orderBy.equals("desc",  ignoreCase = true)) Order.DESC else Order.ASC
        val orderSpecifier = when(req.sortBy) {
            "created_at" -> OrderSpecifier(sortOrder, iotRules.createdAt)
            "updated_at" -> OrderSpecifier(sortOrder, iotRules.updatedAt)
            "priority" -> OrderSpecifier(sortOrder, iotRules.priority)
            else -> OrderSpecifier(sortOrder, iotRules.createdAt)
        }

        // 조건 빌더
        val builder = BooleanBuilder()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

        // 날짜 조건
        if (!req.periodFrom.isNullOrEmpty()) {
            builder.and(
                iotRules.createdAt.goe(LocalDate.parse(req.periodFrom, formatter).atStartOfDay())
            )
        }

        if (!req.periodTo.isNullOrEmpty()) {
            builder.and(
                iotRules.createdAt.loe(LocalDate.parse(req.periodTo, formatter).atTime(23, 59, 59))
            )
        }

        // keyword 조건
        if(!req.keyword.isNullOrEmpty() && !req.keywordColumn.isNullOrEmpty()) {
            when(req.keywordColumn){
                "ruleId" -> builder.and(iotRules.id.eq(req.keyword.toLong()))
                "ruleGroup" -> builder.and(iotRules.ruleGroup.lower().like("%${req.keyword.lowercase()}%"))
                "message" -> builder.and(
                    iotRules.actions.like("%${req.keyword}%")
                ) // action 안의 json값의 message에 대한 like 검색
                "priority" -> builder.and(iotRules.priority.stringValue().eq(req.keyword))
                "active" -> builder.and(iotRules.active.stringValue().eq(req.keyword) )
            }
        }

        // 페이징 (pagenumber 1 부터 시작)
        val offset = ((req.pageNumber -1 ) * req.pageSize ).toLong()
        val limit = req.pageSize.toLong()

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
            .where(builder)
            .orderBy(orderSpecifier)
            .offset(offset)
            .limit(limit)
            .fetch()
    }


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

    //<editor-fold desc="count rules">
    fun countCRulesInfo(): Int{
        val count  = queryFactory
            .select(iotRules.count())
            .from(iotRules)
            .fetchOne()
        val result:  Int = count?.toInt() ?:0
        return result
    }
    //</editor-fold desc="count rules">



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

    //<editor-fold desc="rule 삭제">
    @Transactional
    fun deleteRule(
        ruleId: Long
    ): Long {
        return queryFactory
            .delete(iotRules)
            .where(iotRules.id.eq(ruleId))
            .execute()
    }
    //<editor-fold desc="rule 삭제">


}