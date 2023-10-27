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

object ModalRegister : BottomSheetDialogFragment() {
    const val TAG = "ModalRegister"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.modal_fragment_register ,container,false)

        /*
        val register : TextView = view.findViewById(R.id.login_register)
        register.setOnClickListener {
            this.dismiss()
        }*/

        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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
        var dialog : Dialog? = ModalLogin.getDialog();

        if (dialog != null) {
            var bottomSheet : View = dialog.findViewById(R.id.modal_fragment)
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

            var view : View? = ModalLogin.getView();

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