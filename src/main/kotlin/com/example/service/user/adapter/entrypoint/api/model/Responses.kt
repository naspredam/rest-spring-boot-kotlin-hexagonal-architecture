package com.example.service.user.adapter.entrypoint.api.model

data class UserDto
    private constructor(val firstName: String?, val lastName: String?, val phone: String?) {

    data class Builder(
            var firstName: String? = null,
            var lastName: String? = null,
            var phone: String? = null) {

        fun firstName(firstName: String) = apply { this.firstName = firstName }
        fun lastName(lastName: String) = apply { this.lastName = lastName }
        fun phone(phone: String) = apply { this.phone = phone }
        fun build() = UserDto(firstName, lastName, phone)
    }
}