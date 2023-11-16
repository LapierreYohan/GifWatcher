package com.example.gifs_watcher.models

import com.google.gson.annotations.SerializedName

data class Gif (

  @SerializedName("preview")
  var preview : String? = null,

  @SerializedName("url")
  var url : String? = null,

  @SerializedName("dims")
  var dims : ArrayList<Int>? = arrayListOf(),

  @SerializedName("duration")
  var duration : Double? = null,

  @SerializedName("size")
  var size : Int? = null
)