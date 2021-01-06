package com.example.service.user.adapter.entrypoint.api

import com.example.service.user.domain.UserFunctions.userFirstName
import com.example.service.user.domain.UserFunctions.userLastName
import com.example.service.user.domain.UserFunctions.userPhoneNumber
import com.example.service.user.domain.UserId
import com.example.service.user.utils.fakeSaveUserBodyDto
import com.example.service.user.utils.fakeUser
import com.example.service.user.utils.fakeUserIdAsInt
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class UserDtoMapperTest {

    @InjectMockKs
    private lateinit var userDtoMapper: UserDtoMapper

    @Test
    fun `should map to dto from domain` () {
        val user = fakeUser()
        val (firstName, lastName, phone) = userDtoMapper.toDto(user)
        assertThat(firstName).isEqualTo(userFirstName(user))
        assertThat(lastName).isEqualTo(userLastName(user))
        assertThat(phone).isEqualTo(userPhoneNumber(user))
    }

    @Test
    fun `should map to domain from dto, without user id` () {
        val saveUserBodyDto = fakeSaveUserBodyDto()
        val user = userDtoMapper.toDomainFromSaveBody(saveUserBodyDto)
        assertThat(user.id).isNull()
        assertThat(userFirstName(user)).isEqualTo(saveUserBodyDto.firstName)
        assertThat(userLastName(user)).isEqualTo(saveUserBodyDto.lastName)
        assertThat(userPhoneNumber(user)).isEqualTo(saveUserBodyDto.phone)
    }

    @Test
    fun `should map to domain from dto with user id` () {
        val userId = fakeUserIdAsInt()
        val saveUserBodyDto = fakeSaveUserBodyDto()

        val user = userDtoMapper.toDomainFromSaveBody(userId, saveUserBodyDto)

        assertThat(user.id).isEqualTo(UserId(userId))
        assertThat(userFirstName(user)).isEqualTo(saveUserBodyDto.firstName)
        assertThat(userLastName(user)).isEqualTo(saveUserBodyDto.lastName)
        assertThat(userPhoneNumber(user)).isEqualTo(saveUserBodyDto.phone)
    }
}