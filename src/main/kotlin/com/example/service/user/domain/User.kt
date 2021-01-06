package com.example.service.user.domain

data class User
    private constructor(internal val id: UserId? = null,
                        internal val fullName: UserFullName,
                        internal val phone: UserPhone) {

    data class Builder(
            var id: UserId? = null,
            var fullName: UserFullName? = null,
            var phone: UserPhone? = null) {

        fun id(id: UserId?) = apply { this.id = id }
        fun fullName(fullName: UserFullName) = apply { this.fullName = fullName }
        fun phone(phone: UserPhone) = apply { this.phone = phone }
        fun build() = User(id, fullName!!, phone!!)
    }

}

object UserFunctions {

    val userIdAsInt = composeOptional(UserId::intValue, User::id)
    val userFirstName = compose(UserFullName::firstName, User::fullName)
    val userLastName = compose(UserFullName::lastName, User::fullName)
    val userPhoneNumber = compose(UserPhone::toString, User::phone)

    private fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { f(g(it)) }
    private fun <A, B, C> composeOptional(f: (B) -> C, g: (A) -> B?): (A) -> C? = { x -> g(x)?.let { f(it) } }
}