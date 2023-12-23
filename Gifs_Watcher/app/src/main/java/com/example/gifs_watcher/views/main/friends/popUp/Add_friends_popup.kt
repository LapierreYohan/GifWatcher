package com.example.gifs_watcher.views.main.friends.popUp

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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Add_friends_popup(): DialogFragment() {

    private lateinit var title : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.add_friends_pop_up, container, false)

        val titleTV : TextView = view.findViewById(R.id.tv_add_friends_popUp_title)
        val btnLeftTV : Button = view.findViewById(R.id.bt_add_friends_popup_left)
        val btnRightTV : Button = view.findViewById(R.id.bt_add_friends_popup_right)
        val inputText : TextInputLayout = view.findViewById(R.id.add_friend_textinput)

        titleTV.text = title

        btnLeftTV.text = "Annuler"
        btnRightTV.text = "Ajouter"

        btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_primaryContainer)!!)
        btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onPrimaryContainer)!!)

        btnLeftTV.setOnClickListener {
            dismiss()                }
        btnRightTV.setOnClickListener {
            Toast.makeText(context, "Clicked: add ${inputText.getEditText()?.getText()}", Toast.LENGTH_SHORT).show()
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

    fun setTitle(msg : String){
        this.title = msg
    }

}