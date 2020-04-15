package com.haidang.tinmoinhat.ui.activity

import android.os.Bundle
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.base.BaseActivity

class InfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (currentIsNight()) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
        setContentView(R.layout.activity_info)

    }
}