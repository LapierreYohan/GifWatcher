package com.example.gifs_watcher.views.main.home.menu

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.gifs_watcher.R


class MoreOptionMenu(private var context: Context, anchorView: View) {

    private val popupMenu: PopupMenu

    init {
        val wrapper: Context = ContextThemeWrapper(context, R.style.PopupMenuStyle)
        popupMenu = PopupMenu(wrapper, anchorView)
        popupMenu.menuInflater.inflate(R.menu.gif_dropdown_menu, popupMenu.menu)

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
            R.id.menu_share -> Toast.makeText(context, "Partager sélectionné", Toast.LENGTH_SHORT).show()
            R.id.menu_download -> Toast.makeText(context, "Télécharger sélectionné", Toast.LENGTH_SHORT).show()
            R.id.menu_copy_link -> Toast.makeText(context, "Copier le lien sélectionné", Toast.LENGTH_SHORT).show()
            R.id.menu_set_avatar -> Toast.makeText(context, "Mettre en Avatar sélectionné", Toast.LENGTH_SHORT).show()
        }
    }
}