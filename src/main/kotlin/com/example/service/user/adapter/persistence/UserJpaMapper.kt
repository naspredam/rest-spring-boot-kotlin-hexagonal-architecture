package com.example.service.user.adapter.persistence

import com.example.service.user.infrastructure.repository.data.UserData
import com.example.service.user.domain.User
import com.example.service.user.domain.UserFullName
import com.example.service.user.domain.UserFunctions.userFirstName
import com.example.service.user.domain.UserFunctions.userIdAsInt
import com.example.service.user.domain.UserFunctions.userLastName
import com.example.service.user.domain.UserFunctions.userPhoneNumber
import com.example.service.user.domain.UserId
import com.example.service.user.domain.UserPhone
import com.example.service.user.infrastructure.annotations.Mapper
import java.time.LocalDateTime

@Mapper
class UserJpaMapper {

    fun toJpaEntity(user: User) = UserData.Builder()
                .id(userIdAsInt(user))
                .firstName(userFirstName(user))
                .lastName(userLastName(user))
                .phone(userPhoneNumber(user))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()

    fun toJpaEntity(user: User, persistedUserData: UserData) =
            persistedUserData.copy(
                    firstName = userFirstName(user),
                    lastName = userLastName(user),
                    phone = userPhoneNumber(user),
                    updatedAt = LocalDateTime.now()
            )

    fun toDomain(userData: UserData) = User.Builder()
                .id(UserId(userData.id!!))
                .fullName(UserFullName(userData.firstName!!, userData.lastName!!))
                .phone(UserPhone.from(userData.phone!!))
                .build()
}