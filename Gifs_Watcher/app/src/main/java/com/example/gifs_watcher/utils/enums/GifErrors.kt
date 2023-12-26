package com.example.gifs_watcher.utils.enums

enum class GifErrors (override val message : String) : BaseError {
    GIF_IS_EMPTY("The gif is empty."),
    GIF_NOT_FOUND("The gif is not found."),
    GIF_CONTAINS_EXCEPTED_CHARACTERS("The gif code contains unauthorized characters. (Only numbers are allowed)"),
    UNKNOWN_ERROR("An unknown error has occurred.")
}