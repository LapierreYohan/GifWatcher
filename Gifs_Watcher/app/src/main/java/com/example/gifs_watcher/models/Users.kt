package com.example.gifswatcher.models

/* Import et fonctions afin de transformer un String en une chaine MD5SUM */
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
// Exemple : println(md5("Hello, world!").toHex())
/*------------------------------------------------------------------------*/

data class Users (
    val username: String,
    val password: String,
    val name: String?,
    val lastName: String?,
    val mail: String?,
    val bio: String?,
    val profilPicture: String?,
    val idUsers: String
){
}