package com.example.service.user.adapter.persistence

import com.example.service.user.domain.UserFunctions.userIdAsInt
import com.example.service.user.infrastructure.repository.UserRepository
import com.example.service.user.utils.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class WriteUserAdapterTest {

    @InjectMockKs
    private lateinit var writeUserAdapter: WriteUserAdapter

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var userJpaMapper: UserJpaMapper

    @Test
    fun `should save new user` () {
        val newUser = fakeUserBuilder().id(null).build()
        val newUserDataMapped = fakeUserDataBuilder().id(null).build()
        val persistedUserData = fakeUserData()
        val persistedUser = fakeUser()

        every { userJpaMapper.toJpaEntity(newUser) } returns newUserDataMapped
        every { userRepository.save(newUserDataMapped) } returns persistedUserData
        every { userJpaMapper.toDomain(persistedUserData) } returns persistedUser

        val userSaved = writeUserAdapter.saveNew(newUser)

        assertThat(userSaved).isEqualTo(persistedUser)
    }

    @Test
    fun `should try to update user but return empty, when no user found` () {
        val userToUpdate = fakeUser()

        every { userRepository.findById(userIdAsInt(userToUpdate)!!) } returns Optional.empty()

        val update = writeUserAdapter.update(userToUpdate)

        assertThat(update).isNull()

        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should update user, returning the updated value, when user found` () {
        val userToUpdate = fakeUser()
        val persistedUserData = fakeUserData()
        val updatedUserFromDataMapped = fakeUserData()
        val updatedUserFromData = fakeUserData()
        val updatedUser = fakeUser()

        every { userRepository.findById(userIdAsInt(userToUpdate)!!) } returns Optional.of(persistedUserData)
        every { userJpaMapper.toJpaEntity(userToUpdate, persistedUserData) } returns updatedUserFromDataMapped
        every { userRepository.save(updatedUserFromDataMapped) } returns updatedUserFromData
        every { userJpaMapper.toDomain(updatedUserFromData) } returns updatedUser

        val update = writeUserAdapter.update(userToUpdate)

        assertThat(update).isEqualTo(updatedUser)
    }

    @Test
    fun `should apply the delete action on the repository, by id` () {
        val userId = fakeUserId()

        every { userRepository.deleteById(userId.intValue) } returns Unit

        writeUserAdapter.deleteById(userId)

        verify(exactly = 1) { userRepository.deleteById(any()) }
    }

}