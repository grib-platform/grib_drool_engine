package kr.co.grib.drools.api.rules.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.co.grib.drools.api.rules.Entity.Drools
import kr.co.grib.drools.api.rules.Entity.QDrools
import kr.co.grib.drools.api.rules.dto.RuleAddResponseDto
import kr.co.grib.drools.api.rules.dto.RuleInsertReqDto
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class DroolsRepo(
    private val queryFactory: JPAQueryFactory,
    private val em: EntityManager
) {
    private val drools = QDrools.drools

    //<editor-fold desc="select  rules">
    fun selectRulesText(
        ruleGroup: String
    ): RuleAddResponseDto?{
        return  queryFactory
            .select(
                Projections.constructor(
                    RuleAddResponseDto::class.java,
                    drools.id,
                    drools.ruleGroup,
                    drools.ruleText
                )
            )
            .from(drools)
            .where(
                drools.ruleGroup.eq(ruleGroup)
            ).fetchOne()
    }
    //</editor-fold desc="select  rules">


    //<editor-fold desc="select 등록 rule 확인, UNIQUE(RULE_GROUP, RULE_NM), RULE_GROUP은 중복 되면 안됨 ">
    fun countDrools(
        ruleGroup: String
    ): Int {
        val count = queryFactory
            .select(drools.count())
            .from(drools)
            .where(
                 drools.ruleGroup.eq(ruleGroup)
            ).fetchOne()
        val result: Int = count?.toInt() ?:0
        return result
    }
    //</editor-fold desc="select 등록 rule 확인, UNIQUE(RULE_GROUP, RULE_NM), RULE_GROUP은 중복 되면 안됨">



    //<editor-fold desc="insert 룰 등록">
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveRule(
        req: RuleInsertReqDto
    ): Int {
        val rules = Drools(
             ruleGroup = req.ruleGroup,
             ruleText = req.ruleText,
             enable = req.enabled.code,
             createdAt = LocalDateTime.now(),
             createdBy = req.createdBy
        )
        em.persist(rules)
        em.flush()

        val result: Int = rules.id?.toInt() ?:0
        return result
    }
    //</editor-fold desc="insert 룰 등록">

    //<editor-fold desc="update 룰 수정">
    @Transactional
    fun updateRule(
        ruleGroup: String,
        ruleText: String
    ): Long {
        return queryFactory
            .update(drools)
            .set(drools.ruleText, ruleText)
            .where(drools.ruleGroup.eq(ruleGroup))
            .execute();
    }
    //</editor-fold desc="update 룰 수정">

    //<editor-fold desc="Delete 룰 삭제">
    @Transactional
    fun deleteRule(
        ruleId: Long
    ): Long {
        return queryFactory
            .delete(drools)
            .where(drools.id.eq(ruleId))
            .execute()
    }
    //</editor-fold desc="Delete 룰 삭제">
}