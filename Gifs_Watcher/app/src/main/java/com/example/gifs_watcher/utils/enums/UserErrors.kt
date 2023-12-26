package com.example.gifs_watcher.utils.enums

enum class UserErrors (override val message : String) : BaseError {
    ID_OR_PASSWORD_INVALID("The username or password is invalid."),
    ID_EMPTY("The username is empty."),
    PASSWORD_INVALID("The password is invalid."),
    PASSWORD_TOO_SHORT("The password is too short (minimum 6 characters)."),
    PASSWORD_IS_EMPTY("The password is empty."),
    USERNAME_ALREADY_USED("The username is already used."),
    USERNAME_TOO_SHORT("The username is too short (minimum 3 characters)."),
    USERNAME_TOO_LONG("The username is too long (maximum 20 characters)."),
    USERNAME_IS_EMPTY("The username is empty."),
    USERNAME_CONTAINS_EXCEPTED_CHARACTERS("The username contains unauthorized characters."),
    EMAIL_ALREADY_USED("The email is already used."),
    EMAIL_CONTAINS_EXCEPTED_CHARACTERS("The email contains unauthorized characters."),
    EMAIL_IS_EMPTY("The email is empty."),
    PASSWORDS_NOT_MATCHING("The passwords are not matching."),
    EMAIL_NOT_VALID("The email is not valid."),
    USERNAME_NOT_VALID("The username is not valid."),
    BIRTHDATE_NOT_VALID("The birthdate is not valid."),
    BIRTHDATE_TOO_YOUNG("You must be at least 15 years old to register."),
    BIRTHDATE_IS_EMPTY("The birthdate is empty."),
    UNKNOWN_ERROR("An unknown error has occurred.")
}