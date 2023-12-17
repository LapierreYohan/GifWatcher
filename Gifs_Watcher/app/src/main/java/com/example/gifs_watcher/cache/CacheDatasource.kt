package com.example.gifs_watcher.cache

import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.utils.enums.CacheMode

object CacheDatasource {

    private var randomData : ArrayList<Results?> = arrayListOf()
    private var searchData : ArrayList<Results?> = arrayListOf()
    private var authUser : User? = null
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

    fun setAuthUser(user : User?) {
        this.authUser = user
    }

    fun getAuthUser() : User? {
        return this.authUser
    }

    fun clear(mode : CacheMode? = null) {
        if (mode == null && this.mode == CacheMode.RANDOM) {
            this.randomData.clear()
        } else if (mode == null && this.mode == CacheMode.SEARCH) {
            this.searchData.clear()
        } else if (mode != null){
            if (mode == CacheMode.RANDOM) {
                this.randomData.clear()
            } else if (mode == CacheMode.SEARCH) {
                this.searchData.clear()
            }
        }
    }
}