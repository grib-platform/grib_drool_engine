package kr.co.grib.drools.api.rules.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.co.grib.drools.Entity.Drools
import org.springframework.transaction.annotation.Transactional
import kr.co.grib.drools.Entity.QDrools
import kr.co.grib.drools.api.rules.dto.RuleInsertReqDto
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import java.time.LocalDateTime

@Repository
class DroolsRepo(
    private val queryFactory: JPAQueryFactory,
    private val em: EntityManager
) {
    private val drools = QDrools.drools


    //<editor-fold desc="select 등록 rule 확인, UNIQUE(RULE_GROUP, RULE_NM), RULE_GROUP은 중복 되면 안됨 ">
    fun countDrools(
        ruleGroup: String,
        ruleName: String
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
    //</editor-fold desc="select 등록 rule 확인, UNIQUE(RULE_GROUP, RULE_NM)">



    //<editor-fold desc="insert 룰 등록">
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveRule(
        req: RuleInsertReqDto
    ): Int {
        val rules = Drools(
             ruleGroup = req.ruleGroup,
             ruleNm = req.ruleSetName,
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



}