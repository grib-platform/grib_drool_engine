package kr.co.grib.drools.Entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "tb_drools_modify_history")
class DroolsModifyHistory (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    var mhId: Long? = 0L,

    @Column(name = "rule_id", nullable = false)
    var ruleId: Long = 0L,

    @Column(name = "rule_action_type", nullable = false)
    var ruleActionType: String = "",

    @Column(name = "rule_contents", nullable = false)
    var ruleContents: String = "",

    @Column(name = "updated_by" , nullable =  false)
    var updatedBy: String = "",

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime  = LocalDateTime.now()
)