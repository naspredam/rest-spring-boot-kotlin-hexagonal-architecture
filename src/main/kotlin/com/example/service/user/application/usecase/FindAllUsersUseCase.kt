package com.example.service.user.application.usecase

import com.example.service.user.domain.User

interface FindAllUsersUseCase {

    fun fetchAllPersisted(): Collection<User>
}