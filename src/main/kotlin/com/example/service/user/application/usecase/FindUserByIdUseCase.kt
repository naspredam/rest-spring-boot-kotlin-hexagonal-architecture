package com.example.service.user.application.usecase

import com.example.service.user.domain.User
import com.example.service.user.domain.UserId

interface FindUserByIdUseCase {

    fun findById(userId: UserId): User
}