package com.example.service.user.application.usecase

import com.example.service.user.domain.UserId

interface SubmitDeleteUserUseCase {

    fun deleteById(userId: UserId)
}