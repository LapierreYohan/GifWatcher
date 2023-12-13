package com.example.gifs_watcher.database.services

import com.example.gifs_watcher.models.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreService {

    private lateinit var firestore: FirebaseFirestore
    constructor(firestore: FirebaseFirestore) {
        this.firestore = firestore
    }

    suspend fun createUser(user: User) : Flow<Boolean> = flow {

        try {
            // Pas besoin du password dans le document pour l'instant
            // Si viens a être nécessaire, il faudra hasher le password avec utils/PasswordManager
            user.password = null

            val result = suspendCoroutine<Boolean> { cont ->
                firestore.collection("users").document(user.idUsers!!).set(user)
                    .addOnSuccessListener {
                        cont.resume(true)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore createUser error with user $user")
            Timber.e(e)
            emit(false)
        }
    }

    suspend fun checkUsernameAvailability(username: String): Flow<Boolean> = flow {

        try {
            val result = suspendCoroutine<Boolean> { cont ->
                firestore.collection("users").whereEqualTo("username", username.lowercase()).get()
                    .addOnSuccessListener { documents ->
                        cont.resume(documents.isEmpty)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore checkUsernameAvailability error with username $username")
            Timber.e(e)
            emit(false)
        }
    }

    suspend fun checkEmailAvailability(email: String): Flow<Boolean> = flow {

        try {
            val result = suspendCoroutine<Boolean> { cont ->
                firestore.collection("users").whereEqualTo("mail", email.lowercase()).get()
                    .addOnSuccessListener { documents ->
                        cont.resume(documents.isEmpty)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore checkEmailAvailability error with email $email")
            Timber.e(e)
            emit(false)
        }
    }

    suspend fun getUserByUsernameOrEmail(id: String): Flow<User?> = flow {
        try {
            val lowerCaseId = id.lowercase()
            val result = suspendCoroutine<User?> { cont ->
                // Vérifier si l'id correspond à un nom d'utilisateur
                firestore.collection("users").whereEqualTo("username", lowerCaseId).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val user = documents.documents[0].toObject(User::class.java)
                            cont.resume(user)
                        } else {
                            // Si l'id ne correspond pas à un nom d'utilisateur, vérifier par e-mail
                            firestore.collection("users").whereEqualTo("mail", lowerCaseId).get()
                                .addOnSuccessListener { emailDocuments ->
                                    if (!emailDocuments.isEmpty) {
                                        val user =
                                            emailDocuments.documents[0].toObject(User::class.java)
                                        cont.resume(user)
                                    } else {
                                        cont.resume(null)
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    cont.resumeWithException(exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            emit(null)
        }
    }
}