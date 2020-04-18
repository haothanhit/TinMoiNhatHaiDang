package com.haidang.tinmoinhat.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.adapter.ViewPagerAdapter
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.base.BaseFragment
import com.haidang.tinmoinhat.common.global.Constants.Companion.PAGE_NUMBER
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {
    var adapter: ViewPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setupViewPager()

    }

    @SuppressLint("ResourceType")
    private fun setupViewPager() {

        adapter = ViewPagerAdapter(
            childFragmentManager!!,
            (activity as BaseActivity).listTopicCurrent!!
        ) //viewpager
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = PAGE_NUMBER
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.tabTextColors =
            if ((activity as BaseActivity).currentIsNight()) context!!.resources.getColorStateList(R.drawable.tab_color_selector_night) else context!!.resources.getColorStateList(
                R.drawable.tab_color_selector
            )


    }

}