package com.haidang.tinmoinhat.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_THEME
import com.haidang.tinmoinhat.common.global.Constants.Companion.TAB_HOME
import com.haidang.tinmoinhat.common.global.Constants.Companion.TAB_SETTING
import com.haidang.tinmoinhat.ui.fragment.HomeFragment
import com.haidang.tinmoinhat.ui.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var defaultTextBottomColor: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(R.id.container, HomeFragment(), false)
        navigation.performClick()

        llBottomNews.setOnClickListener(mOnclick)
        llBottomSettings.setOnClickListener(mOnclick)
        defaultTextBottomColor = tvBottomSettings.textColors.defaultColor
        setColorTabBottom(1)
    }


    fun setTabBottom(pos: Int) {  // 1 :new ,2:setting
        when (pos) {
            1 -> llBottomNews.callOnClick()
            2 -> llBottomSettings.callOnClick()
        }
    }

    fun setColorTabBottom(pos: Int) {// 1 :new ,2:setting
        when (pos) {
            1 -> {
                llBottomNews.setBackgroundResource(R.drawable.bg_bottom_navigation)
                llBottomSettings.setBackgroundResource(android.R.color.transparent)
                ivBottomNews.setImageResource(R.drawable.ic_bottom_news_selected)
                tvBottomNews.setTextColor(resources.getColor(R.color.colorPrimary))
                ivBottomSettings.setImageResource(R.drawable.ic_bottom_setting)
                tvBottomSettings.setTextColor(defaultTextBottomColor!!)
            }
            2 -> {
                llBottomNews.setBackgroundResource(android.R.color.transparent)
                llBottomSettings.setBackgroundResource(R.drawable.bg_bottom_navigation)
                ivBottomNews.setImageResource(R.drawable.ic_bottom_news)
                tvBottomNews.setTextColor(defaultTextBottomColor!!)
                ivBottomSettings.setImageResource(R.drawable.ic_bottom_setting_selected)
                tvBottomSettings.setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private val mOnclick = View.OnClickListener { p0 ->
        when (p0) {
            llBottomNews -> {
                var isCreate = false
                var fragment = supportFragmentManager.findFragmentByTag(TAB_HOME)
                if (fragment == null) {
                    fragment = HomeFragment()
                    isCreate = true

                }
                replaceFragmentwithTag(fragment, isCreate, TAB_HOME)
                setColorTabBottom(1)
            }
            llBottomSettings -> {
                var isCreate = false

                var fragment = supportFragmentManager.findFragmentByTag(TAB_SETTING)
                if (fragment == null) {
                    fragment = SettingFragment()
                    isCreate = true

                }
                replaceFragmentwithTag(fragment, isCreate, TAB_SETTING)
                setColorTabBottom(2)

            }
        }
    }


    /**
     * add fragment has tag
     */
    private fun replaceFragmentwithTag(fragment: Fragment, isCreate: Boolean, tag: String) {
        if (fragment != currentFragment) {
            if (isCreate) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit()

            } else {
                supportFragmentManager
                    .beginTransaction().show(fragment).commitNow()
            }
            when (tag) {
                TAB_HOME -> {
                    hideFragmentwithTag(TAB_SETTING)
                }
                TAB_SETTING -> {
                    hideFragmentwithTag(TAB_HOME)
                }

            }

            currentFragment = fragment
        }
    }

    /**
     * hide fragment with tag
     */
    private fun hideFragmentwithTag(tag: String) {
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction().hide(fragment).commitNow()
        }
    }
}
