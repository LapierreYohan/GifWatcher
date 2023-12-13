package com.example.gifs_watcher.models.responses

import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.utils.enums.UserErrors

class UserResponse {
    private var error : ArrayList<UserErrors?> = arrayListOf()
    private var user : User? = null

    constructor(){}

    fun success() : Boolean {
        return error.isEmpty() && user != null
    }

    fun failed() : Boolean {
        return !error.isEmpty()
    }

    fun error() : ArrayList<UserErrors?> {
        return error
    }

    fun user() : User? {
        return user
    }

    fun addError(error : UserErrors) {
        this.error.add(error)
    }

    fun addUser(user : User) {
        this.user = user
    }

    override fun toString(): String {
        return "UserResponse(error=$error, user=$user)"
    }
}