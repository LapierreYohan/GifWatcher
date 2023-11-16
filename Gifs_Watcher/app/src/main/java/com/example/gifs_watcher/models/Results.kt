package com.example.gifs_watcher.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "gifs")
data class Results (

  @SerializedName("id")
  @PrimaryKey
  @ColumnInfo(name = "id")
  var id                 : String?           = null,

  @SerializedName("content_description")
  @ColumnInfo(name = "description")
  var contentDescription : String?           = null,

  @SerializedName("content_rating")
  @ColumnInfo(name = "rating")
  var contentRating      : String?           = null,

  @SerializedName("media")
  @ColumnInfo(name = "media")
  var media              : ArrayList<Media>?  = arrayListOf(),

  @SerializedName("bg_color")
  @ColumnInfo(name = "background")
  var bgColor            : String?           = null,

  @SerializedName("created")
  @ColumnInfo(name = "created")
  var created            : Double?           = null,
)