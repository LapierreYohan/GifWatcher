package com.example.gifs_watcher.utils.managers

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordManager {

    fun hashPassword(password : String) : String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun verifyPassword(password : String, hash : String) : Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified
    }
}