package com.example.gifs_watcher.views.splashscreen.modals

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import com.example.gifs_watcher.R
import com.example.gifs_watcher.utils.enums.UserErrors
import com.example.gifs_watcher.viewmodel.SplashScreenViewModel
import com.example.gifs_watcher.views.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object RegisterModal : BottomSheetDialogFragment() {
    const val TAG = "ModalRegister"
    private val splashScreenViewModel by activityViewModels<SplashScreenViewModel>()

    private lateinit var userInput : TextInputEditText
    private lateinit var passwordInput : TextInputEditText
    private lateinit var emailInput : TextInputEditText
    private lateinit var confirmPasswordInput : TextInputEditText
    private lateinit var birthdayInput : TextInputEditText

    private lateinit var userInputLayout : TextInputLayout
    private lateinit var passwordInputLayout : TextInputLayout
    private lateinit var emailInputLayout : TextInputLayout
    private lateinit var confirmPasswordInputLayout : TextInputLayout
    private lateinit var birthdayInputLayout : TextInputLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.modal_fragment_register ,container,false)

        // Ajout d'un moyen de retour sur login
        val login : TextView = view.findViewById(R.id.register_login)
        login.setOnClickListener {
            this.dismiss()
            val loginMenu: LoginModal = LoginModal
            loginMenu.show(parentFragmentManager, loginMenu.TAG)
        }

        // Empêche de quitter le modal
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)

        this.userInput = view?.findViewById(R.id.register_username_textinput)!!
        this.passwordInput = view.findViewById(R.id.register_password_textinput)!!
        this.emailInput = view.findViewById(R.id.register_mail_textinput)!!
        this.confirmPasswordInput = view.findViewById(R.id.register_confirmpassword_textinput)!!
        this.birthdayInput = view.findViewById(R.id.register_birthday_textinput)!!

        this.userInputLayout = view.findViewById(R.id.register_username_textinput_layout)!!
        this.passwordInputLayout = view.findViewById(R.id.register_password_textinput_layout)!!
        this.emailInputLayout = view.findViewById(R.id.register_mail_textinput_layout)!!
        this.confirmPasswordInputLayout = view.findViewById(R.id.register_confirmpassword_textinput_layout)!!
        this.birthdayInputLayout = view.findViewById(R.id.register_birthday_textinput_layout)!!

        this.userInputLayout.error = null
        this.passwordInputLayout.error = null
        this.emailInputLayout.error = null
        this.confirmPasswordInputLayout.error = null
        this.birthdayInputLayout.error = null

        this.splashScreenViewModel.signinLiveData.observe(this) {
            println(it)
            if (it.success()) {
                this.startActivity()
            } else if (it.failed()) {
                this.passwordInput.setText("")
                this.confirmPasswordInput.setText("")

                //Reset des errors
                this.userInputLayout.error = null
                this.passwordInputLayout.error = null
                this.emailInputLayout.error = null
                this.confirmPasswordInputLayout.error = null
                this.birthdayInputLayout.error = null

                it.error().forEach { userError ->
                    when (userError) {
                        UserErrors.USERNAME_IS_EMPTY,
                        UserErrors.USERNAME_ALREADY_USED,
                        UserErrors.USERNAME_NOT_VALID,
                        UserErrors.USERNAME_TOO_SHORT,
                        UserErrors.USERNAME_CONTAINS_EXCEPTED_CHARACTERS -> {
                            if (this.userInputLayout.error == null) {
                                this.userInputLayout.error = userError.message
                                this.userInput.setError(userError.message, null)
                            }
                        }

                        UserErrors.PASSWORD_TOO_SHORT,
                        UserErrors.PASSWORD_INVALID -> {
                            if (this.passwordInputLayout.error == null) {
                                this.passwordInputLayout.error = userError.message
                                passwordInput.setError(userError.message, null)
                            }
                        }

                        UserErrors.PASSWORD_IS_EMPTY,
                        UserErrors.PASSWORDS_NOT_MATCHING -> {
                            if (this.confirmPasswordInputLayout.error == null && this.passwordInputLayout.error == null) {

                                this.confirmPasswordInputLayout.error = userError.message
                                confirmPasswordInput.setError(userError.message, null)
                                this.passwordInputLayout.error = userError.message
                                passwordInput.setError(userError.message, null)

                            }
                        }

                        UserErrors.EMAIL_IS_EMPTY,
                        UserErrors.EMAIL_NOT_VALID,
                        UserErrors.EMAIL_ALREADY_USED,
                        UserErrors.EMAIL_CONTAINS_EXCEPTED_CHARACTERS -> {
                            if (this.emailInputLayout.error == null) {
                                this.emailInputLayout.error = userError.message
                                emailInput.setError(userError.message, null)
                            }
                        }

                        UserErrors.BIRTHDATE_IS_EMPTY,
                        UserErrors.BIRTHDATE_NOT_VALID,
                        UserErrors.BIRTHDATE_TOO_YOUNG -> {
                            if (this.birthdayInputLayout.error == null) {
                                this.birthdayInputLayout.error = userError.message
                                birthdayInput.setError(userError.message, null)
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        setUpRegisterListeners(view)

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
        var dialog : Dialog? = RegisterModal.getDialog();

        if (dialog != null) {
            var bottomSheet : View = dialog.findViewById(R.id.modal_fragment_sign)
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            var view : View? = RegisterModal.getView();

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

    private fun setUpRegisterListeners(view: View) : Unit {
        // Bouton de Connexion
        val registerButton : Button = view.findViewById(R.id.register_signin_button)

        registerButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.splashScreenViewModel.register(
                    this.userInput.text.toString().trim(),
                    this.passwordInput.text.toString().trim(),
                    this.confirmPasswordInput.text.toString().trim(),
                    this.emailInput.text.toString().trim(),
                    this.birthdayInput.text.toString().trim()
                )
            }
        }
    }
    private fun startActivity() : Unit {
        val intent : Intent = Intent(context, MainActivity::class.java)

        this.dismiss()
        startActivity(intent)
        activity?.finish()
    }
}