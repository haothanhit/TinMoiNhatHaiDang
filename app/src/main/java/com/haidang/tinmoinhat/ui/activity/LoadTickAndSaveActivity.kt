package com.haidang.tinmoinhat.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.adapter.AdapterDetail
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.global.Constants
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
import kotlinx.android.synthetic.main.activity_load_tick_and_save.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadTickAndSaveActivity :BaseActivity() {
    private val TAG: String = LoadTickAndSaveActivity::class.java.simpleName
    private var adapterDetail: AdapterDetail?=null
    private var data: ModelArticle? = null
    val mCompositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_tick_and_save)
        data = intent.getSerializableExtra("Article") as ModelArticle?
        initView()
    }

    @SuppressLint("WrongConstant")
    private fun initView() {
        ivBackLoad.setOnClickListener { onBackPressed() }
        var textTitle = data?.source?.substring(7)  //Bỏ chữ nguồn
        tvTitleLoad.text =
            textTitle?.substring(0, textTitle.lastIndexOf("-")!!)?.trim()//set text title
        var linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        rcvLoadTickOrSave.layoutManager = linearLayoutManager
        rcvLoadTickOrSave.setHasFixedSize(true)
        getData()

        ivSizeTextLoad.setOnClickListener {
            showDialogSetSizeText()
        }
        if(data?.isVideo!!){  // if article have video
            frame_video_TickSave.visibility=View.VISIBLE
            Glide.with(this).load(data?.thumb).into(img_thumbTickSave)
            videoViewTickSave.setVideoURI(Uri.parse(data?.linkVideo))
            media_controllerTickSave.setOnLoadingView(R.layout.custom_load_view)
            videoViewTickSave.setMediaController(media_controllerTickSave)

            button_playTickSave.setOnClickListener{
                button_playTickSave.visibility = View.GONE
                img_thumbTickSave.visibility = View.GONE
                videoViewTickSave.start()
            }

            videoViewTickSave.setVideoViewCallback(object : UniversalVideoView.VideoViewCallback{
                override fun onBufferingStart(mediaPlayer: MediaPlayer?) {
                }

                override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {
                }

                override fun onPause(mediaPlayer: MediaPlayer?) {
                }

                override fun onScaleChange(isFullscreen: Boolean) {
                    if(isFullscreen){
                        val layoutParams: ViewGroup.LayoutParams = frame_video_TickSave.layoutParams
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                        frame_video_TickSave.layoutParams = layoutParams
                        //GONE the unconcerned views to leave room for video and controller
                        rcvLoadTickOrSave.visibility = View.GONE
                        rltHeaderTickSave.visibility = View.GONE
                    }else{
                        val layoutParams: ViewGroup.LayoutParams = frame_video_TickSave.layoutParams
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                        layoutParams.height = resources.getDimension(R.dimen.dimen200dp).toInt()
                        frame_video_TickSave.layoutParams = layoutParams
                        rcvLoadTickOrSave.visibility = View.VISIBLE
                        rltHeaderTickSave.visibility = View.VISIBLE

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
            val sizeCurrent: String = getSharedPrefsString(Constants.KEY_FONT_SIZE)
            when (sizeCurrent) {
                Constants.KEY_SIZE_SMALL -> {
                    llSmallDialog.setBackgroundResource(R.color.colorPrimary)
                    tvSmallDialog.setTextColor(resources.getColor(R.color.white))
                }
                Constants.KEY_SIZE_MEDIUM -> {
                    llMediumDialog.setBackgroundResource(R.color.colorPrimary)
                    tvMediumDialog.setTextColor(resources.getColor(R.color.white))
                }
                Constants.KEY_SIZE_BIG -> {
                    llBigDialog.setBackgroundResource(R.color.colorPrimary)
                    tvBigDialog.setTextColor(resources.getColor(R.color.white))
                }
                Constants.KEY_SIZE_VERY_BIG -> {
                    llVeryBigDialog.setBackgroundResource(R.color.colorPrimary)
                    tvVeryBigDialog.setTextColor(resources.getColor(R.color.white))
                }
                else -> { //default is KEY_SIZE_MEDIUM
                    llMediumDialog.setBackgroundResource(R.color.colorPrimary)
                    tvMediumDialog.setTextColor(resources.getColor(R.color.white))
                }

            }
            llSmallDialog.setOnClickListener {
                saveSharedPrefsString(Constants.KEY_FONT_SIZE, Constants.KEY_SIZE_SMALL)
                rcvLoadTickOrSave.removeAllViews()
                adapterDetail?.notifyDataSetChanged()
                dialog!!.dismiss()
            }
            llMediumDialog.setOnClickListener {
                saveSharedPrefsString(Constants.KEY_FONT_SIZE, Constants.KEY_SIZE_MEDIUM)
                rcvLoadTickOrSave.removeAllViews()
                adapterDetail?.notifyDataSetChanged()
                dialog!!.dismiss()
            }
            llBigDialog.setOnClickListener {
                saveSharedPrefsString(Constants.KEY_FONT_SIZE, Constants.KEY_SIZE_BIG)
                rcvLoadTickOrSave.removeAllViews()
                adapterDetail?.notifyDataSetChanged()
                dialog!!.dismiss()
            }
            llVeryBigDialog.setOnClickListener {
                saveSharedPrefsString(Constants.KEY_FONT_SIZE, Constants.KEY_SIZE_VERY_BIG)
                rcvLoadTickOrSave.removeAllViews()
                adapterDetail?.notifyDataSetChanged()
                dialog!!.dismiss()
            }

            dialog!!.show()
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

                    adapterDetail= AdapterDetail(arrayList,this@LoadTickAndSaveActivity)
                    rcvLoadTickOrSave.adapter = adapterDetail

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