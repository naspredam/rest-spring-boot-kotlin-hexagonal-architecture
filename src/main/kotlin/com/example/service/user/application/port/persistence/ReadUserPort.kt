package com.example.service.user.application.port.persistence

import com.example.service.user.domain.User
import com.example.service.user.domain.UserId

interface ReadUserPort {

    fun existsUserByName(user: User): Boolean

    fun existsUserById(userId: UserId): Boolean

    fun fetchById(userId: UserId): User?

    fun fetchAll(): List<User>

}