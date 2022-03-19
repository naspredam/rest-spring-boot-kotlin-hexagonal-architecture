package com.example.service.user.adapter.entrypoint.api

import com.example.service.user.infrastructure.controller.dto.SaveUserBodyDto
import com.example.service.user.infrastructure.controller.dto.UserDto
import com.example.service.user.application.port.entrypoint.api.ChangeUserEndpointPort
import com.example.service.user.application.usecase.SubmitChangeForExistingUserUseCase
import com.example.service.user.application.usecase.SubmitNewUserUseCase
import com.example.service.user.application.usecase.SubmitDeleteUserUseCase
import com.example.service.user.domain.UserId
import org.springframework.stereotype.Component

@Component
class ChangeUserEndpointAdapter(
        private val submitNewUserUseCase: SubmitNewUserUseCase,
        private val submitChangeForExistingUserUseCase: SubmitChangeForExistingUserUseCase,
        private val submitDeleteUserUseCase: SubmitDeleteUserUseCase,
        private val userDtoMapper: UserDtoMapper): ChangeUserEndpointPort {

    override fun saveUser(saveUserBodyDto: SaveUserBodyDto): UserDto {
        val user = userDtoMapper.toDomainFromSaveBody(saveUserBodyDto)
        val savedUser = submitNewUserUseCase.saveUser(user)
        return userDtoMapper.toDto(savedUser)
    }

    override fun updateUser(userIdAsInt: Int, saveUserBodyDto: SaveUserBodyDto): UserDto {
        val user = userDtoMapper.toDomainFromSaveBody(userIdAsInt, saveUserBodyDto)
        val updatedUser = submitChangeForExistingUserUseCase.updateUser(user)
        return userDtoMapper.toDto(updatedUser)
    }

    override fun deleteUser(userIdAsInt: Int) {
        val userId = UserId(userIdAsInt)
        submitDeleteUserUseCase.deleteById(userId)
    }
}