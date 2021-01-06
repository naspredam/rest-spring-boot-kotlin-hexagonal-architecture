package com.example.service.user.adapter.persistence.model

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "users")
data class UserData(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int? = null,
                    val firstName: String? = null,
                    val lastName: String? = null,
                    val phone: String? = null,
                    val createdAt: @NotNull LocalDateTime? = null,
                    val updatedAt: @NotNull LocalDateTime? = null) {

    data class Builder(private var id: Int? = null,
                       private var firstName: String? = null,
                       private var lastName: String? = null,
                       private var phone: String? = null,
                       private var createdAt: LocalDateTime? = null,
                       private var updatedAt: LocalDateTime? = null) {

        fun id(id: Int?) = apply { this.id = id }
        fun firstName(firstName: String) = apply { this.firstName = firstName }
        fun lastName(lastName: String) = apply { this.lastName = lastName }
        fun phone(phone: String) = apply { this.phone = phone }
        fun createdAt(createdAt: LocalDateTime) = apply { this.createdAt = createdAt }
        fun updatedAt(updatedAt: LocalDateTime) = apply { this.updatedAt = updatedAt }
        fun build() = UserData(id, firstName, lastName, phone, createdAt, updatedAt)
    }

}
