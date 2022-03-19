package com.example.service.user.adapter.persistence

import com.example.service.user.application.port.persistence.WriteUserPort
import com.example.service.user.domain.User
import com.example.service.user.domain.UserFunctions.userIdAsInt
import com.example.service.user.domain.UserId
import com.example.service.user.infrastructure.annotations.Adapter
import com.example.service.user.infrastructure.repository.UserRepository

@Adapter
class WriteUserAdapter(private val userRepository: UserRepository,
                       private val userJpaMapper: UserJpaMapper): WriteUserPort {

    override fun saveNew(user: User): User {
        val userData = userJpaMapper.toJpaEntity(user)
        val userSaved = userRepository.save(userData)
        return userJpaMapper.toDomain(userSaved)
    }

    override fun update(user: User): User? {
        val userId: Int = userIdAsInt(user)!!
        return userRepository.findById(userId)
                .map { userJpaMapper.toJpaEntity(user, it) }
                .map { userRepository.save(it) }
                .map { userJpaMapper.toDomain(it) }
                .orElse(null)
    }

    override fun deleteById(userId: UserId) = userRepository.deleteById(userId.intValue)
}