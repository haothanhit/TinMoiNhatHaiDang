package com.haidang.tinmoinhat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.ads.MobileAds
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.Ads.CommonAds
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_COUNT_ADS_FULL

class SplashActivity : BaseActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 2000 //2 seconds




    internal val mRunnable: Runnable = Runnable {
//        if (!isFinishing) {
//            if (mInterstitialAd.isLoaded) {
//                mInterstitialAd.show()
//                mInterstitialAd.adListener = object : AdListener() {
//                    override fun onAdClosed() {
//                        val intent = Intent(applicationContext, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                        CommonAds().loadAdsFulL(applicationContext)
//
//                    }
//                }
//            } else {
//                val intent = Intent(applicationContext, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//
//
//        }else {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
      //  }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        try {
            MobileAds.initialize(this) {}
          CommonAds().loadAdsFulL(this)
            clearSharedPrefs(KEY_COUNT_ADS_FULL)  //remove count ads full

        } catch (ex: Exception) {
        }

    }

    override fun onResume() {
        super.onResume()
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }
    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

}
