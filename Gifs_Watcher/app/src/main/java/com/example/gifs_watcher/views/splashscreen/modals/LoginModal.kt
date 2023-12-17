package com.example.gifs_watcher.views.splashscreen.modals

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import com.example.gifs_watcher.R
import com.example.gifs_watcher.utils.enums.UserErrors
import com.example.gifs_watcher.views.splashscreen.SplashScreenViewModel
import com.example.gifs_watcher.views.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


object LoginModal : BottomSheetDialogFragment() {

    const val TAG = "ModalLogin"
    private val splashScreenViewModel by activityViewModels<SplashScreenViewModel>()

    private lateinit var idInput : TextInputEditText
    private lateinit var passwordInput : TextInputEditText
    private lateinit var idInputLayout : TextInputLayout
    private lateinit var passwordInputLayout : TextInputLayout

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

        this.idInput = view?.findViewById(R.id.login_identifiant_textinput)!!
        this.passwordInput = view.findViewById(R.id.login_password_textinput)!!

        //TODO : DEV ONLY
        idInput.setText("galtrips")
        passwordInput.setText("azerty")

        this.idInputLayout = view.findViewById(R.id.login_identifiant_textinput_layout)!!
        this.passwordInputLayout = view.findViewById(R.id.login_password_textinput_layout)!!

        this.idInputLayout.error = null
        this.passwordInputLayout.error = null

        splashScreenViewModel.loggedLiveData.observe(this) {
            if (it.success()) {
                this.startActivity()
            } else if (it.failed()) {
                idInput.setText("")
                passwordInput.setText("")

                //reset des errors
                idInputLayout.error = null
                passwordInputLayout.error = null

                it.error().forEach { userError ->
                    when (userError) {
                        UserErrors.ID_EMPTY -> {
                            idInputLayout.error = userError.message
                            idInput.setError(userError.message, null)
                        }

                        UserErrors.PASSWORD_IS_EMPTY -> {
                            passwordInputLayout.error = userError.message
                            passwordInput.setError(userError.message, null)
                        }

                        UserErrors.ID_OR_PASSWORD_INVALID -> {
                            passwordInputLayout.error = userError.message
                            passwordInput.setError(userError.message, null)
                            idInputLayout.error = userError.message
                            idInput.setError(userError.message, null)
                        }

                        else -> {}
                    }
                }
            }
        }

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
        // Bouton de Connexion
        val connectButton : Button = view.findViewById(R.id.login_signin_button)
        connectButton.setOnClickListener {
            this.splashScreenViewModel.login(idInput.text.toString().trim(), passwordInput.text.toString().trim())
        }

        // Bouton de Connexion avec Google
        val connectGoogleButton : ImageView = view.findViewById(R.id.login_google_signin)
        connectGoogleButton.setOnClickListener {
            this.login()
        }

        // Bouton de Connexion avec Facebook
        val connectFacebookButton : ImageView = view.findViewById(R.id.login_facebook_signin)
        connectFacebookButton.setOnClickListener {
            this.login()
        }

        // Bouton de Connexion avec Apple
        val connectAppleButton : ImageView = view.findViewById(R.id.login_apple_signin)
        connectAppleButton.setOnClickListener {
            this.login()
        }
    }

    private fun login() : Unit {

    }

    private fun startActivity() : Unit {
        val intent : Intent = Intent(context, MainActivity::class.java)

        this.dismiss()
        startActivity(intent)
        activity?.finish()
    }
}