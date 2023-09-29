package com.example.gifs_watcher.managers

import com.example.gifs_watcher.models.Results

object CacheManager {

    private var data : ArrayList<Results> = arrayListOf()

    fun setArray(tenorData : ArrayList<Results>) : Unit {
        this.data = tenorData
    }
    fun addArray(tenorData : ArrayList<Results>) : Unit {
        this.data.addAll(tenorData)
    }
    fun getData() : ArrayList<Results> {
        return this.data
    }
}