package com.example.gifs_watcher.utils

import androidx.room.TypeConverter
import com.example.gifs_watcher.models.Media
import com.google.gson.Gson


class Converters {

    @TypeConverter
    fun fromMedia(value:Media?):String {

        if (value == null) {
            return ""
        }

        return Gson().toJson(value)
    }
}