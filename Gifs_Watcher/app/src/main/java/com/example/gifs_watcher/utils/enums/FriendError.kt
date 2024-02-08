package com.example.gifs_watcher.utils.enums

enum class FriendError(override val message : String) : BaseError {
    USER_NOT_EXIST("The user does not exist."),
    MUST_BE_CONNECTED("You must be connected to send a friend request."),
    UNKNOWN_ERROR("An unknown error occurred."),
    CANT_ADD_SELF("You can't add yourself as a friend."),
    ALREADY_FRIEND_OR_PENDING("You are already friend or have a pending request with this user."),
}