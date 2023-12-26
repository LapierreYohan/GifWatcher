package com.example.gifs_watcher.models.maps.models


data class GifMap(
    var id : String? = null,
    var content_description : String? = null,
    var content_rating : String? = null,

    var preview : String? = null,
    var url : String? = null,
    var height : Int? = null,
    var width : Int? = null,
    var duration: Double? = null,
    var size : Int? = null,

    var tiny_url : String? = null,
    var tiny_preview : String? = null,

    var bgColor : String? = null,
    var created : Double? = null,

    var stars : Int = 0,
    var likes : Int = 0,
    var dislikes : Int = 0,
    var comments : Int = 0,
    var shares : Int = 0,
    var views : Int = 0,
)
