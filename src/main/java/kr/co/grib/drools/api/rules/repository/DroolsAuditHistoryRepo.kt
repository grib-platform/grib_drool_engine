package kr.co.grib.drools.api.rules.repository

import com.querydsl.core.QueryFactory
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.grib.drools.Entity.QDroolsAuditHistory
import org.springframework.stereotype.Repository

@Repository
class DroolsAuditHistoryRepo (
    private val queryFactory: JPAQueryFactory
) {
    private val droolsAuditHistory = QDroolsAuditHistory.droolsAuditHistory

    //<editor-fold desc="History 등록">
    //</editor-fold desc="History 등록">

}