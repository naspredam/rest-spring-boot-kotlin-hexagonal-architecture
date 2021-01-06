package com.example.service.user.domain

import java.util.regex.Matcher
import java.util.regex.Pattern

data class UserId(val intValue: Int)

data class UserFullName(internal val firstName: String, internal val lastName: String)

data class UserPhone(internal val countryCode: Int, internal val number: String) {

    companion object {
        private val PATTERN = Pattern.compile("\\(\\+(\\d+)\\)\\s*([0-9\\-\\s]+)")
        fun from(fullPhone: String): UserPhone {
            val matcher: Matcher = PATTERN.matcher(fullPhone)
            if (matcher.find()) {
                val countryCode: Int = matcher.group(1).toInt()
                return UserPhone(countryCode, matcher.group(2))
            }
            throw IllegalArgumentException("Cell phone introduced in not valid")
        }
    }

    override fun toString(): String = "(+${countryCode}) $number"
}
