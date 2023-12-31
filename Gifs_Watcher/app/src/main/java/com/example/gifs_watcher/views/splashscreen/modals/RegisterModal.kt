package com.example.gifs_watcher.views.splashscreen.modals

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import com.example.gifs_watcher.R
import com.example.gifs_watcher.utils.enums.UserErrors
import com.example.gifs_watcher.views.main.MainActivity
import com.example.gifs_watcher.views.splashscreen.SplashScreenViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

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

    @RequiresApi(Build.VERSION_CODES.O)
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

        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.monthValue
        val day = currentDate.dayOfMonth
        Locale.setDefault(Locale.FRENCH)

        val configuration: Configuration = requireContext().resources.configuration
        configuration.setLocale(Locale.FRENCH)
        configuration.setLayoutDirection(Locale.FRENCH)
        requireContext().createConfigurationContext(configuration)

        val datePickerDialog = DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerDialog,
                { _, year, monthOfYear, dayOfMonth ->
                    val month = monthOfYear + 1
                    val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month, year)
                    birthdayInput.setText(formattedDate)
                },
                year - 15,
                month - 1,
                day
        )

        // Personnalisez le DatePickerDialog pour ajouter un NumberPicker pour les années
        datePickerDialog.setOnShowListener { dialog ->
            val datePicker = (dialog as DatePickerDialog).datePicker

            datePicker.maxDate = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            datePicker.minDate = currentDate.minusYears(120).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        birthdayInput.showSoftInputOnFocus = false

        birthdayInput.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                datePickerDialog.show()
            }
            true
        }

        setUpRegisterListeners(view)

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
            }
        }

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