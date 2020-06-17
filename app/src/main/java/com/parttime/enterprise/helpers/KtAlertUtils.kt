package com.albacars.app.helpers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.parttime.enterprise.R


object KtAlertUtils {

    fun showAlert(
        context: Context,
        msg: String,
        listener: DialogInterface.OnClickListener,
        cancelListener: DialogInterface.OnClickListener
    ) {
        var alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setMessage(msg)
        alertDialog.setPositiveButton(context.getString(R.string.yes), listener)
        alertDialog.setNegativeButton(context.getString(R.string.no), cancelListener)

        val alertDialogBuild = alertDialog.create()
        alertDialogBuild.window?.attributes?.windowAnimations = R.style.PauseDialogAnimation
        alertDialogBuild.setCancelable(false)
        alertDialogBuild.show()

    }
}