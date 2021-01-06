package com.example.service.user.application.port.persistence

import com.example.service.user.domain.User
import com.example.service.user.domain.UserId

interface WriteUserPort {

    fun saveNew(user: User): User

    fun update(user: User): User?

    fun deleteById(userId: UserId)
}