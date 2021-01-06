package com.example.service.user.adapter.persistence

import com.example.service.user.adapter.persistence.model.UserData
import com.example.service.user.utils.fakeUserDataBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
internal class UserRepositoryTest(
        @Autowired private val userRepository: UserRepository
) {

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
    }

    @Test
    fun `should return true when there is a user for given first and last name` () {
        val userData: UserData = fakeUserDataBuilder().id(null).build()
        userRepository.save(userData)

        val firstName = userData.firstName
        val lastName = userData.lastName

        val found = userRepository.findByFirstNameAndLastName(firstName!!, lastName!!)

        assertThat(found.isEmpty()).isFalse
    }

    @Test
    fun `should return false when there is no user for given first and last name` () {
        val userData: UserData = fakeUserDataBuilder().id(null).build()
        userRepository.save(userData)

        val firstName = userData.firstName + "a"
        val lastName = userData.lastName

        val found = userRepository.findByFirstNameAndLastName(firstName, lastName!!)

        assertThat(found.isEmpty()).isTrue
    }
}