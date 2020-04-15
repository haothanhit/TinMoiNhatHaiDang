package com.haidang.tinmoinhat.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.adapter.AdapterDetail
import com.haidang.tinmoinhat.common.adapter.AdapterRelate
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_FONT_SIZE
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_RECENT_READING
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_RELATE
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_SAVE_ARTICLE
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_SIZE_BIG
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_SIZE_MEDIUM
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_SIZE_SMALL
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_SIZE_VERY_BIG
import com.haidang.tinmoinhat.common.model.ModelArticle
import com.haidang.tinmoinhat.common.model.ModelContent
import com.haidang.tinmoinhat.common.retrofit.APIClientDetail
import com.haidang.tinmoinhat.common.retrofit.APIInterface
import com.universalvideoview.UniversalVideoView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BooleanSupplier
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class DetailActivity : BaseActivity() {
    private val TAG: String = DetailActivity::class.java.simpleName
    private var data: ModelArticle? = null
    private var isArticleSaved: Boolean = false
    private var adapterDetail:AdapterDetail?=null
    val mCompositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (currentIsNight()) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
        setContentView(R.layout.activity_detail)
        data = intent.getSerializableExtra("Article") as ModelArticle?
        initView()
    }

    @SuppressLint("WrongConstant")
    private fun initView() {
        ivBackDetail.setOnClickListener { onBackPressed() }
        var textTitle = data?.source?.substring(7)  //Bỏ chữ nguồn
        tvTitleDetail.text =
            textTitle?.substring(0, textTitle.lastIndexOf("-")!!)?.trim()//set text title
        ivLoveArticle.setOnClickListener { saveOrRemoveArticle(isArticleSaved) }
        //AdView
        val adRequest = AdRequest.Builder().build()
        adView_detail.loadAd(adRequest)
        //Layout detail
        var linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycler_view_details.layoutManager = linearLayoutManager
        recycler_view_details.setHasFixedSize(true)
        getData()
        //Layout relate
        recycler_view_relate.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycler_view_relate.setHasFixedSize(true)
        val dividerHorizontal =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        recycler_view_relate.addItemDecoration(dividerHorizontal)
        loadRelate()
        saveRecentReading()
        getSateSaveArticle()
        setStateImageArticleSaved()
        ivSizeText.setOnClickListener {
            showDialogSetSizeText()
        }
        if(data?.isVideo!!){  // if article have video
            frame_video.visibility=View.VISIBLE
            Glide.with(this).load(data?.thumb).into(img_thumb)
            videoView.setVideoURI(Uri.parse(data?.linkVideo))
            media_controller.setOnLoadingView(R.layout.custom_load_view)
            videoView.setMediaController(media_controller)

            button_play.setOnClickListener{
                button_play.visibility = View.GONE
                img_thumb.visibility = View.GONE
                videoView.start()
            }
            videoView.setVideoViewCallback(object :UniversalVideoView.VideoViewCallback{
                override fun onBufferingStart(mediaPlayer: MediaPlayer?) {
                }

                override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {
                }

                override fun onPause(mediaPlayer: MediaPlayer?) {
                }

                override fun onScaleChange(isFullscreen: Boolean) {
                    if(isFullscreen){
                        val layoutParams: ViewGroup.LayoutParams = frame_video.layoutParams
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                        frame_video.layoutParams = layoutParams
                        //GONE the unconcerned views to leave room for video and controller
                        frame_item.visibility = View.GONE
                        rltHeaderDetail.visibility = View.GONE
                    }else{
                        val layoutParams: ViewGroup.LayoutParams = frame_video.layoutParams
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                        layoutParams.height = resources.getDimension(R.dimen.dimen200dp).toInt()
                        frame_video.layoutParams = layoutParams
                        frame_item.visibility = View.VISIBLE
                        rltHeaderDetail.visibility = View.VISIBLE

                    }
                }

                override fun onStart(mediaPlayer: MediaPlayer?) {
                }
            })
        }
    }

    var dialog: Dialog? = null  //show dialog

    @SuppressLint("ResourceType")
    private fun showDialogSetSizeText() {
        if (dialog != null && dialog!!.isShowing) dialog!!.dismiss()
        else {
            dialog = Dialog(this!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(true)
            dialog!!.setCanceledOnTouchOutside(true)
            dialog!!.setContentView(R.layout.dialog_text_size)
            val window: Window = dialog!!.window!!
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
            val wlp: WindowManager.LayoutParams = window.attributes
            wlp.gravity = Gravity.BOTTOM
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            window.attributes = wlp
            //init view
            val llSmallDialog = dialog!!.findViewById(R.id.llSmallDialog) as LinearLayout
            val tvSmallDialog = dialog!!.findViewById(R.id.tvSmallDialog) as TextView
            val llMediumDialog = dialog!!.findViewById(R.id.llMediumDialog) as LinearLayout
            val tvMediumDialog = dialog!!.findViewById(R.id.tvMediumDialog) as TextView
            val llBigDialog = dialog!!.findViewById(R.id.llBigDialog) as LinearLayout
            val tvBigDialog = dialog!!.findViewById(R.id.tvBigDialog) as TextView
            val llVeryBigDialog = dialog!!.findViewById(R.id.llVeryBigDialog) as LinearLayout
            val tvVeryBigDialog = dialog!!.findViewById(R.id.tvVeryBigDialog) as TextView
            val btnCloseDialog = dialog!!.findViewById(R.id.btnCloseDialog) as Button
            btnCloseDialog.setOnClickListener { dialog!!.dismiss() }
            //get current size
            val sizeCurrent: String = getSharedPrefsString(KEY_FONT_SIZE)
            when (sizeCurrent) {
                KEY_SIZE_SMALL -> {
                    llSmallDialog.setBackgroundResource(R.color.colorPrimary)
                    tvSmallDialog.setTextColor(resources.getColor(R.color.white))
                }
                KEY_SIZE_MEDIUM -> {
                    llMediumDialog.setBackgroundResource(R.color.colorPrimary)
                    tvMediumDialog.setTextColor(resources.getColor(R.color.white))
                }
                KEY_SIZE_BIG -> {
                    llBigDialog.setBackgroundResource(R.color.colorPrimary)
                    tvBigDialog.setTextColor(resources.getColor(R.color.white))
                }
                KEY_SIZE_VERY_BIG -> {
                    llVeryBigDialog.setBackgroundResource(R.color.colorPrimary)
                    tvVeryBigDialog.setTextColor(resources.getColor(R.color.white))
                }
                else -> { //default is KEY_SIZE_MEDIUM
                    llMediumDialog.setBackgroundResource(R.color.colorPrimary)
                    tvMediumDialog.setTextColor(resources.getColor(R.color.white))
                }

            }
            llSmallDialog.setOnClickListener {
                saveSharedPrefsString(KEY_FONT_SIZE, KEY_SIZE_SMALL)
                recycler_view_details.removeAllViews()
                adapterDetail?.notifyDataSetChanged()
                dialog!!.dismiss()
            }
            llMediumDialog.setOnClickListener {
                saveSharedPrefsString(KEY_FONT_SIZE, KEY_SIZE_MEDIUM)
                recycler_view_details.removeAllViews()
                adapterDetail?.notifyDataSetChanged()
                dialog!!.dismiss()
            }
            llBigDialog.setOnClickListener {
                saveSharedPrefsString(KEY_FONT_SIZE, KEY_SIZE_BIG)
                recycler_view_details.removeAllViews()
                adapterDetail?.notifyDataSetChanged()
                dialog!!.dismiss()
            }
            llVeryBigDialog.setOnClickListener {
                saveSharedPrefsString(KEY_FONT_SIZE, KEY_SIZE_VERY_BIG)
                recycler_view_details.removeAllViews()
                adapterDetail?.notifyDataSetChanged()
                dialog!!.dismiss()
            }

            dialog!!.show()
        }
    }

    private fun getSateSaveArticle() {  //kiem tra xem bai viet nay da duoc luu chưa thay doi image
        val typeRecent = object : TypeToken<ArrayList<ModelArticle?>?>() {}.type
        val jsonSave: String = getSharedPrefsString(KEY_SAVE_ARTICLE)
        if (jsonSave != "") {
            var arrayListSave = ArrayList<ModelArticle>()
            try {
                arrayListSave = Gson().fromJson(jsonSave, typeRecent)

            } catch (ex: java.lang.Exception) {
                var a: ModelArticle =
                    Gson().fromJson(jsonSave, object : TypeToken<ModelArticle>() {}.type)
                arrayListSave.add(a)
            }
            for (i in arrayListSave) {
                if (i.id.equals(data?.id)) { //neu tin nay da luu thi k luu
                    isArticleSaved = true
                    break
                }
            }
        }

    }

    private fun setStateImageArticleSaved() {
        if (isArticleSaved) {   // if article saved
            ivLoveArticle.setImageResource(R.drawable.ic_loved)
        } else {
            ivLoveArticle.setImageResource(R.drawable.ic_love)
        }
    }

    private fun saveOrRemoveArticle(state: Boolean) {//luu lai hoac xoa tin da luu
        val typeRecent = object : TypeToken<ArrayList<ModelArticle?>?>() {}.type
        val jsonSave: String = getSharedPrefsString(KEY_SAVE_ARTICLE)
        var arrayListSave = ArrayList<ModelArticle>()
        if (state) {  //xoa tin da luu
            if (jsonSave != "") {
                try {
                    arrayListSave = Gson().fromJson(jsonSave, typeRecent)

                } catch (ex: java.lang.Exception) {
                    var a: ModelArticle =
                        Gson().fromJson(jsonSave, object : TypeToken<ModelArticle>() {}.type)
                    arrayListSave.add(a)
                }
                for (i in arrayListSave) {
                    if (i.id.equals(data?.id)) { //neu tin nay da luu thi xoa
                        arrayListSave.remove(i)
                        break
                    }
                }
            }
            val json1 = Gson().toJson(arrayListSave)
            saveSharedPrefsString(KEY_SAVE_ARTICLE, json1)
            isArticleSaved = false

        } else { //them tin da luu
            var isSaved = true
            if (jsonSave != "") {
                try {
                    arrayListSave = Gson().fromJson(jsonSave, typeRecent)

                } catch (ex: java.lang.Exception) {
                    var a: ModelArticle =
                        Gson().fromJson(jsonSave, object : TypeToken<ModelArticle>() {}.type)
                    arrayListSave.add(a)
                }
                for (i in arrayListSave) {
                    if (i.id.equals(data?.id)) { //neu tin nay da luu thi k luu
                        isSaved = false
                        break
                    }
                }
            }
            arrayListSave.add(data!!)
            if (isSaved) {   // if article not saved
                val json1 = Gson().toJson(arrayListSave)
                saveSharedPrefsString(KEY_SAVE_ARTICLE, json1)
            }
            isArticleSaved = true
        }
        setStateImageArticleSaved()


    }

    private fun saveRecentReading() {  //luu lai tin doc gan day
        //save RecentReading
        val appSharedPrefs: SharedPreferences =
            this.getSharedPreferences(KEY_RECENT_READING, Context.MODE_PRIVATE)
        val prefsEditor = appSharedPrefs.edit()
        val typeRecent = object : TypeToken<ArrayList<ModelArticle?>?>() {}.type
        val jsonSave: String = appSharedPrefs.getString(KEY_RECENT_READING, "").toString()
        var arrayListSave = ArrayList<ModelArticle>()
        var isSaved = true
        if (jsonSave != "") {
            try {
                arrayListSave = Gson().fromJson(jsonSave, typeRecent)

            } catch (ex: java.lang.Exception) {
                var a: ModelArticle =
                    Gson().fromJson(jsonSave, object : TypeToken<ModelArticle>() {}.type)
                arrayListSave.add(a)
            }
            for (i in arrayListSave) {
                if (i.id.equals(data?.id)) {//neu tin nay da luu thi k luu
                    isSaved = false
                    break
                }
            }
        }
        arrayListSave.add(data!!)
        if (isSaved) {   // if article not saved
            val json1 = Gson().toJson(arrayListSave)
            prefsEditor.putString(KEY_RECENT_READING, json1).apply()
        }
    }

    private fun loadRelate() {
        val gson = Gson()
        val json = getSharedPrefsString(KEY_RELATE)
        val type = object : TypeToken<ArrayList<ModelArticle>>() {}.type
        if (json != "") {

            //Layout relate
            val arrayList = ArrayList<ModelArticle>()
            var arrayListRelate = ArrayList<ModelArticle>()
            try {
                arrayListRelate = gson.fromJson(json, type)

            } catch (ex: Exception) {
                var a: ModelArticle =
                    gson.fromJson(json, object : TypeToken<ModelArticle>() {}.type)
                arrayListRelate.add(a)
            }
            if (arrayListRelate.size > 1) {
                var i = 0
                while (arrayList.size < 7) {  //tong so bai viet lien quan se hien
                    val ran = Random()
                    val pos = ran.nextInt(arrayListRelate.size - 1)
                    if (!arrayListRelate.get(pos).id.equals(data?.id)) { //bo show bai viet hien tai
                        arrayList.add(arrayListRelate.get(pos))
                        arrayListRelate.removeAt(pos)
                    }
                    i++
                }
            }
            recycler_view_relate.adapter = AdapterRelate(arrayList, this)

        }


    }
    var getDataDone:Boolean=false

    fun getData() {
       showProgress()
        val request = APIClientDetail.getClient(APIInterface::class.java)
        var disposable = request.getNews(data?.id!!)
            .retryUntil(BooleanSupplier { getDataDone })
            .observeOn(AndroidSchedulers.mainThread())  // handle the results in the ui thread
            .subscribeOn(Schedulers.io()) // execute the call asynchronously
            .subscribe({ success ->
                success?.let {
                  hideProgress()
                    getDataDone=true
                    var arrayList = ArrayList<ModelContent>()
                    val document: org.jsoup.nodes.Document = Jsoup.parse(it)
                    val element: Element = document.select("div.content-detail").first()

                    if (element != null) {
                        val elements: Elements = element.select("p")
                        val title: String = document.select("h3.title").text()
                        // val source: String = document.select("p").first().text()
                        arrayList.add(ModelContent(title, ""))
                        //  arrayList.add(ModelContent(source, ""))

                        //API mới
                        val shot: String
                        if (document.select("p.title").first() != null) {
                            shot = document.select("p.title").first().text()
                            arrayList.add(ModelContent(shot, ""))
                        }
                        for (e in elements) {
                            var text = ""
                            var img = ""
                            if (e.getElementsByTag("img").first() != null) {
                                img = e.getElementsByTag("img").first().attr("src")
                            }
                            if (img == "") {
                                text = e.text()
                            }
                            arrayList.add(ModelContent(text, img))
                        }
                    }

                    adapterDetail= AdapterDetail(arrayList,this@DetailActivity)
                    recycler_view_details.adapter = adapterDetail
                    frame_item.visibility = View.VISIBLE

                }
            }, { t ->

            })
        mCompositeDisposable.add(disposable)

    }
    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

}