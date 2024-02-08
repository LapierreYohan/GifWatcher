package com.example.gifs_watcher.views.main


import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gifs_watcher.R
import com.example.gifs_watcher.cache.CacheDatasource
import com.example.gifs_watcher.models.FriendRequest
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.responses.Response
import com.example.gifs_watcher.repositories.GifRepository
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.maps.GifMapper
import com.example.gifs_watcher.models.maps.models.GifMap
import com.example.gifs_watcher.repositories.UserRepository
import com.example.gifs_watcher.utils.enums.FriendError
import com.example.gifs_watcher.utils.enums.GifErrors
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class MainViewModel : ViewModel() {

    private var gifRepo : GifRepository = GifRepository
    private var userRepo : UserRepository = UserRepository

    private var printedGif: Results? = Results()

    private val _addedFriendResponse : MutableLiveData<Response<FriendRequest>> = MutableLiveData()
    val addedFriendResponse : LiveData<Response<FriendRequest>> = _addedFriendResponse

    private val gifResponseLD: MutableLiveData<Response<Results>> = MutableLiveData()
    val gifResponse : LiveData<Response<Results>> = gifResponseLD

    private val sharedGifLD: MutableLiveData<Results?> = MutableLiveData()
    val sharedGif : LiveData<Results?> = sharedGifLD

    private val printedGifLD: MutableLiveData<Results?> = MutableLiveData()
    val printedGifA : LiveData<Results?> = printedGifLD

    private val listFriend: MutableLiveData<ArrayList<FriendRequest>> = MutableLiveData(arrayListOf())
    val friends : LiveData<ArrayList<FriendRequest>> = listFriend

    private val pendingFriends : MutableLiveData<ArrayList<FriendRequest>> = MutableLiveData(arrayListOf())
    val pendings : LiveData<ArrayList<FriendRequest>> = pendingFriends

    private val sentFriends : MutableLiveData<ArrayList<FriendRequest>> = MutableLiveData(arrayListOf())
    val sents : LiveData<ArrayList<FriendRequest>> = sentFriends

    private val likesGif : MutableLiveData<ArrayList<GifMap?>> = MutableLiveData(arrayListOf())
    val likes : LiveData<ArrayList<GifMap?>> = likesGif

    private val starsGif : MutableLiveData<ArrayList<GifMap?>> = MutableLiveData(arrayListOf())
    val stars : LiveData<ArrayList<GifMap?>> = starsGif

    private val sharesGif : MutableLiveData<ArrayList<GifMap?>> = MutableLiveData(arrayListOf())
    val shares : LiveData<ArrayList<GifMap?>> = sharesGif

    var seeGifTraitement : Boolean = false

    fun getRandomGif(theme: String = "") {
        viewModelScope.launch {
            gifRepo.getRandomGif(theme)
                .collect {
                    it?.let { tenorData ->
                        printedGif = tenorData
                        printedGifLD.postValue(tenorData)
                    }
                }
        }
    }

    fun likeGif() {
        if (printedGif == null) {
            return
        }

        viewModelScope.launch {
            gifRepo.likeGif(
                GifMapper.map(printedGif!!),
                "like"
            )
        }
    }

    fun dislikeGif() {
        viewModelScope.launch {
            gifRepo.likeGif(
                GifMapper.map(printedGif!!),
                "dislike"
            )
        }
    }

    fun starGif() {
        viewModelScope.launch {
            gifRepo.likeGif(
                GifMapper.map(printedGif!!),
                "star"
            )
        }
    }

    fun getFriendsUsers() {
        Timber.e("getFriendsUsers")
        viewModelScope.launch {
            userRepo.getAllFriends().collect {
                listFriend.postValue(ArrayList(it))
            }
        }
    }

    fun getPendingFriendsUsers() {
        Timber.e("getPendingFriendsUsers")
        viewModelScope.launch {
            userRepo.getAllFriendRequests().collect {
                pendingFriends.postValue(ArrayList(it))
            }
        }
    }

    fun getSentFriendsUsers() {
        Timber.e("getSentFriendsUsers")
        viewModelScope.launch {
            userRepo.gerAllPendingRequests().collect {
                sentFriends.postValue(ArrayList(it))
            }
        }
    }

    fun getLikesProfil() {
        likesGif.value?.clear()
        viewModelScope.launch {
            gifRepo.getLikedGifs("likedGifs").collect {
                likesGif.value?.clear()
                likesGif.value?.addAll(it)
                likesGif.postValue(likesGif.value)
            }
        }
    }

    fun getStarsProfil() {
        starsGif.value?.clear()
        viewModelScope.launch {
            gifRepo.getLikedGifs("starredGifs").collect {
                starsGif.value?.clear()
                starsGif.value?.addAll(it)
                starsGif.postValue(starsGif.value)
            }
        }
    }

    fun getSharesProfil() {
        sharesGif.value?.clear()
        viewModelScope.launch {
            gifRepo.getLikedGifs("sharedGifs").collect {
                sharesGif.value?.clear()
                sharesGif.value?.addAll(it)
                sharesGif.postValue(sharesGif.value)
            }
        }
    }

    fun getProfil() : User? {
        return CacheDatasource.getAuthUser()
    }

    fun downloadGif(context: Context, gif: Results?) {

        val gifData = GifMapper.map(gif!!) ?: return

        val maxCharCount = 30

        val gifName: String = if ((gifData.contentDescription?.length ?: 0) > maxCharCount) {
            gifData.contentDescription?.substring(0, maxCharCount) + ".gif"
        } else {
            gifData.contentDescription + ".gif"
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val uri = Uri.parse(gifData.url)

        val request = DownloadManager.Request(uri)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, gifName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        downloadManager.enqueue(request)
    }

    fun shareGif(gif: Results, navController: NavController) {
        viewModelScope.launch {
            gifRepo.likeGif(GifMapper.map(gif), "share")
            gifRepo.setSharedGif(gif).collect {
                if (it) {
                    navController.navigate(R.id.navigation_share_gif)
                }
            }
        }
    }

    fun getShareGif() {
        viewModelScope.launch {
            gifRepo.getSharedGif().collect {
                if (it != null) {
                    sharedGifLD.postValue(it)
                }
            }
        }
    }

    fun copyLinkGif(context: Context, gif: Results?) {

        val gifData = GifMapper.map(gif!!) ?: return

        gif.let {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(gifData.contentDescription, gifData.url)
            clipboardManager.setPrimaryClip(clipData)
        }
    }

    fun setAvatarGif(gif: Results) {
        viewModelScope.launch {
            gifRepo.setAvatarGif(
                GifMapper.map(gif)
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun downloadQrCode(context: Context, bitmap: Bitmap, name: String = "qrcode_image.png") {
        try {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            val imageUri = resolver.insert(collection, values)

            if (imageUri != null) {
                resolver.openOutputStream(imageUri).use { out ->
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }
                    out?.flush()
                }

                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(imageUri, values, null, null)

                Toast.makeText(context, "Image enregistrée dans la galerie", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erreur lors de l'enregistrement de l'image", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Erreur lors de l'enregistrement de l'image", Toast.LENGTH_SHORT).show()
        }
    }


    fun copyIdGif(context: Context, id : String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("id", id)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "ID copié", Toast.LENGTH_SHORT).show()
    }

    fun seeGif(gifId : String, navController: NavController) {
        seeGifTraitement = true

        val response = Response<Results>()

        if (gifId.isEmpty()) {
            response.addError(GifErrors.GIF_IS_EMPTY)
            gifResponseLD.postValue(response)
            seeGifTraitement = false
            return
        }

        if (!gifId.matches(Regex("[0-9]+"))) {
            response.addError(GifErrors.GIF_CONTAINS_EXCEPTED_CHARACTERS)
            gifResponseLD.postValue(response)
            seeGifTraitement = false
            return
        }

        viewModelScope.launch {
            gifRepo.seeGif(gifId, printedGif).collect {
                Timber.d("Gif : $it")
                if (it != null) {
                    printedGif = it
                    printedGifLD.postValue(it)
                    response.addData(it)
                    response.error().clear()
                    gifResponseLD.postValue(response)
                    navController.popBackStack(R.id.navigation_home, false)
                    navController.navigate(R.id.navigation_home)
                    seeGifTraitement = false
                } else {
                    response.addError(GifErrors.GIF_NOT_FOUND)
                    gifResponseLD.postValue(response)
                    seeGifTraitement = false
                }
            }
        }
    }

    fun requestFriend(username : String) {

        val response = Response<FriendRequest>()

        viewModelScope.launch {

            // Trouve le destinataire par son username
            userRepo.getUserByUsername(username).collect { dest ->
                if (dest != null) {

                    // Si le destinataire est l'utilisateur authentifié
                    if (userRepo.isUserIsAuth(dest)) {
                        Timber.e("Can't add self")
                        response.addError(FriendError.CANT_ADD_SELF)
                        _addedFriendResponse.postValue(response)
                        return@collect
                    }

                    //Alwrady friends or pending request -> Error
                    userRepo.isFriendOrPending(dest).collect { isFriendOrPending ->
                        if (isFriendOrPending) {
                            Timber.e("Already friend or pending")
                            response.addError(FriendError.ALREADY_FRIEND_OR_PENDING)
                            _addedFriendResponse.postValue(response)
                            return@collect
                        } else {
                            Timber.e("Not friend or pending")
                            checkPendingRequest(dest)
                        }
                    }
                } else {
                    Timber.d("User not found")
                    response.addError(FriendError.USER_NOT_EXIST)
                    _addedFriendResponse.postValue(response)
                }
            }
        }
    }

    private suspend fun checkPendingRequest(dest : User) {
        //Dest have pending request for auth user
        userRepo.isWaitingForResponse(dest).collect { isWaitingForResponse ->
            Timber.e("isWaitingForResponse : $isWaitingForResponse")
            if (isWaitingForResponse) {
                //If true -> accept request
                /// change pending for dest
                acceptRequest(dest)
            } else {
                //else -> Send request to user
                //Add user to pending list
                sendPendingRequest(dest)
            }
        }
    }

    private suspend fun acceptRequest(dest : User) {
        //Change pending for auth user
        //Add user to friends list
        userRepo.acceptFriendRequest(dest).collect { request ->
            //Notify user
            notifyUser(dest)
            _addedFriendResponse.postValue(request)
        }
    }

    private suspend fun sendPendingRequest(dest : User) {
        //else -> Send request to user
        userRepo.sendFriendRequest(dest).collect { request ->

            //Add user to pending list
            userRepo.addPendingRequest(dest).collect { success ->
                if (success) {
                    //Notify user
                    notifyUser(dest)
                    _addedFriendResponse.postValue(request)
                }
            }
        }
    }

    private suspend fun notifyUser(dest : User) {
        this.getFriendsUsers()
        this.getPendingFriendsUsers()
        this.getSentFriendsUsers()
    }
}