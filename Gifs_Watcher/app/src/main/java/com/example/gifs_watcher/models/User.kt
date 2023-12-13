package com.example.gifs_watcher.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

data class User (

    var idUsers: String? = null,

    var username: String? = null,

    var password: String? = null,

    val name: String? = null,

    val lastName: String? = null,

    val mail: String? = null,

    val bio: String? = null,

    val birthdate: String? = null,

    val profilPicture: String? = null,
)
