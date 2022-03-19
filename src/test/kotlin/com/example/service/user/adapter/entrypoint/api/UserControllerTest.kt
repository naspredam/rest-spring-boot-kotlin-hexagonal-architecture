package com.example.service.user.adapter.entrypoint.api

import com.example.service.user.infrastructure.controller.dto.SaveUserBodyDto
import com.example.service.user.infrastructure.controller.dto.UserDto
import com.example.service.user.application.port.entrypoint.api.ChangeUserEndpointPort
import com.example.service.user.application.port.entrypoint.api.FindUserEndpointPort
import com.example.service.user.infrastructure.controller.UserController
import com.example.service.user.utils.fakeSaveUserBodyDto
import com.example.service.user.utils.fakeUserDto
import com.example.service.user.utils.fakeUserIdAsInt
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@TestConfiguration
internal class ControllerTestConfig {
    @Bean
    fun changeUserEndpointPort() = mockk<ChangeUserEndpointPort>()

    @Bean
    fun findUserEndpointPort() = mockk<FindUserEndpointPort>()
}

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ControllerTestConfig::class, UserController::class])
@AutoConfigureMockMvc
@EnableWebMvc
internal class UserControllerTest(
        @Autowired private val mockMvc: MockMvc,
        @Autowired private val changeUserEndpointPort: ChangeUserEndpointPort,
        @Autowired private val findUserEndpointPort: FindUserEndpointPort
) {

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `should create new user on users post request` () {
        val bodyDto: SaveUserBodyDto = fakeSaveUserBodyDto()
        val userDto = UserDto.Builder()
                .firstName(bodyDto.firstName)
                .lastName(bodyDto.lastName)
                .phone(bodyDto.phone)
                .build()

        every { changeUserEndpointPort.saveUser(bodyDto) } returns userDto

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bodyDto)))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("firstName", `is`(bodyDto.firstName)))
                .andExpect(jsonPath("lastName", `is`(bodyDto.lastName)))
                .andExpect(jsonPath("phone", `is`(bodyDto.phone)))
    }

    @Test
    fun `should retrieve user by id when pre populated on the database` () {
        val userId = fakeUserIdAsInt()
        val userDto = fakeUserDto()

        every { findUserEndpointPort.fetchUserById(userId) } returns userDto

        mockMvc.perform(get("/users/${userId}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("firstName", `is`(userDto.firstName)))
                .andExpect(jsonPath("lastName", `is`(userDto.lastName)))
                .andExpect(jsonPath("phone", `is`(userDto.phone)))
    }

    @Test
    fun `should retrieve all users on get users request` () {
        val userDto1 = fakeUserDto()
        val userDto2 = fakeUserDto()

        every { findUserEndpointPort.fetchAllUsers() } returns listOf(userDto1, userDto2)

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("length()", `is`(2)))
                .andExpect(jsonPath("[0].firstName", `is`(userDto1.firstName)))
                .andExpect(jsonPath("[0].lastName", `is`(userDto1.lastName)))
                .andExpect(jsonPath("[0].phone", `is`(userDto1.phone)))
                .andExpect(jsonPath("[1].firstName", `is`(userDto2.firstName)))
                .andExpect(jsonPath("[1].lastName", `is`(userDto2.lastName)))
                .andExpect(jsonPath("[1].phone", `is`(userDto2.phone)))
    }

    @Test
    fun `should update an existing user` () {
        val userId = fakeUserIdAsInt()
        val userDto = fakeUserDto()
        val bodyDto: SaveUserBodyDto = fakeSaveUserBodyDto()

        every { changeUserEndpointPort.updateUser(userId, bodyDto) } returns userDto

        mockMvc.perform(put("/users/${userId}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bodyDto)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("firstName", `is`(userDto.firstName)))
                .andExpect(jsonPath("lastName", `is`(userDto.lastName)))
                .andExpect(jsonPath("phone", `is`(userDto.phone)))
    }

    @Test
    fun `should delete user` () {
        val userId = fakeUserIdAsInt()

        every { changeUserEndpointPort.deleteUser(userId) } returns Unit

        mockMvc.perform(delete("/users/${userId}"))
                .andExpect(status().isNoContent)

        verify { changeUserEndpointPort.deleteUser(userId) }
    }

}