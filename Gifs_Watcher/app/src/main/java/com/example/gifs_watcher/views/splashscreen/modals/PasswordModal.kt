package com.example.gifs_watcher.views.splashscreen.modals

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.gifs_watcher.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@SuppressLint("StaticFieldLeak")
class PasswordModal : BottomSheetDialogFragment() {

    val TAG = "ModalPassword"

    private lateinit var modal : ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.modal_fragment_password ,container,false)

        this.modal = view.findViewById(R.id.modal_fragment_pass)

        // Ajout d'un moyen de retour sur login
        val login : TextView = view.findViewById(R.id.password_login)
        login.setOnClickListener {
            this.dismiss()
            this.modal.visibility = View.INVISIBLE
            val loginMenu = LoginModal()
            loginMenu.show(this.parentFragmentManager, loginMenu.TAG)
        }

        this.dialog?.setCancelable(false)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = context?.let {
            BottomSheetDialog(
                it,
                R.style.MyTransparentBottomSheetDialogTheme
            )
        } ?: super.onCreateDialog(savedInstanceState)

        // Animation lors du show
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                this.modal.visibility = View.VISIBLE
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = 0
                it.translationY = 1500f
                it.animate().translationY(0f).setDuration(500).start()
            }
        }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        // Adapter la fenêtre à la taille de la modal
        val dialog : Dialog? = this.dialog

        if (dialog != null) {
            val bottomSheet : View = dialog.findViewById(R.id.modal_fragment_pass)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

            val view : View? = this.view

            view?.post {
                val parent : View = view.parent as View
                val params : CoordinatorLayout.LayoutParams = (parent).layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior
                val bottomSheetBehavior = behavior as BottomSheetBehavior
                bottomSheetBehavior.peekHeight = view.measuredHeight

                (bottomSheet.parent as View).setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}