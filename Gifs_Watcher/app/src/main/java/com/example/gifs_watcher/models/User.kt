package com.example.gifs_watcher.models

data class User (

    var idUsers: String? = null,

    var username: String? = null,

    var displayname: String? = null,

    var password: String? = null,

    val mail: String? = null,

    var bio: String? = null,

    val birthdate: String? = null,

    var profilPicture: String? = null,

    var lowProfilPicture: String? = null,

    var staticProfilPicture : String? = null,
)
