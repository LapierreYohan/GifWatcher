package com.example.gifs_watcher.views.main.friends.popUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gifs_watcher.R
import com.example.gifs_watcher.models.FriendRequest
import com.example.gifs_watcher.models.responses.Response
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddFriendsPopup(): DialogFragment() {

    private lateinit var title : String

    private val _addedFriend : MutableLiveData<String> = MutableLiveData()
    val addedFriend : LiveData<String> = _addedFriend

    val addedFriendResponse : MutableLiveData<Response<FriendRequest>> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.add_friends_pop_up, container, false)

        val titleTV : TextView = view.findViewById(R.id.tv_add_friends_popUp_title)
        val btnLeftTV : Button = view.findViewById(R.id.bt_add_friends_popup_left)
        val btnRightTV : Button = view.findViewById(R.id.bt_add_friends_popup_right)
        val inputTextLayout : TextInputLayout = view.findViewById(R.id.add_friend_textinput)
        val inputText : TextInputEditText = view.findViewById(R.id.login_identifiant_textinput)

        titleTV.text = title

        inputTextLayout.error = null
        inputText.error = null
        inputText.setText("")

        btnLeftTV.text = getString(R.string.cancel)
        btnRightTV.text = getString(R.string.add)

        btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_primaryContainer)!!)
        btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onPrimaryContainer)!!)

        btnLeftTV.setOnClickListener {
            dismiss()
        }

        btnRightTV.setOnClickListener {
            val contentInputText = inputTextLayout.editText?.text.toString().trim().lowercase()

            if(contentInputText.isEmpty()){
                inputTextLayout.error = "Please enter a username."
                inputText.error = "Please enter a username."
            } else {

                addedFriendResponse.observe(viewLifecycleOwner) { response ->
                    response?.let {
                        if (it.success()) {
                            dismiss()
                        } else {
                            inputTextLayout.error = it.error()[0]?.message
                            inputText.error = it.error()[0]?.message
                        }
                    }
                }

                _addedFriend.postValue(contentInputText)
            }
        }

        return view

    }

    constructor(title: String = "") : this(){
        this.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.TransparentDialog)
    }
}