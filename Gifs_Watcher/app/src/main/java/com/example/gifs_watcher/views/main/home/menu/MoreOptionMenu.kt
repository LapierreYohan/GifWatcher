package com.example.gifs_watcher.views.main.home.menu

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.example.gifs_watcher.R
import com.example.gifs_watcher.models.Results
import com.example.gifs_watcher.views.main.MainViewModel


class MoreOptionMenu(private var context: Context, anchorView: View, viewModel : MainViewModel) {

    private val popupMenu: PopupMenu
    private var gifPrinted : Results? = null
    private val mainViewModel = viewModel

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

        if (gifPrinted == null) {
            Toast.makeText(context, "Aucun gif sélectionné", Toast.LENGTH_SHORT).show()
            return
        }

        when (item.itemId) {
            R.id.menu_share -> Toast.makeText(context, "Partager sélectionné", Toast.LENGTH_SHORT).show()

            R.id.menu_download -> {
                mainViewModel.downloadGif(context, gifPrinted!!)
            }

            R.id.menu_copy_link -> {
                mainViewModel.copyLinkGif(context, gifPrinted!!)
            }

            R.id.menu_set_avatar -> {
                mainViewModel.setAvatarGif(gifPrinted!!)
            }
        }
    }

    fun setGifPrinted(gif : Results) {
        gifPrinted = gif
    }

    fun getGifPrinted() : Results? {
        return gifPrinted
    }
}