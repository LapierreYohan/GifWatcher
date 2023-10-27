package com.example.gifs_watcher.models

import com.google.gson.annotations.SerializedName


data class Media (

    @SerializedName("tinygif")
  var tinygif : Gif? = Gif(),

    @SerializedName("gif")
  var gif : Gif? = Gif()
)