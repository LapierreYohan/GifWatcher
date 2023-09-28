package com.example.gifs_watcher.models

import com.google.gson.annotations.SerializedName


data class Media (

    @SerializedName("tinygif")
  var tinygif : Tinygif? = Tinygif(),

    @SerializedName("gif")
  var gif : Gif? = Gif()
)