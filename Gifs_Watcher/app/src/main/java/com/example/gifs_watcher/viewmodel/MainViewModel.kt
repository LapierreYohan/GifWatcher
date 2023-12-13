package com.example.gifs_watcher.viewmodel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifs_watcher.models.Gif
import com.example.gifs_watcher.models.Media
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.repositories.GifRepository
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.utils.adapters.LikesAdapter
import com.example.gifs_watcher.views.main.fragments.FriendsFragment
import com.example.gifs_watcher.views.main.fragments.HomeFragment
import com.example.gifs_watcher.views.splashscreen.modals.LoginModal.requireContext
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var gifRepo : GifRepository = GifRepository

    private var printedGif: Results? = Results()

    //surement à modifier sans le mutable
    val printedGifLD: MutableLiveData<Results?> = MutableLiveData()
    val printedGifA : LiveData<Results?> = printedGifLD
    val listFriend: MutableLiveData<ArrayList<User?>> = MutableLiveData()
    val Friends : LiveData<ArrayList<User?>> = listFriend
    val pendingFriends : MutableLiveData<ArrayList<User?>> = MutableLiveData()
    val pendings : LiveData<ArrayList<User?>> = pendingFriends
    val sentFriends : MutableLiveData<ArrayList<User?>> = MutableLiveData()
    val sents : LiveData<ArrayList<User?>> = sentFriends
    val likesGif : MutableLiveData<ArrayList<Results?>> = MutableLiveData()
    val likes : LiveData<ArrayList<Results?>> = likesGif

    enum class View{
        HOME, FRIENDS, PROFIL
    }

    fun getRandomGif(context: Context) {

        viewModelScope.launch {
            gifRepo.getRandomGif(context)
                .collect {
                    it?.let { tenorData ->
                        printedGif = tenorData
                        printedGifLD.postValue(tenorData)
                    }
                }
        }
    }
    fun getText(view: View) : String {
        return "This is " + view.toString()
    }

    fun getFriendsUsers() {
        val u1 = User("Nekioux3", "pwd", "Axel", "Gailliard", "axel.g@gmail.com", "pas d'idee", "", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
        val u2 = User("Galstrip3", "pwd2", "Yohan", "Lapierre", "yohan.l@gmail.com", "toujours pas d'idee", "", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
        addFriendsUser(u1)
        addFriendsUser(u2)
        addFriendsUser(u1)
        addFriendsUser(u2)
        addFriendsUser(u1)
        addFriendsUser(u2)
        addFriendsUser(u1)
        addFriendsUser(u2)
        viewModelScope.launch {
            listFriend.postValue(listFriend.value)
        }
    }

    fun addFriendsUser(user : User) {
        listFriend.value?.add(user)
    }

    fun getPendingFriendsUsers() {
        val u1 = User("Nekioux22", "pwd", "Axel", "Gailliard", "axel.g@gmail.com", "pas d'idee", "", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
        val u2 = User("Galstrip22", "pwd2", "Yohan", "Lapierre", "yohan.l@gmail.com", "toujours pas d'idee", "", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
        addPendingFriendsUser(u1)
        addPendingFriendsUser(u2)
        addPendingFriendsUser(u1)
        addPendingFriendsUser(u2)
        addPendingFriendsUser(u1)
        addPendingFriendsUser(u2)
        addPendingFriendsUser(u1)
        addPendingFriendsUser(u2)
        viewModelScope.launch {
            pendingFriends.postValue(pendingFriends.value)
        }
    }

    fun addPendingFriendsUser(user : User) {
        pendingFriends.value?.add(user)
    }

    fun getSentFriendsUsers() {
        val u1 = User("Nekioux1", "pwd", "Axel", "Gailliard", "axel.g@gmail.com", "pas d'idee", "", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
        val u2 = User("Galstrip1", "pwd2", "Yohan", "Lapierre", "yohan.l@gmail.com", "toujours pas d'idee", "", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
        addSentFriendsUser(u1)
        addSentFriendsUser(u2)
        addSentFriendsUser(u1)
        addSentFriendsUser(u2)
        addSentFriendsUser(u1)
        addSentFriendsUser(u2)
        addSentFriendsUser(u1)
        addSentFriendsUser(u2)
        viewModelScope.launch {
            sentFriends.postValue(sentFriends.value)
        }
    }

    fun addSentFriendsUser(user : User) {
        sentFriends.value?.add(user)
    }

    fun getLikesProfil(context: Context) {
        viewModelScope.launch {
            gifRepo.getRandomGif(context)
                .collect {
                    addLikes(it)
                }
        }


        val d1 = ArrayList<Int>()
        d1.add(1)
        d1.add(1)
        val g1 = Gif("1","https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif", d1, 1.1, 1)
        val m1 = Media(g1, g1)
        val lm1 = ArrayList<Media>()
        lm1.add(m1)
        val l1 = Results("1", "1", "1", lm1, "1", 1.1)
        val l2 = Results("2", "2", "2", lm1, "1", 1.2)
        addLikes(l2)
        addLikes(l1)
        viewModelScope.launch {
            likesGif.postValue(likesGif.value)
        }

    }

    fun addLikes(like : Results?) {
        likesGif.value?.add(like)
    }

    fun getProfil() : User {
        val u1 = User("Nekioux1", "pwd", "Axel", "Gailliard", "axel.g@gmail.com", "pas d'idee", "", "https://media1.tenor.com/m/z2FuWLCu0MsAAAAC/merry-christmas.gif")
        return u1
    }


}