package com.example.service.user.adapter.entrypoint.api

import com.example.service.user.adapter.entrypoint.api.model.SaveUserBodyDto
import com.example.service.user.adapter.persistence.UserRepository
import com.example.service.user.utils.fakeSaveUserBodyDto
import com.example.service.user.utils.fakeUserDataBuilder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest
@EnableWebMvc
@AutoConfigureMockMvc
@Transactional
internal class UserControllerIntegrationTest(
        @Autowired private val mockMvc: MockMvc,
        @Autowired private val userRepository: UserRepository
) {

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `should create new user on users post request` () {
        val bodyDto: SaveUserBodyDto = fakeSaveUserBodyDto()

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
        val userData = fakeUserDataBuilder().id(null).build()
        val savedUserData = userRepository.save(userData)
        val userId = savedUserData.id

        mockMvc.perform(get("/users/${userId}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("firstName", `is`(savedUserData.firstName)))
                .andExpect(jsonPath("lastName", `is`(savedUserData.lastName)))
                .andExpect(jsonPath("phone", `is`(savedUserData.phone)))
    }

    @Test
    fun `should retrieve all users from persistence layer on a get users request` () {
        val userData1 = fakeUserDataBuilder().id(null).build()
        val userData2 = fakeUserDataBuilder().id(null).build()
        userRepository.saveAll(listOf(userData1, userData2))

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("length()", `is`(2)))
                .andExpect(jsonPath("[0].firstName", `is`(userData1.firstName)))
                .andExpect(jsonPath("[0].lastName", `is`(userData1.lastName)))
                .andExpect(jsonPath("[0].phone", `is`(userData1.phone)))
                .andExpect(jsonPath("[1].firstName", `is`(userData2.firstName)))
                .andExpect(jsonPath("[1].lastName", `is`(userData2.lastName)))
                .andExpect(jsonPath("[1].phone", `is`(userData2.phone)))
    }

    @Test
    fun `should update an existing user on the persistence layer` () {
        val userData = fakeUserDataBuilder().id(null).build()
        val savedUserData = userRepository.save(userData)
        val userId = savedUserData.id
        val bodyDto = fakeSaveUserBodyDto()

        mockMvc.perform(put("/users/${userId}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bodyDto)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("firstName", `is`(bodyDto.firstName)))
                .andExpect(jsonPath("lastName", `is`(bodyDto.lastName)))
                .andExpect(jsonPath("phone", `is`(bodyDto.phone)))
    }

    @Test
    fun `should hard delete user` () {
        val userData = fakeUserDataBuilder().id(null).build()
        val savedUserData = userRepository.save(userData)
        val userId = savedUserData.id

        assertThat(userRepository.existsById(userId!!)).isTrue

        mockMvc.perform(delete("/users/${userId}"))
                .andExpect(status().isNoContent)

        assertThat(userRepository.existsById(userId)).isFalse
    }

}