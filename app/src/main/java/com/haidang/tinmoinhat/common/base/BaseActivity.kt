package com.haidang.tinmoinhat.common.base

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.haidang.tinmoinhat.R


abstract class BaseActivity : AppCompatActivity() {
    lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.startusColor)
        }
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


}