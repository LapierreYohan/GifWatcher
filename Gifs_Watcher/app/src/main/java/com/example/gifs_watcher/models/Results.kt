package com.example.gifs_watcher.models

import com.google.gson.annotations.SerializedName


data class Results (

  @SerializedName("id"                  )
  var id                 : String?           = null,

  @SerializedName("title"               )
  var title              : String?           = null,

  @SerializedName("content_description" )
  var contentDescription : String?           = null,

  @SerializedName("content_rating"      )
  var contentRating      : String?           = null,

  @SerializedName("h1_title"            )
  var h1Title            : String?           = null,

  @SerializedName("media"               )
  var media              : ArrayList<Media>?  = arrayListOf(),

  @SerializedName("bg_color"            )
  var bgColor            : String?           = null,

  @SerializedName("created"             )
  var created            : Double?           = null,
)