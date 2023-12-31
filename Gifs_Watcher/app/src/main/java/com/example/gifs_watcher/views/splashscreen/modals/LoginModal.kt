package com.example.gifs_watcher.views.splashscreen.modals


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.gifs_watcher.R
import com.example.gifs_watcher.utils.enums.UserErrors
import com.example.gifs_watcher.views.splashscreen.SplashScreenViewModel
import com.example.gifs_watcher.views.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("StaticFieldLeak")
object LoginModal : BottomSheetDialogFragment() {

    const val TAG = "ModalLogin"
    private val splashScreenViewModel by activityViewModels<SplashScreenViewModel>()

    private lateinit var idInput : TextInputEditText
    private lateinit var passwordInput : TextInputEditText
    private lateinit var idInputLayout : TextInputLayout
    private lateinit var passwordInputLayout : TextInputLayout

    private lateinit var googleSignInButton : ImageView
    private lateinit var facebookSignInButton : ImageView
    private lateinit var appleSignInButton : ImageView

    private lateinit var title : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.modal_fragment_login ,container,false)

        googleSignInButton = view.findViewById(R.id.login_google_signin)
        facebookSignInButton = view.findViewById(R.id.login_facebook_signin)
        appleSignInButton = view.findViewById(R.id.login_apple_signin)
        title = view.findViewById(R.id.login_title)

        googleSignInButton.visibility = View.INVISIBLE
        facebookSignInButton.visibility = View.INVISIBLE
        appleSignInButton.visibility = View.INVISIBLE

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
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = 0
                it.translationY = 1500f // ajustez la valeur selon votre besoin
                it.animate().translationY(0f).setDuration(500).start()

                Handler().postDelayed({
                    animateSignInButtons(it)
                }, 500)
            }
        }

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

    private fun animateSignInButtons(view: View) {

        val delayBetweenAnimations = 200L

        YoYo.with(Techniques.Tada)
            .duration(1000)
            .playOn(title)

        YoYo.with(Techniques.SlideInUp)
            .duration(500)
            .onStart {
                googleSignInButton.visibility = View.VISIBLE
            }
            .playOn(googleSignInButton)

        Handler().postDelayed({
            YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .onStart {
                    facebookSignInButton.visibility = View.VISIBLE
                }
                .playOn(facebookSignInButton)
        }, delayBetweenAnimations)

        Handler().postDelayed({
            YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .onStart {
                    appleSignInButton.visibility = View.VISIBLE
                }
                .playOn(appleSignInButton)
        }, delayBetweenAnimations * 2)
    }
}