package com.example.gifs_watcher.views.splashscreen.modals

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.gifs_watcher.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


object LoginModal : BottomSheetDialogFragment() {

    const val TAG = "ModalLogin"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.modal_fragment_login ,container,false)

        // Ajout d'un moyen de register un compte
        val register : TextView = view.findViewById(R.id.login_register)
        register.setOnClickListener {
            this.dismiss()
            val registerMenu: RegisterModal = RegisterModal
            registerMenu.show(parentFragmentManager, registerMenu.TAG)
        }

        // Ajout d'un moyen de reset son mot de passe
        val forgotPassword : TextView = view.findViewById(R.id.login_forgot_password)
        forgotPassword.setOnClickListener {
            this.dismiss()
            val passwordMenu: PasswordModal = PasswordModal
            passwordMenu.show(parentFragmentManager, passwordMenu.TAG)
        }

        // Empêche de quitter le modal
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Définir un background transparent
        var dialog : BottomSheetDialog
        dialog = context?.let {
           BottomSheetDialog(
                it,
                R.style.MyTransparentBottomSheetDialogTheme
            )
        }!!

        return dialog
    }

    override fun onStart() : Unit {
        super.onStart();
        // Adapter la fenêtre à la taille de la modal
        var dialog : Dialog? = getDialog();

        if (dialog != null) {
            var bottomSheet : View = dialog.findViewById(R.id.modal_fragment_log)
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            var view : View? = getView();

            val post = view?.post {

                var parent : View = view.getParent() as View
                var params : CoordinatorLayout.LayoutParams = (parent).getLayoutParams() as CoordinatorLayout.LayoutParams
                var behavior = params.getBehavior();
                var bottomSheetBehavior = behavior as BottomSheetBehavior;
                bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());

                (bottomSheet.getParent() as View).setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}