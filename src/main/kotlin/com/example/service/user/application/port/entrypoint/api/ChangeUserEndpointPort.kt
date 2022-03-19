package com.example.service.user.application.port.entrypoint.api

import com.example.service.user.infrastructure.controller.dto.SaveUserBodyDto
import com.example.service.user.infrastructure.controller.dto.UserDto

interface ChangeUserEndpointPort {

    fun saveUser(saveUserBodyDto: SaveUserBodyDto): UserDto

    fun updateUser(userIdAsInt: Int, saveUserBodyDto: SaveUserBodyDto): UserDto

    fun deleteUser(userIdAsInt: Int)

}