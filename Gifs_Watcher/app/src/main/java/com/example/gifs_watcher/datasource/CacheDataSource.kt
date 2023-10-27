package com.example.gifs_watcher.datasource

import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.utils.enums.CacheMode

object CacheDataSource {

    private var randomData : ArrayList<Results?> = arrayListOf()
    private var searchData : ArrayList<Results?> = arrayListOf()
    private var mode : CacheMode = CacheMode.RANDOM

    fun size() : Int {
        if (this.mode == CacheMode.RANDOM) {
            return this.randomData.size;
        } else if (this.mode == CacheMode.SEARCH) {
            return this.searchData.size;
        }

        return -1;
    }

    fun replace(tenorData : ArrayList<Results?>) : Unit {
        if (this.mode == CacheMode.RANDOM) {
            this.randomData.clear()
            this.randomData.addAll(tenorData)
        } else if (this.mode == CacheMode.SEARCH) {
            this.searchData.clear()
            this.searchData.addAll(tenorData)
        }
    }

    fun add(tenorData : ArrayList<Results?>) : Unit {
        if (this.mode == CacheMode.RANDOM) {
            this.randomData.addAll(tenorData)
        } else if (this.mode == CacheMode.SEARCH) {
            this.searchData.addAll(tenorData)
        }
    }
    fun get() : ArrayList<Results?> {
        if (this.mode == CacheMode.RANDOM) {
            return this.randomData
        } else if (this.mode == CacheMode.SEARCH) {
            return this.searchData
        }
        return arrayListOf()
    }

    fun switch(mode : CacheMode? = null) : CacheMode {
        if (mode == null && this.mode == CacheMode.RANDOM) {
            this.mode = CacheMode.SEARCH
        } else if (mode == null && this.mode == CacheMode.SEARCH) {
            this.mode = CacheMode.RANDOM
        } else if (mode != null){
            this.mode = mode
        }

        return this.mode
    }

    fun getMode() : CacheMode {
        return this.mode
    }
}