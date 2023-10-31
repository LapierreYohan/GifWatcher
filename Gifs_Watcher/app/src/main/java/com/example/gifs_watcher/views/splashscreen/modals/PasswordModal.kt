package com.example.gifs_watcher.views.splashscreen.modals

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import com.example.gifs_watcher.R
import com.example.gifs_watcher.viewmodel.SplashScreenViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

object PasswordModal : BottomSheetDialogFragment() {
    const val TAG = "ModalPassword"
    private val splashScreenViewModel by activityViewModels<SplashScreenViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.modal_fragment_password ,container,false)

        // Ajout d'un moyen de retour sur login
        val login : TextView = view.findViewById(R.id.password_login)
        login.setOnClickListener {
            this.dismiss()
            val loginMenu: LoginModal = LoginModal
            loginMenu.show(parentFragmentManager, loginMenu.TAG)
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
        var dialog : Dialog? = PasswordModal.getDialog();

        if (dialog != null) {
            var bottomSheet : View = dialog.findViewById(R.id.modal_fragment_pass)
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            var view : View? = PasswordModal.getView();

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