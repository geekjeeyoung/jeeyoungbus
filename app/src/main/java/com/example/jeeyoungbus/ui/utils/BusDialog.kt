package com.example.jeeyoungbus.ui.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.jeeyoungbus.databinding.DialogAlertPaymentBinding

class BusDialog {
    fun showPaymentAlertDialog(
        context: Context,
        dialogMsg: String,
        doOnConfirm: () -> Unit,
        doOnClose: () -> Unit,
        isResult: Boolean = false
    ) {
        val dialogBinding: DialogAlertPaymentBinding =
            DialogAlertPaymentBinding.inflate(LayoutInflater.from(context), null, false)
        val dialog = Dialog(context)
        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setContentView(dialogBinding.root)
            val params: WindowManager.LayoutParams? = window?.attributes
            params?.width = WindowManager.LayoutParams.MATCH_PARENT
            params?.height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.attributes = params
            setCancelable(false)
            show()
        }
        dialogBinding.apply {
            tvDialogMessage.text = dialogMsg
            tvDialogConfirm.setOnClickListener {
                dialog.dismiss()
                doOnConfirm()
            }
            if (isResult) {
                ivCloseBtn.visibility = View.INVISIBLE
                tvDialogCancel.visibility = View.INVISIBLE
            } else {
                ivCloseBtn.setOnClickListener {
                    dialog.dismiss()
                    doOnClose()
                }
                tvDialogCancel.setOnClickListener {
                    dialog.dismiss()
                    doOnClose()
                }
            }
        }
    }
}