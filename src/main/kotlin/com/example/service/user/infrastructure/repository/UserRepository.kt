package com.example.service.user.infrastructure.repository

import com.example.service.user.infrastructure.repository.data.UserData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserData, Int> {

    fun findByFirstNameAndLastName(firstName: String, lastName: String): Collection<UserData>

}