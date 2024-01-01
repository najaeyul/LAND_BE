package land.land_be.common

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
abstract class DateInfo {

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private val createdAt: LocalDateTime? = null

    @Column(name = "updated_at")
    @LastModifiedDate
    private val updatedAt: LocalDateTime? = null
}