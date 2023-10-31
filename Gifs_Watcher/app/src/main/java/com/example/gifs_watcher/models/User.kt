package com.example.gifs_watcher.models

/*
fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
 */

data class User (
    var username: String? = null,
    var password: String? = null,
    var name: String? = null,
    var lastName: String? = null,
    var mail: String? = null,
    var bio: String? = null,
    var profilPicture: String? = null,
    val idUsers: String? = null
)
