package com.example.service.user.application.usecase

import com.example.service.user.domain.User

interface SubmitNewUserUseCase {

    fun saveUser(user: User): User
}