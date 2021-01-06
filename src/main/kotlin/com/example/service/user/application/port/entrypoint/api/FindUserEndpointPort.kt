package com.example.service.user.application.port.entrypoint.api

import com.example.service.user.adapter.entrypoint.api.model.UserDto

interface FindUserEndpointPort {

    fun fetchAllUsers(): Collection<UserDto>

    fun fetchUserById(userIdAsInt: Int): UserDto

}