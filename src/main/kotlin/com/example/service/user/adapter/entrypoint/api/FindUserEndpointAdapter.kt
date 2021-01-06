package com.example.service.user.adapter.entrypoint.api

import com.example.service.user.adapter.entrypoint.api.model.UserDto
import com.example.service.user.application.port.entrypoint.api.FindUserEndpointPort
import com.example.service.user.application.usecase.FindAllUsersUseCase
import com.example.service.user.application.usecase.FindUserByIdUseCase
import com.example.service.user.domain.UserId
import com.example.service.user.infrastructure.annotations.Adapter
import org.springframework.stereotype.Component

@Component
class FindUserEndpointAdapter(private val findAllUsersUseCase: FindAllUsersUseCase,
                              private val findUserByIdUseCase: FindUserByIdUseCase,
                              private val userDtoMapper: UserDtoMapper) : FindUserEndpointPort {

    override fun fetchAllUsers() = findAllUsersUseCase.fetchAllPersisted()
                .map { userDtoMapper.toDto(it) }

    override fun fetchUserById(userIdAsInt: Int): UserDto {
        val userId = UserId(userIdAsInt)
        val foundUser = findUserByIdUseCase.findById(userId)
        return userDtoMapper.toDto(foundUser)
    }
}