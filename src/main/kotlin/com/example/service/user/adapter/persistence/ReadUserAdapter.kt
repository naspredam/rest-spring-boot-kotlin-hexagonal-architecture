package com.example.service.user.adapter.persistence

import arrow.core.toOption
import com.example.service.user.application.port.persistence.ReadUserPort
import com.example.service.user.domain.User
import com.example.service.user.domain.UserFunctions.userFirstName
import com.example.service.user.domain.UserFunctions.userLastName
import com.example.service.user.domain.UserId
import com.example.service.user.infrastructure.annotations.Adapter
import com.example.service.user.infrastructure.repository.UserRepository

@Adapter
class ReadUserAdapter(private val userRepository: UserRepository,
                      private val userJpaMapper: UserJpaMapper): ReadUserPort {

    override fun existsUserByName(user: User): Boolean {
        val firstName: String = userFirstName(user)
        val lastName: String = userLastName(user)
        return userRepository.findByFirstNameAndLastName(firstName, lastName).isNotEmpty()
    }

    override fun existsUserById(userId: UserId): Boolean {
        val userIdAsInt: Int = userId.intValue
        return userRepository.existsById(userIdAsInt)
    }

    override fun fetchById(userId: UserId) =
            userRepository.findById(userId.intValue)
                .map { userJpaMapper.toDomain(it) }
                .orElse(null)
                .toOption()

    override fun fetchAll() = userRepository.findAll()
                .map { userJpaMapper.toDomain(it) }
}