package kr.co.grib.drools.api.rules.Entity

import jakarta.persistence.*
import org.checkerframework.checker.units.qual.C
import java.time.LocalDateTime

@Entity
@Table(name = "tb_drools_audit_history")
class DroolsAuditHistory (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    var ahId: Long? = 0L,

    @Column(name = "rule_name", nullable = false)
    var ruleName: String = "",

    @Column(name = "rule_audit_ip", nullable = false)
    var ruleAuditIp: String = "",

    @Column(name = "rule_api_method", nullable = false)
    var ruleApiMethod: String = "",

    @Column(name = "rule_api", nullable = false)
    var ruleApi: String = "",

    @Column(name = "rule_fact", nullable = false)
    var ruleFact: String = "",

    @Column(name = "rule_result", nullable = false)
    var ruleResult: String = "",

    @Column(name = "status", nullable = false)
    var status: String ="",

    @Column(name = "message", nullable = false)
    var message: String = "",

    @Column(name = "created_by", nullable = false)
    var createdBy: String = "",

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)