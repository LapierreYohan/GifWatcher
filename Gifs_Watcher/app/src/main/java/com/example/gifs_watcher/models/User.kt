package com.example.gifs_watcher.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
 */

@Entity(tableName = "users")
data class User (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val idUsers: Int? = null,

    @ColumnInfo(name = "username")
    var username: String? = null,

    @ColumnInfo(name = "password")
    var password: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "lastname")
    var lastName: String? = null,

    @ColumnInfo(name = "mail")
    var mail: String? = null,

    @ColumnInfo(name = "bio")
    var bio: String? = null,

    @ColumnInfo(name = "picture")
    var profilPicture: String? = null
)
