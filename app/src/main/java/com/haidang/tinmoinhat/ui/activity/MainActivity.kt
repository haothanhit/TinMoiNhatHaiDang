package com.haidang.tinmoinhat.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.global.Constants.Companion.TAB_HOME
import com.haidang.tinmoinhat.common.global.Constants.Companion.TAB_SETTING
import com.haidang.tinmoinhat.ui.fragment.HomeFragment
import com.haidang.tinmoinhat.ui.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(R.id.container, HomeFragment(), false)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.navigation_news -> {

                        var isCreate = false
                        var fragment = supportFragmentManager.findFragmentByTag(TAB_HOME)
                        if (fragment == null) {
                            fragment = HomeFragment()
                            isCreate = true

                        }
                        replaceFragmentwithTag(fragment, isCreate, TAB_HOME)
                        return true
                    }
                    R.id.navigation_setting -> {

                        var isCreate = false

                        var fragment = supportFragmentManager.findFragmentByTag(TAB_SETTING)
                        if (fragment == null) {
                            fragment = SettingFragment()
                            isCreate = true

                        }
                        replaceFragmentwithTag(fragment, isCreate, TAB_SETTING)

                        return true
                    }
                }
                return false
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
