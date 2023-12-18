package com.example.gifs_watcher.models.maps

import com.example.gifs_watcher.models.Gif
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.maps.models.GifMap

object GifMapper {
    fun map(gif : Results) : GifMap {
        return GifMap(
            id = gif.id,
            content_description = gif.contentDescription,
            content_rating = gif.contentRating,

            preview = gif.media?.get(0)?.gif?.preview,
            url = gif.media?.get(0)?.gif?.url,
            height = gif.media?.get(0)?.gif?.dims?.get(1),
            width = gif.media?.get(0)?.gif?.dims?.get(0),
            duration = gif.media?.get(0)?.gif?.duration,
            size = gif.media?.get(0)?.gif?.size,

            tiny_url = gif.media?.get(0)?.tinygif?.url,
            tiny_preview = gif.media?.get(0)?.tinygif?.preview,

            bgColor = gif.bgColor,
            created = gif.created,
        )
    }
}