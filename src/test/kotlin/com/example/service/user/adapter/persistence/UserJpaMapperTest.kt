package com.example.service.user.adapter.persistence

import com.example.service.user.infrastructure.repository.data.UserData
import com.example.service.user.domain.User
import com.example.service.user.domain.UserFunctions.userFirstName
import com.example.service.user.domain.UserFunctions.userIdAsInt
import com.example.service.user.domain.UserFunctions.userLastName
import com.example.service.user.domain.UserFunctions.userPhoneNumber
import com.example.service.user.utils.fakeUserBuilder
import com.example.service.user.utils.fakeUserData
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@ExtendWith(MockKExtension::class)
internal class UserJpaMapperTest {

    @InjectMockKs
    private lateinit var userJpaMapper: UserJpaMapper

    @Test
    fun `should map to jpa entity from domain, for new user` () {
        val user: User = fakeUserBuilder().id(null).build()

        val userData = userJpaMapper.toJpaEntity(user)

        assertThat(userData.id).isNull()
        assertThat(userData.firstName).isEqualTo(userFirstName(user))
        assertThat(userData.lastName).isEqualTo(userLastName(user))
        assertThat(userData.phone).isEqualTo(userPhoneNumber(user))
        assertThat(userData.createdAt).isCloseTo(LocalDateTime.now(), within(100, ChronoUnit.MILLIS))
        assertThat(userData.updatedAt).isCloseTo(LocalDateTime.now(), within(100, ChronoUnit.MILLIS))
    }

    @Test
    fun `should map to jpa entity from doain, for an existing user` () {
        val user: User = fakeUserBuilder().build()

        val userData = userJpaMapper.toJpaEntity(user)

        assertThat(userData.id).isEqualTo(userIdAsInt(user))
        assertThat(userData.firstName).isEqualTo(userFirstName(user))
        assertThat(userData.lastName).isEqualTo(userLastName(user))
        assertThat(userData.phone).isEqualTo(userPhoneNumber(user))
        assertThat(userData.createdAt).isCloseTo(LocalDateTime.now(), within(100, ChronoUnit.MILLIS))
        assertThat(userData.updatedAt).isCloseTo(LocalDateTime.now(), within(100, ChronoUnit.MILLIS))
    }

    @Test
    fun `should map to jpa entity from domain, overriding persisting jpa entity` () {
        val user: User = fakeUserBuilder().id(null).build()
        val persistedUserData: UserData = fakeUserData()

        val userData = userJpaMapper.toJpaEntity(user, persistedUserData)

        assertThat(userData.id).isEqualTo(persistedUserData.id)
        assertThat(userData.firstName).isEqualTo(userFirstName(user))
        assertThat(userData.lastName).isEqualTo(userLastName(user))
        assertThat(userData.phone).isEqualTo(userPhoneNumber(user))
        assertThat(userData.createdAt).isEqualTo(persistedUserData.createdAt)
        assertThat(userData.updatedAt).isCloseTo(LocalDateTime.now(), within(100, ChronoUnit.MILLIS))
    }

    @Test
    fun `should map from jpa entity to domain` () {
        val userData: UserData = fakeUserData()

        val user = userJpaMapper.toDomain(userData)

        assertThat(userIdAsInt(user)).isEqualTo(userData.id)
        assertThat(userFirstName(user)).isEqualTo(userData.firstName)
        assertThat(userLastName(user)).isEqualTo(userData.lastName)
        assertThat(userPhoneNumber(user)).isEqualTo(userData.phone)
    }

}