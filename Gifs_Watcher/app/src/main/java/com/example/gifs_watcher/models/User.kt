package com.example.gifs_watcher.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "users")
data class User (

    @ColumnInfo(name = "username")
    val username: String? = null,

    @ColumnInfo(name = "password")
    val password: String? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "lastname")
    val lastName: String? = null,

    @ColumnInfo(name = "mail")
    val mail: String? = null,

    @ColumnInfo(name = "bio")
    val bio: String? = null,

    @ColumnInfo(name = "birthdate")
    val birthdate: String? = null,

    @ColumnInfo(name = "picture")
    val profilPicture: String? = null,

    @ColumnInfo(name = "created")
    val created: String? = null,
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var idUsers: Int? = null


}
