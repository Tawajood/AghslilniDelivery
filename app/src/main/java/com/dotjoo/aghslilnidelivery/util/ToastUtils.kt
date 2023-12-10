package com.dotjoo.aghslilnidelivery.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.Toast
import com.dotjoo.aghslilnidelivery.R
import com.google.android.material.snackbar.Snackbar

class ToastUtils{


    companion object{
        private var toastMessage: Toast? = null
        @SuppressLint("ShowToast")
        fun showToast(context: Context?, msg: String){
            if (toastMessage != null) {
                toastMessage!!.cancel()
            }
            toastMessage = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            toastMessage!!.show()
        }
        fun onSNACK(view: View, context: Context ,listener: OnClickListener,){
            //Snackbar(view)
            val snackbar = Snackbar.make(view, context.resources.getString(R.string.retry),
                Snackbar.LENGTH_LONG).setAction("Action", listener)
            snackbar.setActionTextColor(Color.BLUE)
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(Color.LTGRAY)
            val textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.BLUE)
            textView.textSize = 28f
            snackbar.show()
        }
    }
}
