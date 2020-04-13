package com.haidang.tinmoinhat.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import com.haidang.tinmoinhat.R

class LoadingDialog(context: Context) : Dialog(context) {
    private var animation: AnimationDrawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        window!!.setBackgroundDrawableResource(R.drawable.transparent)
        val loadingImage = findViewById<View>(R.id.login_loading) as ImageView
        val rltLoading = findViewById<View>(R.id.rltLoading) as RelativeLayout
        rltLoading.setOnClickListener { dismiss() }


        animation = loadingImage.background as AnimationDrawable
    }
    override fun show() {
        try {
            super.show()
            animation?.start()
        } catch (ignored: Exception) {
        }
    }

    override fun dismiss() {
        try {
            super.dismiss()
            if (animation != null) {
                animation?.stop()
            }
        } catch (ignored: Exception) {
        }
    }

    override fun cancel() {
        super.cancel()
        if (animation != null) {
            animation?.stop()
        }
    }
}