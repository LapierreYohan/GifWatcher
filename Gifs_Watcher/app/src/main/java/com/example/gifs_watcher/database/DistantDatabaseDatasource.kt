package com.example.gifs_watcher.database

import com.example.gifs_watcher.models.FriendRequest
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.maps.models.GifMap
import com.example.gifs_watcher.utils.enums.UserErrors
import com.example.gifs_watcher.models.responses.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber

object DistantDatabaseDatasource {
    suspend fun login(id : String, password : String) : Flow<Response<User>> = flow {

        val response = Response<User>()
        try {
            DistantDatabase.firestoreService.getUserByUsernameOrEmail(id).collect{
                if (it != null) {
                    DistantDatabase.authService.login(it.mail!!, password).collect{ firebaseUser ->
                        if (firebaseUser != null) {
                            DistantDatabase.firestoreService.updateToken(it) .collect {isTokenSet ->
                                if (isTokenSet) {
                                    DistantDatabase.firestoreService.getUserByUsernameOrEmail(id).collect{newUser ->
                                        response.addData(newUser!!)
                                        emit(response)
                                    }
                                }
                            }
                        } else {
                            response.addError(UserErrors.ID_OR_PASSWORD_INVALID)
                            emit(response)
                        }
                    }
                } else {
                    response.addError(UserErrors.ID_OR_PASSWORD_INVALID)
                    emit(response)
                }
            }
        } catch (e: Exception) {
            response.addError(UserErrors.ID_OR_PASSWORD_INVALID)
            emit(response)
        }

    }

    fun getCurrentUser() : Flow<User?> = flow {
        DistantDatabase.authService.getCurrentUser().collect{
            if (it != null) {
                val userId = it.uid
                DistantDatabase.firestoreService.getUserById(userId).collect{ user ->
                    if (user != null) {
                        DistantDatabase.firestoreService.updateToken(user).collect { isTokenSet ->
                            if (isTokenSet) {
                                DistantDatabase.firestoreService.getUserById(userId).collect{ finalUser ->
                                    emit(finalUser)
                                }
                            }
                        }
                    }
                }
            } else {
                emit(null)
            }
        }
    }

    suspend fun register(userToInsert : User) : Flow<Response<User>> = flow {

        val response = Response<User>()
        var uid: String?

        try {
            DistantDatabase.authService.register(userToInsert.mail!!, userToInsert.password!!).collect{
                uid = it
                if (uid != null) {
                    userToInsert.idUsers = uid
                } else {
                    response.addError(UserErrors.UNKNOWN_ERROR)
                    emit(response)
                    return@collect
                }

                response.addData(userToInsert)

                DistantDatabase.firestoreService.createUser(userToInsert).collect{ storeInsert ->
                    if (storeInsert) {
                        emit(response)
                    } else {
                        response.addError(UserErrors.UNKNOWN_ERROR)
                        emit(response)
                    }
                }
            }
        } catch (e: Exception) {
            response.addError(UserErrors.EMAIL_ALREADY_USED)
            emit(response)
        }
    }

    suspend fun checkUsernameAvailability(username : String) : Flow<Boolean> = flow {
        DistantDatabase.firestoreService.checkUsernameAvailability(username).collect{
            emit(it)
        }
    }

    suspend fun checkMailAvailability(mail : String) : Flow<Boolean> = flow {
        DistantDatabase.firestoreService.checkEmailAvailability(mail).collect{
            emit(it)
        }
    }

    suspend fun insertGif(gif : GifMap) {
        DistantDatabase.firestoreService.checkGifAvailability(gif.id!!).collect{
            if (it) {
                DistantDatabase.firestoreService.insertGif(gif)
            }
        }
    }
    suspend fun likeGif(gif : GifMap, userId: String, userField : String, gifField: String) {

        DistantDatabase.firestoreService.checkLikedGifAvailable(gif.id!!, userId).collect{ used ->
            if (!used) {
                // On ajoute le gif dans la liste des gifs likés de l'utilisateur
                DistantDatabase.firestoreService.insertLikedGif(gif, userId, userField).collect{ success ->
                    if (success) {
                        // On incrémente le nombre de like du gif dans la base de données
                        DistantDatabase.firestoreService.incrementGifLike(gif, gifField)
                    }
                }
            }
        }
    }

    suspend fun setAvatarGif(gif : GifMap, userId: String) {
        DistantDatabase.firestoreService.setAvatarGif(gif, userId)
    }

    suspend fun getGifById(gifId : String) : Flow<GifMap?> = flow {
        DistantDatabase.firestoreService.getGifById(gifId).collect{
            emit(it)
        }
    }

    suspend fun getLikedGifs(userId : String, type : String) : Flow<List<GifMap>> = flow {
        DistantDatabase.firestoreService.getLikedGifs(userId, type).collect{
            emit(it)
        }
    }

    suspend fun getUserByUsername(username : String) : Flow<User?> = flow {
        DistantDatabase.firestoreService.getUserByUsername(username).collect{
            emit(it)
        }
    }

    suspend fun sendFriendRequest(user: User, auth : User, request: FriendRequest) : Flow<Response<FriendRequest>> = flow {
        DistantDatabase.firestoreService.sendFriendRequest(user, auth, request).collect{
            emit(it)
        }
    }

    suspend fun isFriendOrPending(user: User, auth : User) : Flow<Boolean> = flow {
        DistantDatabase.firestoreService.isFriendOrPending(user, auth).collect{
            emit(it)
        }
    }

    suspend fun isWaitingForResponse(user: User, auth : User) : Flow<Boolean> = flow {
        DistantDatabase.firestoreService.isWaitingForResponse(user, auth).collect{
            emit(it)
        }
    }

    suspend fun addPendingRequest(user: User, auth : User, request : FriendRequest) : Flow<Boolean> = flow {
        DistantDatabase.firestoreService.addPendingRequest(user, auth, request).collect{
            emit(it)
        }
    }

    suspend fun acceptFriendRequest(user: User, auth : User, request : FriendRequest) : Flow<Response<FriendRequest>> = flow {
        DistantDatabase.firestoreService.acceptFriendRequest(user, auth, request).collect{
            emit(it)
        }
    }

    suspend fun acceptPendingFriendRequest(user: User, auth : User, request : FriendRequest) : Flow<Boolean> = flow {
        DistantDatabase.firestoreService.acceptPendingFriendRequest(user, auth, request).collect{
            emit(it)
        }
    }

    suspend fun getAllPendingRequests(user: User) : Flow<List<FriendRequest>> = flow {
        DistantDatabase.firestoreService.getAllPendingRequests(user).collect{
            Timber.e("getAllPendingRequests: $it")
            emit(it)
        }
    }

    suspend fun getAllFriends(user: User) : Flow<List<FriendRequest>> = flow {
        DistantDatabase.firestoreService.getAllFriends(user).collect{
            Timber.e("getAllFriends: $it")
            emit(it)
        }
    }

    suspend fun getAllFriendRequests(user: User) : Flow<List<FriendRequest>> = flow {
        DistantDatabase.firestoreService.getAllFriendRequests(user).collect{
            Timber.e("getAllFriendRequests: $it")
            emit(it)
        }
    }

    suspend fun removeFriendRequest(user: User, auth : User) : Flow<Boolean> = flow {
        DistantDatabase.firestoreService.removeFriendRequest(user, auth).collect{
            emit(it)
        }
    }

    suspend fun setUpFriendsRequestListener(user: User) : Flow<Boolean> = flow {
        DistantDatabase.firestoreService.setupSnapShotListener(user).collect{
            emit(it)
        }
    }

    suspend fun logout() : Flow<Boolean> = flow {
        DistantDatabase.authService.logout().collect{
            emit(it)
        }
    }
}