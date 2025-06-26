package kr.co.grib.drools.api.rules.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name =  "tb_drools")
class Drools (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    var id: Long? = 0L,

    @Column(name = "rule_group", nullable = false)
    var ruleGroup: String ="",

    @Column(name= "rule_nm" , nullable =  false)
    var ruleNm: String = "",

    @Column(name= "rule_text",  nullable = false)
    var ruleText: String = "",

    @Column(name = "enable" , nullable = false)
    var enable: Boolean = false,

    @Column(name = "created_by" , nullable = false)
    var createdBy: String = "",

    @Column(name = "created_at" , nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)