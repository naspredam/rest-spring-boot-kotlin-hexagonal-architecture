package com.example.service.user.application.service

import arrow.core.Option
import arrow.core.none
import com.example.service.user.application.port.persistence.ReadUserPort
import com.example.service.user.domain.User
import com.example.service.user.domain.UserId
import com.example.service.user.utils.fakeUser
import com.example.service.user.utils.fakeUserId
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.persistence.EntityNotFoundException

@ExtendWith(MockKExtension::class)
internal class ReadOnlyUserServiceTest {

    @InjectMockKs
    private lateinit var readOnlyUserService: ReadOnlyUserService

    @MockK
    private lateinit var readUserPort: ReadUserPort

    @Test
    fun `should return entity not found, when the id was not found ont the read port` () {
        val userId: UserId = fakeUserId()

        every { readUserPort.fetchById(userId) } returns none()

        Assertions.assertThatThrownBy { readOnlyUserService.findById(userId) }
                .isInstanceOf(EntityNotFoundException::class.java)

        verify(exactly = 1) { readUserPort.fetchById(any()) }
    }

    @Test
    fun `should return user from the id, when found` () {
        val userId = fakeUserId()
        val userFromPort = fakeUser()

        every { readUserPort.fetchById(userId) } returns Option(userFromPort)

        val byId = readOnlyUserService.findById(userId)
        assertThat(byId).isEqualTo(userFromPort)

        verify(exactly = 1) { readUserPort.fetchById(any()) }
    }

    @Test
    fun `should return empty list when not user is returned from the port` () {
        every { readUserPort.fetchAll() } returns emptyList()

        val users = readOnlyUserService.fetchAllPersisted()
        assertThat(users).isEmpty()
    }

    @Test
    fun `should return the users from the port` () {
        val user1: User = fakeUser()
        val user2: User = fakeUser()

        every { readUserPort.fetchAll() } returns listOf(user1, user2)

        val users = readOnlyUserService.fetchAllPersisted()
        assertThat(users).hasSize(2).containsExactlyInAnyOrder(user1, user2)
    }

}