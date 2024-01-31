package com.example.gifs_watcher.models.maps

import com.example.gifs_watcher.models.Gif
import com.example.gifs_watcher.models.Media
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.maps.models.GifMap

object GifMapper {
    fun map(gif : Results) : GifMap? {
        try {
            return GifMap(
                id = gif.id,
                contentDescription = gif.contentDescription,
                contentRating = gif.contentRating,

                preview = gif.media?.get(0)?.gif?.preview,
                url = gif.media?.get(0)?.gif?.url,
                height = gif.media?.get(0)?.gif?.dims?.get(1),
                width = gif.media?.get(0)?.gif?.dims?.get(0),
                duration = gif.media?.get(0)?.gif?.duration,
                size = gif.media?.get(0)?.gif?.size,

                tinyUrl = gif.media?.get(0)?.tinygif?.url,
                tinyPreview = gif.media?.get(0)?.tinygif?.preview,

                bgColor = gif.bgColor,
                created = gif.created,
            )
        } catch (e: Exception) {
            return null
        }
    }

    fun reverseMap(gifMap: GifMap): Results {
        return Results(
            id = gifMap.id,
            contentDescription = gifMap.contentDescription,
            contentRating = gifMap.contentRating,

            media = arrayListOf(
                Media(
                    gif = Gif(
                        preview = gifMap.preview,
                        url = gifMap.url,
                        dims = arrayListOf(gifMap.width!!, gifMap.height!!),
                        duration = gifMap.duration,
                        size = gifMap.size
                    ),
                    tinygif = Gif(
                        url = gifMap.tinyUrl,
                        preview = gifMap.tinyPreview
                    )
                )
            ),

            bgColor = gifMap.bgColor,
            created = gifMap.created
        )
    }
}