package com.example.service.user.utils

import com.example.service.user.adapter.entrypoint.api.model.SaveUserBodyDto
import com.example.service.user.adapter.entrypoint.api.model.UserDto
import com.example.service.user.adapter.persistence.model.UserData
import com.example.service.user.domain.User
import com.example.service.user.domain.UserFullName
import com.example.service.user.domain.UserId
import com.example.service.user.domain.UserPhone
import com.github.javafaker.Faker
import com.github.javafaker.service.FakeValuesService
import com.github.javafaker.service.RandomService
import java.time.LocalDateTime
import java.util.*

internal object DataFaker {
    internal val FAKER = Faker()
    internal val FAKE_VALUES_SERVICE = FakeValuesService(Locale.CANADA, RandomService())
}

fun fakeUserId() = UserId(fakeUserIdAsInt())

fun fakeUserIdAsInt() = DataFaker.FAKER.number().numberBetween(1, 10000)

fun fakeFullName() = UserFullName(DataFaker.FAKER.name().firstName(), DataFaker.FAKER.name().lastName())

fun fakePhone() = UserPhone.from(fakePhoneNumberAsString())

private fun fakePhoneNumberAsString(): String {
    return DataFaker.FAKE_VALUES_SERVICE.regexify("\\(\\+[1-9]\\d{1,2}\\) [0-9]{1,3}[0-9\\-]{6,9}[0-9]{1}")
}

fun fakeUserBuilder() = User.Builder()
            .id(fakeUserId())
            .fullName(fakeFullName())
            .phone(fakePhone())

fun fakeUser() = fakeUserBuilder()
            .build()

fun fakeUserDataBuilder() = UserData.Builder()
            .id(fakeUserIdAsInt())
            .firstName(DataFaker.FAKER.name().firstName())
            .lastName(DataFaker.FAKER.name().lastName())
            .phone(fakePhoneNumberAsString())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())

fun fakeUserData() = fakeUserDataBuilder()
            .build()

fun fakeUserDto() = UserDto.Builder()
        .firstName(DataFaker.FAKER.name().firstName())
        .lastName(DataFaker.FAKER.name().lastName())
        .phone(fakePhoneNumberAsString())
        .build()

fun fakeSaveUserBodyDto() = SaveUserBodyDto(
        DataFaker.FAKER.name().firstName(),
        DataFaker.FAKER.name().lastName(),
        fakePhoneNumberAsString())