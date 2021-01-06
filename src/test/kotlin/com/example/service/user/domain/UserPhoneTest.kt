package com.example.service.user.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class UserPhoneTest {

    @Test
    fun `should throw exception when number format is invalid`() {
        Assertions.assertThatThrownBy { UserPhone.from("blah") }
                .isExactlyInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should accept phone number as single string and be able to get country code and number`() {
        val phone = UserPhone.from("(+34) 2 443-312-1")
        Assertions.assertThat(phone.countryCode).isEqualTo(34)
        Assertions.assertThat(phone.number).isEqualTo("2 443-312-1")
        Assertions.assertThat(phone.toString()).isEqualTo("(+34) 2 443-312-1")
    }


    @Test
    fun `should build user phone when building from country code and number`() {
        val phone = UserPhone(55, "2 443-312-1")
        Assertions.assertThat(phone.countryCode).isEqualTo(55)
        Assertions.assertThat(phone.number).isEqualTo("2 443-312-1")
        Assertions.assertThat(phone).asString().isEqualTo("(+55) 2 443-312-1")
    }

}