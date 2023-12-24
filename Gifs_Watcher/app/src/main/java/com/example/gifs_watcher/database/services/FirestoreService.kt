package com.example.gifs_watcher.database.services

import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.maps.models.GifMap
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreService {

    private var firestore: FirebaseFirestore
    constructor(firestore: FirebaseFirestore) {
        this.firestore = firestore
    }

    suspend fun createUser(user: User) : Flow<Boolean> = flow {

        try {
            // Pas besoin du password dans le document pour l'instant
            // Si viens a être nécessaire, il faudra hasher le password avec utils/PasswordManager
            user.password = null

            // Image de profil par défaut
            user.profilPicture = "https://media.tenor.com/Gn82P94Ap5wAAAAd/beluga-cat.gif"

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

    suspend fun checkGifAvailability(gifId: String): Flow<Boolean> = flow {
        try {
            val result = suspendCoroutine<Boolean> { cont ->
                firestore.collection("gifs").document(gifId).get()
                    .addOnSuccessListener { document ->
                        cont.resume(document == null || !document.exists())
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore checkGifAvailability error with ID $gifId")
            Timber.e(e)
            emit(false)
        }
    }

    fun insertGif(gif: GifMap) {
        try {
            firestore.collection("gifs").document(gif.id!!).set(gif)
        } catch (e: Exception) {
            Timber.e("Firestore createUser error with user $gif")
            Timber.e(e)
        }
    }

    fun checkLikedGifAvailable(gifId: String, userId: String): Flow<Boolean> = flow {
        try {
            val fields = listOf("likedGifs", "dislikedGifs", "starredGifs")

            // Vérifier la présence dans chacun des champs
            val result = fields.any { field ->
                suspendCoroutine<Boolean> { cont ->
                    firestore.collection("users")
                        .document(userId)
                        .collection(field)
                        .document(gifId)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            cont.resume(documentSnapshot.exists())
                        }
                        .addOnFailureListener { exception ->
                            cont.resumeWithException(exception)
                        }
                }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore checkLikedGifAvailable error with gifId $gifId and userId $userId")
            Timber.e(e)
            emit(false)
        }
    }

    fun insertLikedGif(gif: GifMap, userId: String, field : String) : Flow<Boolean> = flow {
        try {
            val result = suspendCoroutine<Boolean> { cont ->
                firestore.collection("users")
                    .document(userId)
                    .collection(field)
                    .document(gif.id!!)
                    .set(mapOf("url" to gif.url, "id" to gif.id))
                    .addOnSuccessListener {
                        cont.resume(true)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore insertLikedGif error with gif $gif and userId $userId with field $field")
            Timber.e(e)
            emit(false)
        }
    }

    fun incrementGifLike(gif: GifMap, field : String) {
        try {
            firestore.collection("gifs")
                .document(gif.id!!)
                .update(field, FieldValue.increment(1))
        } catch (e: Exception) {
            Timber.e("Firestore incrementGifLike error with gif $gif with field $field")
            Timber.e(e)
        }
    }

    fun setAvatarGif(gif: GifMap,userId: String) {
        try {
            val updateData = mapOf(
                "profilPicture" to gif.url,
                "lowProfilPicture" to gif.tiny_url
            )

            firestore.collection("users")
                .document(userId)
                .update(updateData)
        } catch (e: Exception) {
            Timber.e("Firestore setAvatarGif error with gif $gif with userId $userId")
            Timber.e(e)
        }
    }
}