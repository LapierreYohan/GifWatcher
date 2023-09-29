package com.example.gifs_watcher.models

import com.google.gson.annotations.SerializedName


data class TenorData (

  @SerializedName("results")
  var results : ArrayList<Results?> = arrayListOf(),
)