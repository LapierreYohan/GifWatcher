package com.example.gifs_watcher.views.main.friends.popUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gifs_watcher.R
import com.example.gifs_watcher.models.FriendRequest
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.utils.enums.FriendPopUpType
import retrofit2.Response

class FriendsPopup(): DialogFragment() {

    private lateinit var user : FriendRequest
    private lateinit var type : FriendPopUpType
    private lateinit var title : String
    private lateinit var desc : String

    private val _acceptFriend : MutableLiveData<String> = MutableLiveData()
    val acceptFriend : LiveData<String> = _acceptFriend

    private val _denyFriend : MutableLiveData<String> = MutableLiveData()
    val denyFriend : LiveData<String> = _denyFriend

    private val _deleteFriend : MutableLiveData<String> = MutableLiveData()
    val deleteFriend : LiveData<String> = _deleteFriend

    private val _cancelRequest : MutableLiveData<String> = MutableLiveData()
    val cancelRequest : LiveData<String> = _cancelRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.friends_pop_up, container, false)

        val titleTV : TextView = view.findViewById(R.id.tv_popUp_title)
        val descTV : TextView = view.findViewById(R.id.tv_popUp_description)
        val btnLeftTV : Button = view.findViewById(R.id.bt_friends_popup_left)
        val btnRightTV : Button = view.findViewById(R.id.bt_friends_popup_right)

        titleTV.text = title
        descTV.text = desc

        when(type) {
            FriendPopUpType.ACCEPT_PENDING -> {
                btnLeftTV.text = getString(R.string.cancel)
                btnRightTV.text = getString(R.string.accept)

                btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_primaryContainer)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onPrimaryContainer)!!)

                btnLeftTV.setOnClickListener {
                    dismiss()                }
                btnRightTV.setOnClickListener {
                    _acceptFriend.postValue(user.dest)
                }
            }
            FriendPopUpType.REFUSE_PENDING -> {
                btnLeftTV.text = getString(R.string.cancel)
                btnRightTV.text = getString(R.string.reject)

                btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_errorContainer)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onErrorContainer)!!)

                btnLeftTV.setOnClickListener {
                    dismiss()                }
                btnRightTV.setOnClickListener {
                    _denyFriend.postValue(user.dest)
                }
            }
            FriendPopUpType.DELETE_FRIEND -> {
                btnLeftTV.text = getString(R.string.cancel)
                btnRightTV.text = getString(R.string.delete)

                btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_errorContainer)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onErrorContainer)!!)

                btnLeftTV.setOnClickListener {
                    dismiss()                }
                btnRightTV.setOnClickListener {
                    _deleteFriend.postValue(user.dest)
                }
            }
            FriendPopUpType.DELETE_SENT -> {
                btnLeftTV.text = getString(R.string.cancel)
                btnRightTV.text = getString(R.string.remove)

                btnRightTV.setBackgroundColor(context?.getColor(R.color.orange)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.white)!!)

                btnLeftTV.setOnClickListener {
                    dismiss()
                }
                btnRightTV.setOnClickListener {
                    _cancelRequest.postValue(user.dest)
                }
            }
            FriendPopUpType.ADD_FRIEND -> {
                btnLeftTV.text = getString(R.string.cancel)
                btnRightTV.text = getString(R.string.add)

                btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_primaryContainer)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onPrimaryContainer)!!)

                btnLeftTV.setOnClickListener {
                    Toast.makeText(context, "Clicked: Annuler", Toast.LENGTH_SHORT).show()
                }
                btnRightTV.setOnClickListener {
                    Toast.makeText(context, "Clicked: Ajouter", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view

    }

    constructor(user : FriendRequest, type: FriendPopUpType, title: String = "", desc: String = "") : this(){
        this.user = user
        this.type = type
        this.title = title
        this.desc = desc
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.TransparentDialog)
    }
}