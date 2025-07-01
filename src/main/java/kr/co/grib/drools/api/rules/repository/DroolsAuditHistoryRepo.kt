package kr.co.grib.drools.api.rules.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import kr.co.grib.drools.api.rules.Entity.DroolsAuditHistory
import kr.co.grib.drools.api.rules.Entity.QDroolsAuditHistory
import kr.co.grib.drools.api.rules.dto.RuleAuditHistoryDto
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class DroolsAuditHistoryRepo (
    private val queryFactory: JPAQueryFactory,
    private val em: EntityManager
) {
    private val droolsAuditHistory = QDroolsAuditHistory.droolsAuditHistory

    //<editor-fold desc="History 등록">
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveDroolsAuditHistory(
        req: RuleAuditHistoryDto
    ){
        val ruleHistory = DroolsAuditHistory(
            ruleName = req.ruleName,
            ruleAuditIp = req.ruleAditIp,
            ruleApiMethod = req.ruleApiMethod,
            ruleApi = req.ruleApi,
            ruleFact = req.ruleFact,
            ruleResult = req.ruleResult,
            status = req.ruleAditStatus,
            message = req.ruleMsg,
            createdBy = req.userName,
            createdAt = LocalDateTime.now()
        )
        em.persist(ruleHistory)
        em.flush()
    }
    //</editor-fold desc="History 등록">

}