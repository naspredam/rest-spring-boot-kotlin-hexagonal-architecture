package com.example.service.user.application.service

import com.example.service.user.application.port.persistence.ReadUserPort
import com.example.service.user.application.port.persistence.WriteUserPort
import com.example.service.user.application.usecase.SubmitChangeForExistingUserUseCase
import com.example.service.user.application.usecase.SubmitDeleteUserUseCase
import com.example.service.user.application.usecase.SubmitNewUserUseCase
import com.example.service.user.domain.User
import com.example.service.user.domain.UserId
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class SubmitUserChangesService(
        private val readUserPort: ReadUserPort,
        private val writeUserPort: WriteUserPort
): SubmitNewUserUseCase, SubmitChangeForExistingUserUseCase, SubmitDeleteUserUseCase{

    override fun updateUser(user: User) = writeUserPort.update(user) ?: throw EntityNotFoundException()

    override fun deleteById(userId: UserId) {
        require(readUserPort.existsUserById(userId)) {
            "User missed on the repository, not able to delete it..."
        }
        writeUserPort.deleteById(userId)
    }

    override fun saveUser(user: User): User {
        require(!readUserPort.existsUserByName(user)) { "User duplicated..." }
        return writeUserPort.saveNew(user)
    }
}