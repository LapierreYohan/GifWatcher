package com.example.gifs_watcher.database.services

import com.example.gifs_watcher.models.FriendRequest
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.maps.models.GifMap
import com.example.gifs_watcher.models.responses.Response
import com.example.gifs_watcher.utils.enums.FriendError
import com.example.gifs_watcher.utils.enums.FriendRequestType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreService(private var firestore: FirebaseFirestore) {

    suspend fun createUser(user: User) : Flow<Boolean> = flow {

        try {
            // Pas besoin du password dans le document pour l'instant
            // Si viens a être nécessaire, il faudra hasher le password avec utils/PasswordManager
            user.password = null

            // Image de profil par défaut
            user.profilPicture = "https://media.tenor.com/Gn82P94Ap5wAAAAd/beluga-cat.gif"
            user.lowProfilPicture = "https://tenor.com/fr/view/random-xd-beluga-idk-zzz-bored-gif-25806498"
            user.staticProfilPicture = "https://tenor.com/fr/view/random-xd-beluga-idk-zzz-bored-gif-25806498"

            val result = suspendCoroutine { cont ->
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
            val result = suspendCoroutine { cont ->
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
            val result = suspendCoroutine { cont ->
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
            val result = suspendCoroutine { cont ->
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
            val result = suspendCoroutine { cont ->
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

    suspend fun checkLikedGifAvailable(gifId: String, userId: String): Flow<Boolean> = flow {
        try {
            val fields = listOf("likedGifs", "dislikedGifs", "starredGifs")

            // Vérifier la présence dans chacun des champs
            val result = fields.any { field ->
                suspendCoroutine { cont ->
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

    suspend fun insertLikedGif(gif: GifMap, userId: String, field : String) : Flow<Boolean> = flow {
        try {
            val result = suspendCoroutine { cont ->
                firestore.collection("users")
                    .document(userId)
                    .collection(field)
                    .document(gif.id!!)
                    .set(mapOf("id" to gif.id, "contentDescription" to gif.contentDescription, "preview" to gif.preview, "timestamp" to FieldValue.serverTimestamp()))
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
                "lowProfilPicture" to gif.tinyUrl,
                "staticProfilPicture" to gif.preview
            )

            firestore.collection("users")
                .document(userId)
                .update(updateData)
        } catch (e: Exception) {
            Timber.e("Firestore setAvatarGif error with gif $gif with userId $userId")
            Timber.e(e)
        }
    }

    suspend fun getGifById(gifId: String): Flow<GifMap?> = flow {
        try {
            val result = suspendCoroutine { cont ->
                firestore.collection("gifs")
                    .document(gifId)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        val gif = documentSnapshot.toObject(GifMap::class.java)
                        cont.resume(gif)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore getGifById error with gifId $gifId")
            Timber.e(e)
            emit(null)
        }
    }

    suspend fun getLikedGifs(userId: String, type: String): Flow<List<GifMap>> = flow {
        try {
            val result = suspendCoroutine<List<GifMap>> { cont ->
                firestore.collection("users")
                    .document(userId)
                    .collection(type)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val gifList = mutableListOf<GifMap>()
                        for (document in querySnapshot.documents) {
                            val gif = document.toObject(GifMap::class.java)
                            gif?.let { gifList.add(it) }
                        }
                        cont.resume(gifList)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore getLikedGifs error with userId $userId")
            Timber.e(e)
            emit(emptyList())
        }
    }

    fun getUserByUsername(username: String) : Flow<User?> = flow {
        try {
            val result = suspendCoroutine { cont ->
                firestore.collection("users").whereEqualTo("username", username.lowercase()).get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val user = documents.documents[0].toObject(User::class.java)
                            cont.resume(user)
                        } else {
                            cont.resume(null)
                        }
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore getUserByUsername error with username $username")
            Timber.e(e)
            emit(null)
        }
    }

    suspend fun sendFriendRequest(user: User, auth : User, request: FriendRequest) : Flow<Response<FriendRequest>> = flow {

        val response = Response<FriendRequest>()
        request.timestamp = Timestamp.now()

        try {
            val result = suspendCoroutine { cont ->
                firestore
                    .collection("users")
                    .document(user.idUsers!!)
                    .collection("friendRequests")
                    .document(auth.idUsers!!)
                    .set(request)
                    .addOnSuccessListener {
                        cont.resume(true)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            if (result) {
                response.addData(request)
            } else {
                response.addError(FriendError.UNKNOWN_ERROR)
            }

            emit(response)
        } catch (e: Exception) {
            Timber.e("Firestore sendFriendRequest error with user $user and request $request")
            Timber.e(e)
            response.addError(FriendError.UNKNOWN_ERROR)
            emit(response)
        }
    }

    fun isFriendOrPending(user: User, auth: User) : Flow<Boolean> = flow {
        try {
            val result = suspendCoroutine { cont ->
                firestore
                    .collection("users")
                    .document(auth.idUsers!!)
                    .collection("friendRequests")
                    .whereEqualTo("dest", user.username!!)
                    .whereIn("status", listOf(FriendRequestType.PENDING_REQUEST.message, FriendRequestType.ACCEPTED_REQUEST.message))
                    .get()
                    .addOnSuccessListener { documents ->
                        cont.resume(!documents.isEmpty)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore isFriendOrPending error with user $user and auth $auth")
            Timber.e(e)
            emit(false)
        }
    }

    fun isWaitingForResponse(user: User, auth: User) : Flow<Boolean> = flow {
        try {
            val result = suspendCoroutine { cont ->
                firestore
                    .collection("users")
                    .document(auth.idUsers!!)
                    .collection("friendRequests")
                    .whereEqualTo("dest", user.username!!)
                    .whereEqualTo("status", FriendRequestType.ADD_REQUEST.message)
                    .get()
                    .addOnSuccessListener { documents ->
                        cont.resume(!documents.isEmpty)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore isWaitingForResponse error with user $user and auth $auth")
            Timber.e(e)
            emit(false)
        }
    }

    fun addPendingRequest(user: User, auth: User, request: FriendRequest) : Flow<Boolean> = flow {
        request.timestamp = Timestamp.now()

        try {
            val result = suspendCoroutine { cont ->
                firestore
                    .collection("users")
                    .document(auth.idUsers!!)
                    .collection("friendRequests")
                    .document(user.idUsers!!)
                    .set(request)
                    .addOnSuccessListener {
                        cont.resume(true)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)

        } catch (e: Exception) {
            Timber.e("Firestore addPendingRequest error with user $user and request $request")
            Timber.e(e)
            emit(false)
        }
    }

    fun acceptFriendRequest(user: User, auth: User, request: FriendRequest) : Flow<Response<FriendRequest>> = flow {
        val response = Response<FriendRequest>()
        request.timestamp = Timestamp.now()

        try {
            val result = suspendCoroutine { cont ->
                firestore
                    .collection("users")
                    .document(auth.idUsers!!)
                    .collection("friendRequests")
                    .document(user.idUsers!!)
                    .update(
                        mapOf(
                            "author" to request.author,
                            "dest" to request.dest,
                            "status" to request.status,
                            "timestamp" to request.timestamp
                        )
                    )
                    .addOnSuccessListener {
                        cont.resume(true)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            if (result) {
                response.addData(request)
            } else {
                response.addError(FriendError.UNKNOWN_ERROR)
            }

            emit(response)
        } catch (e: Exception) {
            Timber.e("Firestore acceptFriendRequest error with user $user and request $request")
            Timber.e(e)
            response.addError(FriendError.UNKNOWN_ERROR)
            emit(response)
        }
    }

    fun acceptPendingFriendRequest(user: User, auth: User, request: FriendRequest) : Flow<Boolean> = flow {
        request.timestamp = Timestamp.now()

        try {
            val result = suspendCoroutine { cont ->
                firestore
                    .collection("users")
                    .document(user.idUsers!!)
                    .collection("friendRequests")
                    .document(auth.idUsers!!)
                    .update(
                        mapOf(
                            "author" to request.author,
                            "dest" to request.dest,
                            "status" to request.status,
                            "timestamp" to request.timestamp
                        )
                    )
                    .addOnSuccessListener {
                        cont.resume(true)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore acceptPendingRequest error with user $user and request $request")
            Timber.e(e)
            emit(false)
        }
    }

    fun getAllPendingRequests(user: User) : Flow<List<FriendRequest>> = flow {
        try {
            val result = suspendCoroutine<List<FriendRequest>> { cont ->
                firestore
                    .collection("users")
                    .document(user.idUsers!!)
                    .collection("friendRequests")
                    .whereEqualTo("status", FriendRequestType.PENDING_REQUEST.message)
                    .get()
                    .addOnSuccessListener { documents ->
                        val requests = mutableListOf<FriendRequest>()
                        for (document in documents) {
                            val request = FriendRequest(
                                author = document.getString("author")!!,
                                dest = document.getString("dest")!!,
                                displayDestId = null,
                                displayDest = null,
                                displayDestAvatar = null,
                                status = document.getString("status")!!,
                                timestamp = document.getTimestamp("timestamp")!!
                            )
                            request.let { requests.add(it) }
                        }
                        cont.resume(requests)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore getAllPendingRequests error with user $user")
            Timber.e(e)
            emit(emptyList())
        }
    }

    fun getAllFriends(user: User) : Flow<List<FriendRequest>> = flow {
        try {
            val result = suspendCoroutine<List<FriendRequest>> { cont ->
                firestore
                    .collection("users")
                    .document(user.idUsers!!)
                    .collection("friendRequests")
                    .whereEqualTo("status", FriendRequestType.ACCEPTED_REQUEST.message)
                    .get()
                    .addOnSuccessListener { documents ->
                        val requests = mutableListOf<FriendRequest>()
                        for (document in documents) {
                            val request = FriendRequest(
                                author = document.getString("author")!!,
                                dest = document.getString("dest")!!,
                                displayDestId = null,
                                displayDest = null,
                                displayDestAvatar = null,
                                status = document.getString("status")!!,
                                timestamp = document.getTimestamp("timestamp")!!
                            )
                            request.let { requests.add(it) }
                        }
                        cont.resume(requests)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore getAllFriends error with user $user")
            Timber.e(e)
            emit(emptyList())
        }
    }

    fun getAllFriendRequests(user: User) : Flow<List<FriendRequest>> = flow {
        try {
            val result = suspendCoroutine<List<FriendRequest>> { cont ->
                firestore
                    .collection("users")
                    .document(user.idUsers!!)
                    .collection("friendRequests")
                    .whereEqualTo("status", FriendRequestType.ADD_REQUEST.message)
                    .get()
                    .addOnSuccessListener { documents ->
                        val requests = mutableListOf<FriendRequest>()
                        for (document in documents) {
                            val request = FriendRequest(
                                author = document.getString("author")!!,
                                dest = document.getString("dest")!!,
                                displayDestId = null,
                                displayDest = null,
                                displayDestAvatar = null,
                                status = document.getString("status")!!,
                                timestamp = document.getTimestamp("timestamp")!!
                            )
                            request.let { requests.add(it) }
                        }
                        cont.resume(requests)
                    }
                    .addOnFailureListener { exception ->
                        cont.resumeWithException(exception)
                    }
            }

            emit(result)
        } catch (e: Exception) {
            Timber.e("Firestore getAllFriendRequests error with user $user")
            Timber.e(e)
            emit(emptyList())
        }
    }
}