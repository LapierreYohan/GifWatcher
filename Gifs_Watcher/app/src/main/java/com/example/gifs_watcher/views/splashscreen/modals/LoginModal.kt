package com.example.gifs_watcher.views.splashscreen.modals

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.gifs_watcher.R
import com.example.gifs_watcher.views.MainActivity
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

        // Autorisé la connection à la main activity
        //? Pas de vérification utilisateur
        this.setUpLoginListeners(view)

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

    private fun setUpLoginListeners(view: View) : Unit {
        this.login(view)
    }

    private fun login(view: View) : Unit {
        val connectButton : Button = view.findViewById(R.id.login_signin_button)
        connectButton.setOnClickListener {
            startActivity()
        }

    }

    private fun startActivity() : Unit {
        val intent : Intent = Intent(context, MainActivity::class.java)
        
        this.dismiss()
        startActivity(intent)
        activity?.finish()
    }
}