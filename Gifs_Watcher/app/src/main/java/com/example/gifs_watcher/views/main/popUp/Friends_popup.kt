package com.example.gifs_watcher.views.main.popUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.gifs_watcher.models.User

class Friends_popup(): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater
            .inflate(com.example.gifs_watcher.R.layout.friends_pop_up, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnLeft = view?.findViewById<View>(com.example.gifs_watcher.R.id.bt_friends_popup_left)
        val btnRight = view?.findViewById<View>(com.example.gifs_watcher.R.id.bt_friends_popup_right)

        btnLeft?.setOnClickListener {
            Toast.makeText(context, "Clicked: left", Toast.LENGTH_SHORT).show()
        }
        btnRight?.setOnClickListener {
            Toast.makeText(context, "Clicked: right", Toast.LENGTH_SHORT).show()
        }

    }
}