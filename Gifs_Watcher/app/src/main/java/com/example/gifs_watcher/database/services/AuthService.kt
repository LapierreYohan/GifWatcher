package com.example.gifs_watcher.database.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthService(private var auth: FirebaseAuth) {

    suspend fun login(email : String, password : String) : Flow<FirebaseUser?> = flow {
        try {
            val result = suspendCoroutine { cont ->
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
            val result = suspendCoroutine { cont ->
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

    suspend fun logout() : Flow<Boolean> = flow {
        try {
            val result = suspendCoroutine { cont ->
                auth.signOut()
                cont.resume(true)
            }

            emit(result)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCurrentUser() : Flow<FirebaseUser?> = flow {
        try {
            emit(auth.currentUser)
        } catch (e: Exception) {
            throw e
        }
    }
}