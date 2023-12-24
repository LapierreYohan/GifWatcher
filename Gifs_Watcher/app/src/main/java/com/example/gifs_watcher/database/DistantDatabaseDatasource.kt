package com.example.gifs_watcher.database

import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.maps.models.GifMap
import com.example.gifs_watcher.utils.enums.UserErrors
import com.example.gifs_watcher.models.responses.UserResponse
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

object DistantDatabaseDatasource {
    suspend fun login(id : String, password : String) : Flow<UserResponse> = flow {

        var response = UserResponse()
        try {
            DistantDatabase.FirestoreService.getUserByUsernameOrEmail(id).collect{
                if (it != null) {
                    DistantDatabase.authService.login(it.mail!!, password).collect{ firebaseUser ->
                        if (firebaseUser != null) {
                            response.addUser(it)
                            emit(response)
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

    suspend fun register(userToInsert : User) : Flow<UserResponse> = flow {

        var response = UserResponse()
        var uid: String? = null

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

                response.addUser(userToInsert)

                DistantDatabase.FirestoreService.createUser(userToInsert).collect{ storeInsert ->
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
        DistantDatabase.FirestoreService.checkUsernameAvailability(username).collect{
            emit(it)
        }
    }

    suspend fun checkMailAvailability(mail : String) : Flow<Boolean> = flow {
        DistantDatabase.FirestoreService.checkEmailAvailability(mail).collect{
            emit(it)
        }
    }

    suspend fun getAuthUser() : FirebaseUser? {
        return DistantDatabase.authService.getAuthUser()
     }

    suspend fun insertGif(gif : GifMap) {
        DistantDatabase.FirestoreService.checkGifAvailability(gif.id!!).collect{
            if (it) {
                DistantDatabase.FirestoreService.insertGif(gif)
            }
        }
    }
    suspend fun likeGif(gif : GifMap, userId: String, userField : String, gifField: String) {

        DistantDatabase.FirestoreService.checkLikedGifAvailable(gif.id!!, userId).collect{ used ->
            if (!used) {
                // On ajoute le gif dans la liste des gifs likés de l'utilisateur
                DistantDatabase.FirestoreService.insertLikedGif(gif, userId, userField).collect{success ->
                    if (success) {
                        // On incrémente le nombre de like du gif dans la base de données
                        DistantDatabase.FirestoreService.incrementGifLike(gif, gifField)
                    }
                }
            }
        }
    }

    suspend fun setAvatarGif(gif : GifMap, userId: String) {
        DistantDatabase.FirestoreService.setAvatarGif(gif, userId)
    }
}