package com.example.gifs_watcher.repositories

import com.example.gifs_watcher.cache.CacheDatasource
import com.example.gifs_watcher.database.DistantDatabaseDatasource
import com.example.gifs_watcher.models.FriendRequest
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.responses.Response
import com.example.gifs_watcher.utils.enums.FriendError
import com.example.gifs_watcher.utils.enums.FriendRequestType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object UserRepository {
    private var cache : CacheDatasource = CacheDatasource
    private  var database : DistantDatabaseDatasource = DistantDatabaseDatasource

    fun verifyConnectionData(id : String, password : String) : Flow<Response<User>> = flow {

        database.login(id, password).collect {
            if (it.success()) {
                cache.setAuthUser(it.data())
            }
            emit(it)
        }
    }

    fun registerUser(userToInsert : User) : Flow<Response<User>> = flow {

         database.register(userToInsert).collect {
             cache.setAuthUser(it.data())
             emit(it)
         }
    }

    fun isUsernameUsed(username : String) : Flow<Boolean> = flow {
        database.checkUsernameAvailability(username).collect {
            emit(!it)
        }
    }

    fun isMailUsed(mail : String) : Flow<Boolean> = flow {
        database.checkMailAvailability(mail).collect {
            emit(!it)
        }
    }

    fun getUserByUsername(username : String) : Flow<User?> = flow {
        database.getUserByUsername(username).collect {
            emit(it)
        }
    }

    fun isUserIsAuth(dest : User) : Boolean {
        return cache.getAuthUser()?.idUsers == dest.idUsers
    }

    fun sendFriendRequest(user: User) : Flow<Response<FriendRequest>> = flow {

        val auth = cache.getAuthUser()
        if (auth == null) {
            val response = Response<FriendRequest>()
            response.addError(FriendError.MUST_BE_CONNECTED)
            emit(response)
            return@flow
        }

        val request = FriendRequest(
            author = user.username!!,
            displayDestId = null,
            dest = auth.username!!,
            displayDestAvatar = null,
            displayDest = null,
            status = FriendRequestType.ADD_REQUEST.message
        )

        database.sendFriendRequest(user, auth, request).collect {
            emit(it)
        }
    }

    fun isFriendOrPending(user: User) : Flow<Boolean> = flow {
        val auth = cache.getAuthUser()

        database.isFriendOrPending(user, auth!!).collect {
            emit(it)
        }
    }

    fun isWaitingForResponse(user: User) : Flow<Boolean> = flow {
        val auth = cache.getAuthUser()

        database.isWaitingForResponse(user, auth!!).collect {
            emit(it)
        }
    }

    fun addPendingRequest(user: User) : Flow<Boolean> = flow {
        val auth = cache.getAuthUser()

        val request = FriendRequest(
            author = auth!!.username!!,
            displayDestId = null,
            dest = user.username!!,
            displayDestAvatar = null,
            displayDest = null,
            status = FriendRequestType.PENDING_REQUEST.message
        )

        database.addPendingRequest(user, auth, request).collect {
            emit(it)
        }
    }

    fun acceptFriendRequest(user: User) : Flow<Response<FriendRequest>> = flow {
        val auth = cache.getAuthUser()

        val requestAut = FriendRequest(
            author = auth!!.username!!,
            displayDestId = null,
            dest = user.username!!,
            displayDestAvatar = null,
            displayDest = null,
            status = FriendRequestType.ACCEPTED_REQUEST.message
        )

        val requestDest = FriendRequest(
            author = user.username!!,
            displayDestId = null,
            dest = auth.username!!,
            displayDestAvatar = null,
            displayDest = null,
            status = FriendRequestType.ACCEPTED_REQUEST.message
        )

        database.acceptFriendRequest(user, auth, requestAut).collect { response ->
            database.acceptPendingFriendRequest(user, auth, requestDest).collect {
                emit(response)
            }
        }
    }

    fun gerAllPendingRequests() : Flow<List<FriendRequest>> = flow {
        val auth = cache.getAuthUser()
        database.getAllPendingRequests(auth!!).collect { list ->
            val listWithUserData = list.map { request ->
                database.getUserByUsername(request.dest).collect { user ->
                    request.displayDestId = user?.idUsers
                    request.displayDest = user?.displayname
                    request.displayDestAvatar = user?.staticProfilPicture
                }
                request
            }
            emit (listWithUserData)
        }
    }

    fun getAllFriends() : Flow<List<FriendRequest>> = flow {
        val auth = cache.getAuthUser()
        database.getAllFriends(auth!!).collect { list ->
            val listWithUserData = list.map { request ->
                database.getUserByUsername(request.dest).collect { user ->
                    request.displayDestId = user?.idUsers
                    request.displayDest = user?.displayname
                    request.displayDestAvatar = user?.staticProfilPicture
                }
                request
            }
            emit (listWithUserData)
        }
    }

    fun getAllFriendRequests() : Flow<List<FriendRequest>> = flow {
        val auth = cache.getAuthUser()
        database.getAllFriendRequests(auth!!).collect { list ->
            val listWithUserData = list.map { request ->
                database.getUserByUsername(request.dest).collect { user ->
                    request.displayDestId = user?.idUsers
                    request.displayDest = user?.displayname
                    request.displayDestAvatar = user?.staticProfilPicture
                }
                request
            }
            emit (listWithUserData)
        }
    }
}

