package com.example.gifs_watcher.views.main


import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gifs_watcher.R
import com.example.gifs_watcher.cache.CacheDatasource
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.models.responses.Response
import com.example.gifs_watcher.repositories.GifRepository
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.models.maps.GifMapper
import com.example.gifs_watcher.utils.enums.GifErrors
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainViewModel : ViewModel() {

    private var gifRepo : GifRepository = GifRepository

    private var printedGif: Results? = Results()

    private val u1 = User("id1", "nek1oux", "Nek1oux", "pwd1", "user1@example.com", "Bio de l'utilisateur 1", "03/12/1990", "https://media.tenor.com/tZyRsMZ9CTsAAAAC/luffy-strawhat.gif")
    private val u2 = User("id2", "galtrips", "Galtrips", "pwd2", "user2@example.com", "Bio de l'utilisateur 2", "15/07/1985", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
    private val u3 = User("id3", "aizen", "Aizen", "pwd3", "user3@example.com", "Bio de l'utilisateur 3", "20/04/1993", "https://media.tenor.com/BS2I6El-QPEAAAAC/uu.gif")
    private val u4 = User("id4", "elix", "Elix", "pwd4", "user4@example.com", "Bio de l'utilisateur 4", "07/09/1988", "https://media.tenor.com/SqPSasr3nX8AAAAC/balkan-s%C3%A9rie-h.gif")
    private val u5 = User("id5", "nekfeu", "Nekfeu", "pwd5", "user5@example.com", "Bio de l'utilisateur 5", "11/02/1996", "https://media.tenor.com/46vRSIuTrQ4AAAAC/hey-big.gif")
    private val u6 = User("id6", "oli", "Oli", "pwd6", "user6@example.com", "Bio de l'utilisateur 6", "25/11/1980", "https://media.tenor.com/eUGQLXkRKGMAAAAC/olibara-oli-bara.gif")
    private val u7 = User("id7", "urahara", "Urahara", "pwd7", "user7@example.com", "Bio de l'utilisateur 7", "15/09/1992", "https://media.tenor.com/dnBTBLJW3n0AAAAC/kisuke.gif")
    private val u8 = User("id8", "discord", "Discord", "pwd8", "user8@example.com", "Bio de l'utilisateur 8", "02/06/1987", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
    private val u9 = User("id9", "modo", "Modo", "pwd9", "user9@example.com", "Bio de l'utilisateur 9", "18/03/1995", "https://media.tenor.com/wz4mA2-SG8cAAAAC/luffy-one-piece.gif")
    private val u10 = User("id10", "beluga", "Beluga", "pwd10", "user10@example.com", "Bio de l'utilisateur 10", "30/08/1983", "https://media.tenor.com/2oug0ZIpqJYAAAAC/beluga-kissing.gif")
    private val u11 = User("id11", "pedro", "Pedro", "pwd11", "user11@example.com", "Bio de l'utilisateur 11", "12/07/1998", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
    private val u12 = User("id12", "alex", "Alex", "pwd12", "user12@example.com", "Bio de l'utilisateur 12", "05/04/1982", "https://media.tenor.com/wz4mA2-SG8cAAAAC/luffy-one-piece.gif")
    private val u13 = User("id13", "gaetan", "Gaetan", "pwd13", "user13@example.com", "Bio de l'utilisateur 13", "22/11/1997", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")

    private val gifResponseLD: MutableLiveData<Response<Results>> = MutableLiveData()
    val gifResponse : LiveData<Response<Results>> = gifResponseLD

    private val sharedGifLD: MutableLiveData<Results?> = MutableLiveData()
    val sharedGif : LiveData<Results?> = sharedGifLD

    private val printedGifLD: MutableLiveData<Results?> = MutableLiveData()
    val printedGifA : LiveData<Results?> = printedGifLD

    private val listFriend: MutableLiveData<ArrayList<User?>> = MutableLiveData(arrayListOf())
    val friends : LiveData<ArrayList<User?>> = listFriend

    private val pendingFriends : MutableLiveData<ArrayList<User?>> = MutableLiveData(arrayListOf())
    val pendings : LiveData<ArrayList<User?>> = pendingFriends

    private val sentFriends : MutableLiveData<ArrayList<User?>> = MutableLiveData(arrayListOf())
    val sents : LiveData<ArrayList<User?>> = sentFriends

    private val likesGif : MutableLiveData<ArrayList<Results?>> = MutableLiveData(arrayListOf())
    val likes : LiveData<ArrayList<Results?>> = likesGif

    var seeGifTraitement : Boolean = false

    fun getRandomGif(context: Context, theme: String = "") {
        viewModelScope.launch {
            gifRepo.getRandomGif(context, theme)
                .collect {
                    it?.let { tenorData ->
                        printedGif = tenorData
                        printedGifLD.postValue(tenorData)
                    }
                }
        }
    }

    fun likeGif() {
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
        addFriendsUser(u1)
        addFriendsUser(u2)
        addFriendsUser(u3)
        addFriendsUser(u4)

        viewModelScope.launch {
            listFriend.postValue(listFriend.value)
        }
    }

    fun addFriendsUser(user : User) {
        listFriend.value?.add(user)
    }

    fun getPendingFriendsUsers() {

        addPendingFriendsUser(u5)
        addPendingFriendsUser(u6)
        addPendingFriendsUser(u7)
        addPendingFriendsUser(u8)

        viewModelScope.launch {
            pendingFriends.postValue(pendingFriends.value)
        }
    }

    fun addPendingFriendsUser(user : User) {
        pendingFriends.value?.add(user)
    }

    fun getSentFriendsUsers() {

        addSentFriendsUser(u9)
        addSentFriendsUser(u10)
        addSentFriendsUser(u11)
        addSentFriendsUser(u12)
        addSentFriendsUser(u13)

        viewModelScope.launch {
            sentFriends.postValue(sentFriends.value)
        }
    }

    fun addSentFriendsUser(user : User) {
        sentFriends.value?.add(user)
    }

    fun getLikesProfil(context: Context) {
        likesGif.value?.clear()
        viewModelScope.launch {
            gifRepo.getRandomGif(context)
                .collect {
                    addLikes(it)
                }
        }
    }

    fun addLikes(like : Results?) {
        likesGif.value?.add(like)
    }

    fun getProfil() : User? {
        return CacheDatasource.getAuthUser()
    }

    fun downloadGif(context: Context, gif: Results?) {

        var gifData = GifMapper.map(gif!!)

        val maxCharCount = 30

        val gifName: String = if ((gifData.content_description?.length ?: 0) > maxCharCount) {
            gifData.content_description?.substring(0, maxCharCount) + ".gif"
        } else {
            gifData.content_description + ".gif"
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

        var gifData = GifMapper.map(gif!!)

        gif.let {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(gifData.content_description, gifData.url)
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

    fun downloadQrCode(context: Context, bitmap: Bitmap, name : String = "qrcode_image.png") {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, name + ".png")

        try {
            FileOutputStream(image).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
            }

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = Uri.fromFile(image)
            context.sendBroadcast(mediaScanIntent)

            Toast.makeText(context, "Image enregistrée dans la galerie", Toast.LENGTH_SHORT).show()
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

        var response = Response<Results>()

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
                if (it != null) {
                    Log.d("Gif", it.toString())
                    printedGif = it
                    printedGifLD.postValue(it)
                    response.addData(it)
                    gifResponseLD.postValue(response)
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
}