package com.example.gifs_watcher.viewmodel


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.repositories.GifRepository
import com.example.gifs_watcher.models.User
import kotlinx.coroutines.launch
import kotlin.math.log

class MainViewModel : ViewModel() {

    private var gifRepo : GifRepository = GifRepository

    private var printedGif: Results? = Results()

    //surement à modifier sans le mutable
    val printedGifLD: MutableLiveData<Results?> = MutableLiveData()
    val listFriend: MutableLiveData<ArrayList<User?>> = MutableLiveData()
    val Friends : LiveData<ArrayList<User?>> = listFriend

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

    fun getUsers() {
        val u1 = User("Nekioux", "pwd", "Axel", "Gailliard", "axel.g@gmail.com", "pas d'idee", "", "")
        val u2 = User("Galstrip", "pwd2", "Yohan", "Lapierre", "yohan.l@gmail.com", "toujours pas d'idee", "", "")
        addUser(u1)
        addUser(u2)
        viewModelScope.launch {
            listFriend.postValue(listFriend.value)
        }
    }

    fun addUser(user : User) {
        listFriend.value?.add(user)
    }
}