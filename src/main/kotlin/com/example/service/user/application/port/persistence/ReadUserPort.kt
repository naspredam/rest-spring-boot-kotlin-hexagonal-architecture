package com.example.service.user.application.port.persistence

import arrow.core.Option
import com.example.service.user.domain.User
import com.example.service.user.domain.UserId

interface ReadUserPort {

    fun existsUserByName(user: User): Boolean

    fun existsUserById(userId: UserId): Boolean

    fun fetchById(userId: UserId): Option<User>

    fun fetchAll(): List<User>

}