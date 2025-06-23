package kr.co.grib.drools.api.rules.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.co.grib.drools.Entity.DroolsModifyHistory
import kr.co.grib.drools.Entity.QDroolsModifyHistory
import kr.co.grib.drools.api.rules.dto.RuleInsertReqDto
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class DroolsModifyHistoryRepo(
    private val queryFactory: JPAQueryFactory,
    private val em: EntityManager
) {
    private val droolsModifyHistory = QDroolsModifyHistory.droolsModifyHistory

    //<editor-fold desc="HISTORY 등록">
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveDroolsModifyHistory(
        req: RuleInsertReqDto
    ){
        val ruleHistory = DroolsModifyHistory(
            ruleId = req.ruleId.toLong(),
            ruleActionType = req.ruleActionType.name,
            ruleContents = req.ruleText,
            updatedBy = req.createdBy,
            updatedAt = LocalDateTime.now()
        )
        em.persist(ruleHistory)
        em.flush()
    }
    //</editor-fold desc="HISTORY 등록">

    //<editor-fold desc="Delete history 삭제">
    @Transactional
    fun deleteDroolsModifyHistory(
        ruleId: Long
    ): Long {
        return queryFactory
            .delete(droolsModifyHistory)
            .where(droolsModifyHistory.ruleId.eq(ruleId))
            .execute()
    }
    //</editor-fold desc="Delete history 삭제">

}