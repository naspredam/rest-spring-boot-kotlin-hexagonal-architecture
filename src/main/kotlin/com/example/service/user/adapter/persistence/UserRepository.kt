package com.example.service.user.adapter.persistence

import com.example.service.user.adapter.persistence.model.UserData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserData, Int> {

    fun findByFirstNameAndLastName(firstName: String, lastName: String): Collection<UserData>

}