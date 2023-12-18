package com.example.gifs_watcher.views.main.popUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.gifs_watcher.R
import com.example.gifs_watcher.models.User
import com.example.gifs_watcher.utils.enums.Friend_PopUp_type

class Friends_popup(): DialogFragment() {

    private lateinit var user : User
    private lateinit var type : Friend_PopUp_type
    private lateinit var title : String
    private lateinit var desc : String

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
        btnLeftTV.setOnClickListener {
            Toast.makeText(context, "Clicked: left", Toast.LENGTH_SHORT).show()
        }
        btnRightTV.setOnClickListener {
            Toast.makeText(context, "Clicked: right", Toast.LENGTH_SHORT).show()
        }

        return view

    }

    constructor(user : User, type: Friend_PopUp_type, title: String = "", desc: String = "") : this(){
        this.user = user
        this.type = type
        this.title = title
        this.desc = desc
    }

    fun setTitle(msg : String){
        this.title = msg
    }

    fun setDescription(msg : String){
        this.desc = msg
    }

    fun setType(type : Friend_PopUp_type){
        this.type = type
    }

}