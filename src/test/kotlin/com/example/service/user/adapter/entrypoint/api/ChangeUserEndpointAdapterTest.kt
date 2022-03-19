package com.example.service.user.adapter.entrypoint.api

import com.example.service.user.infrastructure.controller.dto.SaveUserBodyDto
import com.example.service.user.infrastructure.controller.dto.UserDto
import com.example.service.user.application.usecase.SubmitChangeForExistingUserUseCase
import com.example.service.user.application.usecase.SubmitDeleteUserUseCase
import com.example.service.user.application.usecase.SubmitNewUserUseCase
import com.example.service.user.domain.User
import com.example.service.user.domain.UserId
import com.example.service.user.utils.*
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ChangeUserEndpointAdapterTest {

    @InjectMockKs
    private lateinit var changeUserEndpointAdapter: ChangeUserEndpointAdapter

    @MockK
    private lateinit var  submitNewUserUseCase: SubmitNewUserUseCase

    @MockK
    private lateinit var  submitChangeForExistingUserUseCase: SubmitChangeForExistingUserUseCase

    @MockK
    private lateinit var  submitDeleteUserUseCase: SubmitDeleteUserUseCase

    @MockK
    private lateinit var  userDtoMapper: UserDtoMapper

    @Test
    fun `should save new user, returning the data persisted` () {
        val saveUserBodyDto: SaveUserBodyDto = fakeSaveUserBodyDto()
        val userTransformedFromSaveUserDto: User = fakeUser()
        val userCreated: User = fakeUser()
        val userDtoConvertedFromSaveUser: UserDto = fakeUserDto()

        every { userDtoMapper.toDomainFromSaveBody(saveUserBodyDto) } returns userTransformedFromSaveUserDto
        every { submitNewUserUseCase.saveUser(userTransformedFromSaveUserDto) } returns userCreated
        every { userDtoMapper.toDto(userCreated) } returns userDtoConvertedFromSaveUser

        val savedUserDto = changeUserEndpointAdapter.saveUser(saveUserBodyDto)
        Assertions.assertThat(savedUserDto).isEqualTo(userDtoConvertedFromSaveUser)
    }

    @Test
    fun `should update user, returning user data persisted` () {
        val userId: Int = fakeUserIdAsInt()
        val saveUserBodyDto: SaveUserBodyDto = fakeSaveUserBodyDto()
        val userTransformedFromSaveUserDto: User = fakeUser()
        val userCreated: User = fakeUser()
        val userDtoConvertedFromSaveUser: UserDto = fakeUserDto()

        every {  userDtoMapper.toDomainFromSaveBody(userId, saveUserBodyDto) } returns userTransformedFromSaveUserDto
        every {  submitChangeForExistingUserUseCase.updateUser(userTransformedFromSaveUserDto)} returns userCreated
        every {  userDtoMapper.toDto(userCreated) } returns userDtoConvertedFromSaveUser

        val savedUserDto = changeUserEndpointAdapter.updateUser(userId, saveUserBodyDto)
        Assertions.assertThat(savedUserDto).isEqualTo(userDtoConvertedFromSaveUser)
    }

    @Test
    fun `should delete user, returning void!` () {
        val userId: UserId = fakeUserId()

        every { submitDeleteUserUseCase.deleteById(userId) } returns Unit

        changeUserEndpointAdapter.deleteUser(userId.intValue)

        verify { submitDeleteUserUseCase.deleteById(userId) }
    }
}