package com.haidang.tinmoinhat.ui.activity

import android.os.Bundle
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_choose_topic.*

class ChooseTopic :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (currentIsNight()) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
        setContentView(R.layout.activity_choose_topic)
        initView()
    }

    private fun initView() {
        ivBackTopic.setOnClickListener { onBackPressed() }

    }
}