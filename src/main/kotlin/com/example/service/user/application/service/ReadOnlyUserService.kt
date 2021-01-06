package com.example.service.user.application.service

import com.example.service.user.application.port.persistence.ReadUserPort
import com.example.service.user.application.usecase.FindAllUsersUseCase
import com.example.service.user.application.usecase.FindUserByIdUseCase
import com.example.service.user.domain.UserId
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ReadOnlyUserService(private val readUserPort: ReadUserPort) : FindUserByIdUseCase, FindAllUsersUseCase {

    override fun fetchAllPersisted() = readUserPort.fetchAll()

    override fun findById(userId: UserId) = readUserPort.fetchById(userId)?: throw EntityNotFoundException()
}