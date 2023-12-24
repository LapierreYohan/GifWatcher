package com.example.gifs_watcher.views.main.profil.menu

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.example.gifs_watcher.R
import com.example.gifs_watcher.views.main.MainViewModel
class ParameterMenu(private var context: Context, anchorView: View, viewModel : MainViewModel) {

    private val popupMenu: PopupMenu
    private val mainViewModel = viewModel

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
            R.id.menu_parameters -> Toast.makeText(context, "Paramètres sélectionné", Toast.LENGTH_SHORT).show()
            R.id.menu_picture -> Toast.makeText(context, "Changer la photo de profil", Toast.LENGTH_SHORT).show()
            R.id.menu_profil -> Toast.makeText(context, "Changer le nom d'utilisateur", Toast.LENGTH_SHORT).show()
        }
    }
}