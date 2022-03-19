package com.example.service.user.adapter.entrypoint.api

import com.example.service.user.infrastructure.controller.dto.UserDto
import com.example.service.user.application.usecase.FindAllUsersUseCase
import com.example.service.user.application.usecase.FindUserByIdUseCase
import com.example.service.user.domain.User
import com.example.service.user.domain.UserId
import com.example.service.user.utils.fakeUser
import com.example.service.user.utils.fakeUserDto
import com.example.service.user.utils.fakeUserIdAsInt
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class FindUserEndpointAdapterTest {

    @InjectMockKs
    private lateinit var findUserEndpointAdapter: FindUserEndpointAdapter

    @MockK
    private lateinit var findAllUsersUseCase: FindAllUsersUseCase

    @MockK
    private lateinit var findUserByIdUseCase: FindUserByIdUseCase

    @MockK
    private lateinit var userDtoMapper: UserDtoMapper


    @Test
    fun `should return dto object when getting by user id` () {
        val userIdInt: Int = fakeUserIdAsInt()
        val userId = UserId(userIdInt)
        val user: User = fakeUser()
        val userDto: UserDto = fakeUserDto()

        every { findUserByIdUseCase.findById(userId) } returns user
        every { userDtoMapper.toDto(user) } returns userDto

        val fetchUserById: UserDto = findUserEndpointAdapter.fetchUserById(userIdInt)
        assertThat(fetchUserById).isEqualTo(userDto)
    }

    @Test
    fun `should return empty when there are no users available` () {
        every { findAllUsersUseCase.fetchAllPersisted() } returns emptyList()

        val userDtos: Collection<UserDto> = findUserEndpointAdapter.fetchAllUsers()
        assertThat(userDtos).isEmpty()
    }

    @Test
    fun `should list all users when users are available` () {
        val user1: User = fakeUser()
        val user2: User = fakeUser()
        val userDto1: UserDto = fakeUserDto()
        val userDto2: UserDto = fakeUserDto()

        every { findAllUsersUseCase.fetchAllPersisted() } returns listOf(user1, user2)
        every { userDtoMapper.toDto(user1) } returns userDto1
        every { userDtoMapper.toDto(user2) } returns userDto2

        val userDtos = findUserEndpointAdapter.fetchAllUsers()
        assertThat(userDtos).hasSize(2)
                .containsExactlyInAnyOrder(userDto1, userDto2)
    }
}