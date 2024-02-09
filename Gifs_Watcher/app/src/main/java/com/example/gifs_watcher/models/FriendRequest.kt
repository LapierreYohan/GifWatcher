package com.example.gifs_watcher.models

import com.google.firebase.Timestamp

data class FriendRequest(
    val author: String,
    val destToken : String,
    val dest: String,
    var displayDestId: String?,
    var displayDest: String?,
    var displayDestAvatar: String?,
    val status: String,
    var timestamp: Timestamp? = null
)