package kr.co.grib.drools.Entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name= "tb_package")
class Package (
    @Id @Column(name = "package_id")
    private var packageId: String? = null,

    @Column(name = "model_id")
    private var modelId: String? = null,

    @Column(name = "package_version")
    private var packageVersion: String? = null,

    @Column(name = "content_type")
    private var contentType: String? = null,

    @Column(name = "object_key")
    private var objectKey: String? = null,

    @Column(name = "file_url")
    private var fileUrl: String? = null,

    @Column(name = "checksum")
    private var checksum: String? = null,

    @Column(name = "package_size")
    private var packageSize: String? = null,

    @Column(name = "message")
    private var message: String? = null,

    @Column(name = "package_status")
    private var packageStatus: String = "active",

    @Column(name = "created_at")
    private var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    private var updatedAt: LocalDateTime? = null,

)