package com.example.service.user.application.service

import com.example.service.user.application.port.persistence.ReadUserPort
import com.example.service.user.application.port.persistence.WriteUserPort
import com.example.service.user.domain.User
import com.example.service.user.utils.fakeUserBuilder
import com.example.service.user.utils.fakeUserId
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.persistence.EntityNotFoundException

@ExtendWith(MockKExtension::class)
internal class SubmitUserChangesServiceTest {

    @InjectMockKs
    private lateinit var submitUserChangesService: SubmitUserChangesService

    @MockK
    private lateinit var readUserPort: ReadUserPort

    @MockK
    private lateinit var writeUserPort: WriteUserPort

    @Test
    fun `should throw exception when user name already exists` () {
        val user: User = fakeUserBuilder().build()

        every { readUserPort.existsUserByName(user) } returns true

        assertThatThrownBy { submitUserChangesService.saveUser(user) }
                .isInstanceOf(IllegalArgumentException::class.java)

        verify(exactly = 0) { writeUserPort.saveNew(any()) }
        verify(exactly = 1) { readUserPort.existsUserByName(any()) }
    }

    @Test
    fun `should process a user creation when user is valid and not a duplicate` () {
        val user: User = fakeUserBuilder().build()
        val savedUser: User = user.copy(id = fakeUserId())

        every { readUserPort.existsUserByName(user) } returns false
        every { writeUserPort.saveNew(user) } returns savedUser

        val saveUser: User = submitUserChangesService.saveUser(user)

        assertThat(saveUser).isEqualTo(savedUser)

        verify(exactly = 1) { writeUserPort.saveNew(any()) }
        verify(exactly = 1) { readUserPort.existsUserByName(any()) }
    }

    @Test
    fun `should return user updated` () {
        val user = fakeUserBuilder().build()
        val userFromPort = fakeUserBuilder().build()

        every { writeUserPort.update(user) } returns userFromPort

        val userFromService = submitUserChangesService.updateUser(user)

        assertThat(userFromService).isEqualTo(userFromPort)
        verify(exactly = 1) { writeUserPort.update(any()) }
    }

    @Test
    fun `should return empty when no user was found` () {
        val user = fakeUserBuilder().build()

        every { writeUserPort.update(user) } returns null

        assertThatThrownBy { submitUserChangesService.updateUser(user) }
                .isInstanceOf(EntityNotFoundException::class.java)

        verify(exactly = 1) { writeUserPort.update(any()) }
    }

    @Test
    fun `should throw exception when user does not exists` () {
        val userId = fakeUserId()

        every { readUserPort.existsUserById(userId) } returns false

        assertThatThrownBy { submitUserChangesService.deleteById(userId) }
                .isInstanceOf(IllegalArgumentException::class.java)

        verify(exactly = 0) { writeUserPort.deleteById(any()) }
        verify(exactly = 1) { readUserPort.existsUserById(any()) }
    }

    @Test
    fun `should apply deletion action when user exists` () {
        val userId = fakeUserId()

        every { readUserPort.existsUserById(userId) } returns true
        every { writeUserPort.deleteById(userId) } returns Unit

        submitUserChangesService.deleteById(userId)

        verify(exactly = 1) { writeUserPort.deleteById(any()) }
        verify(exactly = 1) { readUserPort.existsUserById(any()) }
    }
}