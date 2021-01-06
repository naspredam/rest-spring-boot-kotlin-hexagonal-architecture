package com.example.service.user.adapter.entrypoint.api

import com.example.service.user.adapter.entrypoint.api.model.SaveUserBodyDto
import com.example.service.user.adapter.entrypoint.api.model.UserDto
import com.example.service.user.domain.User
import com.example.service.user.domain.UserFullName
import com.example.service.user.domain.UserFunctions.userFirstName
import com.example.service.user.domain.UserFunctions.userLastName
import com.example.service.user.domain.UserFunctions.userPhoneNumber
import com.example.service.user.domain.UserId
import com.example.service.user.domain.UserPhone
import com.example.service.user.infrastructure.annotations.Mapper

@Mapper
class UserDtoMapper {

    fun toDto(user: User): UserDto {
        return UserDto.Builder()
                .firstName(userFirstName(user))
                .lastName(userLastName(user))
                .phone(userPhoneNumber(user))
                .build()
    }

    fun toDomainFromSaveBody(saveUserBodyDto: SaveUserBodyDto): User {
        return User.Builder()
                .fullName(UserFullName(saveUserBodyDto.firstName, saveUserBodyDto.lastName))
                .phone(UserPhone.from(saveUserBodyDto.phone))
                .build()
    }

    fun toDomainFromSaveBody(userId: Int, saveUserBodyDto: SaveUserBodyDto): User {
        return User.Builder()
                .id(UserId(userId))
                .fullName(UserFullName(saveUserBodyDto.firstName, saveUserBodyDto.lastName))
                .phone(UserPhone.from(saveUserBodyDto.phone))
                .build()
    }


}