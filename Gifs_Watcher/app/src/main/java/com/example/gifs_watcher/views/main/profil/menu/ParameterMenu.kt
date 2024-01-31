package com.example.gifs_watcher.views.main.profil.menu

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.example.gifs_watcher.R

class ParameterMenu(private var context: Context, anchorView: View) {

    private val popupMenu: PopupMenu

    init {
        val wrapper: Context = ContextThemeWrapper(context, R.style.PopupMenuStyle)
        popupMenu = PopupMenu(wrapper, anchorView)
        popupMenu.menuInflater.inflate(R.menu.profil_dropdown_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->

            handleOptionSelection(item)
            true
        }

        popupMenu.gravity = Gravity.END

        anchorView.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun handleOptionSelection(item: MenuItem) {
        when (item.itemId) {
            R.id.menu_parameters -> showToast("Paramètres sélectionné")
            R.id.menu_picture -> showToast("Changer la photo de profil")
            R.id.menu_profil -> showToast("Changer le nom d'utilisateur")
            R.id.menu_sign_out -> showToast("Sign out")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}