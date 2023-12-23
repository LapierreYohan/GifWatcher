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

        when(type) {
            Friend_PopUp_type.accept_pending -> {
                btnLeftTV.text = "Annuler"
                btnRightTV.text = "Accepter"

                btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_primaryContainer)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onPrimaryContainer)!!)

                btnLeftTV.setOnClickListener {
                    dismiss()                }
                btnRightTV.setOnClickListener {
                    Toast.makeText(context, "Clicked: Accepter", Toast.LENGTH_SHORT).show()
                }
            }
            Friend_PopUp_type.refuse_pending -> {
                btnLeftTV.text = "Annuler"
                btnRightTV.text = "Rejeter"

                btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_errorContainer)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onErrorContainer)!!)

                btnLeftTV.setOnClickListener {
                    dismiss()                }
                btnRightTV.setOnClickListener {
                    Toast.makeText(context, "Clicked: Rejeter", Toast.LENGTH_SHORT).show()
                }
            }
            Friend_PopUp_type.delete_friend -> {
                btnLeftTV.text = "Annuler"
                btnRightTV.text = "Supprimer"

                btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_errorContainer)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onErrorContainer)!!)

                btnLeftTV.setOnClickListener {
                    dismiss()                }
                btnRightTV.setOnClickListener {
                    Toast.makeText(context, "Clicked: Supprimer", Toast.LENGTH_SHORT).show()
                }
            }
            Friend_PopUp_type.delete_sent -> {
                btnLeftTV.text = "Annuler"
                btnRightTV.text = "Supprimer"

                btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_errorContainer)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onErrorContainer)!!)

                btnLeftTV.setOnClickListener {
                    dismiss()
                }
                btnRightTV.setOnClickListener {
                    Toast.makeText(context, "Clicked: Supprimer", Toast.LENGTH_SHORT).show()
                }
            }
            Friend_PopUp_type.add_friend -> {
                btnLeftTV.text = "Annuler"
                btnRightTV.text = "Ajouter"

                btnRightTV.setBackgroundColor(context?.getColor(R.color.md_theme_dark_primaryContainer)!!)
                btnRightTV.setTextColor(context?.getColor(R.color.md_theme_dark_onPrimaryContainer)!!)

                btnLeftTV.setOnClickListener {
                    Toast.makeText(context, "Clicked: Annuler", Toast.LENGTH_SHORT).show()
                }
                btnRightTV.setOnClickListener {
                    Toast.makeText(context, "Clicked: Ajouter", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(context, "Problème de choix", Toast.LENGTH_SHORT).show()
                dismiss()
            }


        }

        return view

    }

    constructor(user : User, type: Friend_PopUp_type, title: String = "", desc: String = "") : this(){
        this.user = user
        this.type = type
        this.title = title
        this.desc = desc
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.TransparentDialog)
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