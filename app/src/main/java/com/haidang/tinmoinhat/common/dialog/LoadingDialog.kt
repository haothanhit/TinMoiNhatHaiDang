package com.haidang.tinmoinhat.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.haidang.tinmoinhat.R

class LoadingDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        setCancelable(true)
       setCanceledOnTouchOutside(true)
        window!!.setBackgroundDrawableResource(R.drawable.transparent)
        val login_loading = findViewById<View>(R.id.login_loading) as ImageView
      Glide.with(context).load(R.drawable.ic_gif_loading).into(login_loading)

    }

}