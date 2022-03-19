package com.example.service.user.adapter.persistence

import com.example.service.user.adapter.persistence.model.UserData
import com.example.service.user.domain.User
import com.example.service.user.domain.UserFunctions.userFirstName
import com.example.service.user.domain.UserFunctions.userIdAsInt
import com.example.service.user.domain.UserFunctions.userLastName
import com.example.service.user.domain.UserId
import com.example.service.user.utils.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class ReadUserAdapterTest {

    @InjectMockKs
    private lateinit var readUserAdapter: ReadUserAdapter

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var userJpaMapper: UserJpaMapper

    @Test
    fun  `should found user by first and last name` () {
        val user = fakeUser()
        val firstName = userFirstName(user)
        val lastName = userLastName(user)

        every { userRepository.findByFirstNameAndLastName(firstName, lastName) } returns listOf(fakeUserData())

        val userFound = readUserAdapter.existsUserByName(user)

        assertThat(userFound).isTrue
    }

    @Test
    fun `should not found user by first and last name` () {
        val user = fakeUser()
        val firstName = userFirstName(user)
        val lastName = userLastName(user)

        every { userRepository.findByFirstNameAndLastName(firstName, lastName) } returns emptyList()

        val userFoundUnitReactive = readUserAdapter.existsUserByName(user)

        assertThat(userFoundUnitReactive).isFalse
    }

    @Test
    fun `should found user by id` () {
        val user = fakeUser()
        val userId = userIdAsInt(user)!!

        every { userRepository.existsById(userId) } returns true

        val userFoundUnitReactive = readUserAdapter.existsUserById(UserId(userId))

        assertThat(userFoundUnitReactive).isTrue
    }

    @Test
    fun `should not found user by id` () {
        val user: User = fakeUser()
        val userId: Int = userIdAsInt(user)!!

        every { userRepository.existsById(userId) } returns false

        val userFoundUnitReactive = readUserAdapter.existsUserById(UserId(userId))
        assertThat(userFoundUnitReactive).isFalse
    }

    @Test
    fun `should return empty when no user found` () {
        val userId: UserId = fakeUserId()

        every { userRepository.findById(userId.intValue) } returns Optional.empty()

        val userOptional = readUserAdapter.fetchById(userId)
        assertThat(userOptional).isNull()
    }

    @Test
    fun `should return user when found` () {
        val userId = fakeUserId()
        val foundUserData = fakeUserDataBuilder().id(userId.intValue).build()
        val user: User = fakeUserBuilder().id(userId).build()

        every { userRepository.findById(foundUserData.id!!) } returns Optional.of(foundUserData)
        every { userJpaMapper.toDomain(foundUserData) } returns user

        val userOptional = readUserAdapter.fetchById(userId)
        assertThat(userOptional).isEqualTo(user)
    }

    @Test
    fun `should return empty list when no user found` () {
        every { userRepository.findAll() } returns emptyList()

        val userCollection: Collection<User> = readUserAdapter.fetchAll()
        assertThat(userCollection).isEmpty()
    }

    @Test
    fun `should return non empty list of users, when users found on the repository` () {
        val userData1: UserData = fakeUserData()
        val userData2: UserData = fakeUserData()
        val user1: User = fakeUser()
        val user2: User = fakeUser()

        every { userRepository.findAll() } returns listOf(userData1, userData2)
        every { userJpaMapper.toDomain(userData1) } returns user1
        every { userJpaMapper.toDomain(userData2) } returns user2

        val users: List<User> = readUserAdapter.fetchAll()
        assertThat(users).hasSize(2)
        assertThat(users[0]).isEqualTo(user1)
        assertThat(users[1]).isEqualTo(user2)
    }
}