package com.example.gifs_watcher.database.services

import com.google.firebase.storage.FirebaseStorage

class StorageService {

    private var storage: FirebaseStorage

    constructor(storage: FirebaseStorage) {
        this.storage = storage
    }


}