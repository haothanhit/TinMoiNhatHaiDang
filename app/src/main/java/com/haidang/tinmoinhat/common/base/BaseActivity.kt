package com.haidang.tinmoinhat.common.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.dialog.LoadingDialog
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_THEME
import com.haidang.tinmoinhat.common.global.Constants.Companion.getALLTopic
import com.haidang.tinmoinhat.common.model.ModelTopic


abstract class BaseActivity : AppCompatActivity() {
    lateinit var currentFragment: Fragment
    var listAllTopic: ArrayList<ModelTopic>? = getALLTopic()  // tat ca danh sach chu de
    var listTopicCurrent: ArrayList<ModelTopic>? =
        getALLTopic()  // danh sach chu de nguoi dung da chon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor= if (currentIsNight()) ContextCompat.getColor(this, R.color.black) else  ContextCompat.getColor(this, R.color.white)
//        }
        val preferencesRelate =
            this.getSharedPreferences(KEY_THEME, MODE_PRIVATE)
        val isTheme = preferencesRelate.getInt(KEY_THEME, 1)
        setModeTheme(isTheme)

    }

    fun getNameTopic(id: String): String { //false :light ,true :dark=night
        if (listAllTopic == null) listAllTopic = getALLTopic()
        var name = ""
        for (item in listAllTopic!!) {
            if (id == item.id) {
                name = item.name!!
                break
            }
        }
        return name
    }

    fun currentIsNight(): Boolean { //false :light ,true :dark=night
        val nightModeFlags =
            this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES

    }

    fun setModeTheme(mode: Int) { //0: time , 1 :light ,2:dark
        val appSharedPrefs: SharedPreferences =    //save local mode Theme
            this.getSharedPreferences(KEY_THEME, Context.MODE_PRIVATE)
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putInt(KEY_THEME, mode).apply()

        AppCompatDelegate.setDefaultNightMode(mode)

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    /**
     * replace fragment
     */
    fun replaceFragment(@IdRes containerId: Int, fragment: Fragment, addToBackStack: Boolean) {
        val fragmentTag = fragment::class.simpleName
        currentFragment = fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //   fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,R.anim.pop_enter, R.anim.pop_exit)
        fragmentTransaction.replace(containerId, fragment, fragmentTag)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragmentTag)
        }
        fragmentTransaction.commit()
    }

    /**
     * show fragment when change tab
     */
    fun showFragment(showFragment: Fragment) {
        supportFragmentManager.beginTransaction().show(showFragment).hide(currentFragment).commit();
        currentFragment = showFragment;
    }

    /**
     * save  SharedPreferences type String
     */
    fun saveSharedPrefsString(key: String, value: String) {
        val appSharedPrefs: SharedPreferences =
            this.getSharedPreferences(key, Context.MODE_PRIVATE)
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putString(key, value).apply()
    }

    /**
     * get  SharedPreferences type String
     */
    fun getSharedPrefsString(key: String): String {
        val preferencesRelate = getSharedPreferences(key, MODE_PRIVATE)
        return preferencesRelate.getString(key, "")!!
    }

    /**
     * get  SharedPreferences type Int
     */
    fun getSharedPrefsInt(key: String): Int {
        val preferencesRelate = getSharedPreferences(key, MODE_PRIVATE)
        return preferencesRelate.getInt(key, 0)!!
    }

    /**
     * save  SharedPreferences type Int
     */
    fun saveSharedPrefsInt(key: String, value: Int) {
        val appSharedPrefs: SharedPreferences =
            this.getSharedPreferences(key, Context.MODE_PRIVATE)
        val prefsEditor = appSharedPrefs.edit()
        prefsEditor.putInt(key, value).apply()
    }

    /**
     * clear  SharedPreferences
     */
    fun clearSharedPrefs(key: String) {
        val preferencesRelate = getSharedPreferences(key, MODE_PRIVATE)
        val prefsEditor = preferencesRelate.edit()
        prefsEditor.clear().apply()
    }

    /**
     * remove all back stack
     */
    fun removeAllBackStack() {
        val fm = this.supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fm.executePendingTransactions()
        }
    }

    /**
     * change status bar color
     */
    fun changeStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = resources.getColor(color, theme)
            } else {
                window.statusBarColor = resources.getColor(color)
            }
        }
    }

    //-------------share app
    fun share() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                " Hey, look this app ! \n https://play.google.com/store/apps/details?id=com.haidang.tinmoinhat"
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share this app with your friends !")
        startActivity(shareIntent)
    }

    //-----------------open My App  on play store
    fun openMyApp() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=com.haidang.tinmoinhat")
            )
        )
    }

    fun feedback() {
        var deviceInfo = "System Info:"
        deviceInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")"
        deviceInfo += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT
        deviceInfo += "\n Device: " + android.os.Build.DEVICE
        deviceInfo += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")"
        val emailIntent =
            Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "tinhochuutri@gmail.com", null)
            )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject))
        emailIntent.putExtra(Intent.EXTRA_TEXT, deviceInfo)
        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    fun stopApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private var mDlg: Dialog? = null

    fun showProgress() {
        try {
            hideProgress()
            mDlg = LoadingDialog(this)
            mDlg?.show()
        } catch (ignored: Throwable) {
            ignored.printStackTrace()
        }
    }

    fun hideProgress() {
        if (mDlg != null) {
            mDlg?.dismiss()
            mDlg = null
        }
    }
}