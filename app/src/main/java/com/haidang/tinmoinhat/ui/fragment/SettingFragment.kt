package com.haidang.tinmoinhat.ui.fragment

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.base.BaseFragment
import com.haidang.tinmoinhat.ui.activity.InfoActivity
import com.haidang.tinmoinhat.ui.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnRateUs.setOnClickListener {
            (activity as BaseActivity).openMyApp()
        }
        btnShareWith.setOnClickListener {
            (activity as BaseActivity).share()
        }
        btnFeeBack.setOnClickListener {
            (activity as BaseActivity).feedback()
        }
        btnApplicationTerms.setOnClickListener {
            val intent = Intent(context, InfoActivity::class.java)
            startActivity(intent)
        }

        // handle swith theme
        val preferencesRelate =
            activity!!.getSharedPreferences("theme", AppCompatActivity.MODE_PRIVATE)
        val isTheme = preferencesRelate.getInt("is_theme", 0)
        when (isTheme) {  //0: time , 1 :light ,2:dark
            0 -> {
                toggle_by_time.isChecked = true
                btnChangeDarkLight.visibility = View.GONE
            }
            1 -> {
                toggle_by_time.isChecked = false
                btnChangeDarkLight.visibility = View.VISIBLE
                toggle_dark_light.isChecked = false
            }
            2 -> {
                toggle_by_time.isChecked = false
                btnChangeDarkLight.visibility = View.VISIBLE
                toggle_dark_light.isChecked = true
            }
        }

        toggle_by_time.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btnChangeDarkLight.visibility = View.GONE
                (activity as MainActivity).setModeTheme(AppCompatDelegate.MODE_NIGHT_AUTO_TIME)
            } else {
                btnChangeDarkLight.visibility = View.VISIBLE
                val nightModeFlags = context!!.resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK
                when (nightModeFlags) {
                    UI_MODE_NIGHT_YES -> {
                        toggle_dark_light.isChecked = true
                        (activity as MainActivity).setModeTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        toggle_dark_light.isChecked = false
                        (activity as MainActivity).setModeTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    }


                }

            }
        }
        toggle_dark_light.setOnCheckedChangeListener { view, isChecked ->

            (activity as MainActivity).setModeTheme(if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)

        }

    }

}