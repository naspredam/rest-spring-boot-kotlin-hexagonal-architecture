package com.example.service.user.application.usecase

import com.example.service.user.domain.User

interface SubmitChangeForExistingUserUseCase {

    fun updateUser(user: User): User
}