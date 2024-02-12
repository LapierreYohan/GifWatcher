package com.example.gifs_watcher.views.splashscreen.modals


import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
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
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
object LoginModal : BottomSheetDialogFragment(), EasyPermissions.PermissionCallbacks {


    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 123

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS


    const val TAG = "ModalLogin"
    private val splashScreenViewModel by activityViewModels<SplashScreenViewModel>()

    private lateinit var modal : ConstraintLayout

    private lateinit var idInput : TextInputEditText
    private lateinit var passwordInput : TextInputEditText
    private lateinit var idInputLayout : TextInputLayout
    private lateinit var passwordInputLayout : TextInputLayout

    private lateinit var googleSignInButton : ImageView
    private lateinit var facebookSignInButton : ImageView
    private lateinit var appleSignInButton : ImageView

    private lateinit var title : TextView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.modal_fragment_login ,container,false)

        this.googleSignInButton = view.findViewById(R.id.login_google_signin)
        this.facebookSignInButton = view.findViewById(R.id.login_facebook_signin)
        this.appleSignInButton = view.findViewById(R.id.login_apple_signin)
        this.title = view.findViewById(R.id.login_title)
        this.modal = view.findViewById(R.id.modal_fragment_log)

        this.googleSignInButton.visibility = View.INVISIBLE
        this.facebookSignInButton.visibility = View.INVISIBLE
        this.appleSignInButton.visibility = View.INVISIBLE

        // Ajout d'un moyen de register un compte
        val register : TextView = view.findViewById(R.id.login_register)
        register.setOnClickListener {
            this.dismiss()
            this.modal.visibility = View.INVISIBLE
            val registerMenu = RegisterModal
            registerMenu.show(this.parentFragmentManager, registerMenu.TAG)
        }

        // Ajout d'un moyen de reset son mot de passe
        val forgotPassword : TextView = view.findViewById(R.id.login_forgot_password)
        forgotPassword.setOnClickListener {
            this.dismiss()
            this.modal.visibility = View.INVISIBLE
            val passwordMenu = PasswordModal
            passwordMenu.show(parentFragmentManager, passwordMenu.TAG)
        }

        // Autorisé la connection à la main activity
        this.setUpLoginListeners(view)

        // Empêche de quitter le modal
        this.dialog?.setCancelable(false)

        this.idInput = view?.findViewById(R.id.login_identifiant_textinput)!!
        this.passwordInput = view.findViewById(R.id.login_password_textinput)!!

        this.idInputLayout = view.findViewById(R.id.login_identifiant_textinput_layout)!!
        this.passwordInputLayout = view.findViewById(R.id.login_password_textinput_layout)!!

        this.idInputLayout.error = null
        this.passwordInputLayout.error = null

        splashScreenViewModel.loggedLiveData.observe(this) {
            if (it.success()) {
                this.startActivity()
            } else if (it.failed()) {
                this.idInput.setText("")
                this.passwordInput.setText("")

                //reset des errors
                this.idInputLayout.error = null
                this.passwordInputLayout.error = null

                it.error().forEach { userError ->
                    when (userError) {
                        UserErrors.ID_EMPTY -> {
                            this.idInputLayout.error = userError.message
                            this.idInput.setError(userError.message, null)
                        }

                        UserErrors.PASSWORD_IS_EMPTY -> {
                            this.passwordInputLayout.error = userError.message
                            this.passwordInput.setError(userError.message, null)
                        }

                        UserErrors.ID_OR_PASSWORD_INVALID -> {
                            this.passwordInputLayout.error = userError.message
                            this.passwordInput.setError(userError.message, null)
                            this.idInputLayout.error = userError.message
                            this.idInput.setError(userError.message, null)
                        }

                        else -> {}
                    }
                }
            }
        }

        if (!hasNotificationPermission()) {
            requestNotificationPermission()
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
                this.modal.visibility = View.VISIBLE
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = 0
                it.translationY = 1500f
                it.animate().translationY(0f).setDuration(500).start()

                Handler(Looper.getMainLooper()).postDelayed({
                    animateSignInButtons()
                }, 500)
            }
        }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog : Dialog? = this.dialog

        if (dialog != null) {
            val bottomSheet : View = dialog.findViewById(R.id.modal_fragment_log)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

            val view : View? = view

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

    private fun setUpLoginListeners(view: View) {
        // Bouton de Connexion
        val connectButton : Button = view.findViewById(R.id.login_signin_button)
        connectButton.setOnClickListener {
            this.splashScreenViewModel.login(this.idInput.text.toString().trim(), this.passwordInput.text.toString().trim())
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

    private fun login() {

    }

    private fun startActivity() {
        val intent = Intent(this.context, MainActivity::class.java)

        this.dismiss()
        startActivity(intent)
        this.activity?.finish()
    }

    private fun animateSignInButtons() {

        val delayBetweenAnimations = 200L

        YoYo.with(Techniques.Tada)
            .duration(1000)
            .playOn(this.title)

        YoYo.with(Techniques.SlideInUp)
            .duration(500)
            .onStart {
                this.googleSignInButton.visibility = View.VISIBLE
            }
            .playOn(this.googleSignInButton)

        Handler(Looper.getMainLooper()).postDelayed({
            YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .onStart {
                    this.facebookSignInButton.visibility = View.VISIBLE
                }
                .playOn(this.facebookSignInButton)
        }, delayBetweenAnimations)

        Handler(Looper.getMainLooper()).postDelayed({
            YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .onStart {
                    this.appleSignInButton.visibility = View.VISIBLE
                }
                .playOn(this.appleSignInButton)
        }, delayBetweenAnimations * 2)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun hasNotificationPermission(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), NOTIFICATION_PERMISSION)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, NOTIFICATION_PERMISSION_REQUEST_CODE, NOTIFICATION_PERMISSION)
                .build()
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Timber.d("Permission Notification granted")
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        requestNotificationPermission()
    }
}