package kr.co.grib.drools.api.CRules.Entity

import jakarta.persistence.*
import org.checkerframework.checker.units.qual.C
import java.time.LocalDateTime

@Entity
@Table(name = "tb_iot_rule")
class IotRules (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    var id: Long? = 0L,

    @Column(name = "ruleGroup", nullable = false)
    var ruleGroup: String ="",

    @Column(name = "conditions",  nullable = false)
    var conditions: String = "",

    @Column(name = "actions", nullable = false)
    var actions: String = "",

    @Column(name = "priority", nullable = false)
    var priority: Int = 0,

    @Column(name = "active", nullable = false)
    var active: Boolean = true,

    @Column(name = "created_by", nullable = false)
    var createdBy: String = "",

    @Column(name = "updated_by", nullable = true)
    var updatedBy: String = "",

    @Column(name="created_at", nullable = false)
    var createdAt:LocalDateTime = LocalDateTime.now(),

    @Column(name="updated_at", nullable = false)
    var updatedAt:LocalDateTime = LocalDateTime.now()
)