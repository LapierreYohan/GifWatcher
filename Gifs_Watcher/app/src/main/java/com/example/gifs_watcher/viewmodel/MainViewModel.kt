package com.example.gifs_watcher.viewmodel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.repositories.GifRepository
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.views.main.fragments.HomeFragment
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var gifRepo : GifRepository = GifRepository

    private var printedGif: Results? = Results()

    //surement à modifier sans le mutable
    val printedGifLD: MutableLiveData<Results?> = MutableLiveData()
    val listFriend: MutableLiveData<ArrayList<User?>> = MutableLiveData()
    val Friends : LiveData<ArrayList<User?>> = listFriend
    val pendingFriends : MutableLiveData<ArrayList<User?>> = MutableLiveData()
    val pendings : LiveData<ArrayList<User?>> = pendingFriends

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
        val u1 = User("Nekioux3", "pwd", "Axel", "Gailliard", "axel.g@gmail.com", "pas d'idee", "", "")
        val u2 = User("Galstrip3", "pwd2", "Yohan", "Lapierre", "yohan.l@gmail.com", "toujours pas d'idee", "", "")
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
        val u1 = User("Nekioux22", "pwd", "Axel", "Gailliard", "axel.g@gmail.com", "pas d'idee", "", "")
        val u2 = User("Galstrip22", "pwd2", "Yohan", "Lapierre", "yohan.l@gmail.com", "toujours pas d'idee", "", "")
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

}