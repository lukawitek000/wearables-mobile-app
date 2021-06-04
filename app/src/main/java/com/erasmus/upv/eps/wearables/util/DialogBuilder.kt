package com.erasmus.upv.eps.wearables.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.erasmus.upv.eps.wearables.R

object DialogBuilder {

    fun buildAndShowDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        title: String,
        message: String,
        yesButtonText: String = "YES",
        noButtonText: String = "NO",
        yesButtonAction: (dialog: AlertDialog) -> Unit = {},
        noButtonAction: (dialog: AlertDialog) -> Unit = {},
        isNoButtonVisible: Boolean = true
        )
    {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val dialog = alertDialogBuilder.create()
        val view = layoutInflater.inflate(R.layout.dialog_alert_info, null)
        view.findViewById<TextView>(R.id.title_alert_dialog_tv).text = title
        view.findViewById<TextView>(R.id.message_alert_dialog_tv).text = message
        val okButton = view.findViewById<Button>(R.id.yes_alert_dialog_bt)
        okButton.text = yesButtonText
        okButton.setOnClickListener {
            yesButtonAction.invoke(dialog)
        }
        val noButton = view.findViewById<Button>(R.id.no_alert_dialog_bt)
        noButton.text = noButtonText
        noButton.setOnClickListener {
            noButtonAction.invoke(dialog)
        }
        if(!isNoButtonVisible) {
            noButton.visibility = View.GONE
        }
        dialog.setView(view)
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)




    }


}