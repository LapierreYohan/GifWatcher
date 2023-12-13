package com.example.gifs_watcher.database.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthService {

    private var auth: FirebaseAuth

    constructor(auth: FirebaseAuth) {
        this.auth = auth
    }

    suspend fun login(email : String, password : String) : Flow<FirebaseUser?> = flow {
        try {
            val result = suspendCoroutine<FirebaseUser?> { cont ->
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        cont.resume(authResult.user)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            throw e
        }
    }
    suspend fun register(email : String, password : String) : Flow<String?> = flow {
        try {
            val result = suspendCoroutine<String?> { cont ->
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        cont.resume(authResult.user?.uid)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getAuthUser() : FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
}