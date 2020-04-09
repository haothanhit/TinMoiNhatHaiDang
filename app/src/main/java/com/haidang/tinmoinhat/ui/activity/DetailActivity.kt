package com.haidang.tinmoinhat.ui.activity

import android.os.Bundle
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.model.ModelArticle

class DetailActivity : BaseActivity() {
    private val TAG: String = DetailActivity::class.java.getSimpleName()
    private var data: ModelArticle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        data = intent.getSerializableExtra("Article") as ModelArticle?
        initView()
    }

    private fun initView() {
        //Layout detail


    }

}