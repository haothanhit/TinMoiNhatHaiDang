package com.haidang.tinmoinhat.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.adapter.AdapterMain
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.base.BaseFragment
import com.haidang.tinmoinhat.common.listener.EndlessRecyclerViewScrollListener
import com.haidang.tinmoinhat.common.retrofit.APIClient
import com.haidang.tinmoinhat.common.retrofit.APIInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BooleanSupplier
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_acticle.*


class ArticleFragment : BaseFragment() {
    private var tabID: String? = ""
    private val TAG: String = ArticleFragment::class.java.simpleName
    private var mAdaper: AdapterMain? = null
    val mCompositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    companion object {
        @JvmStatic
        fun newInstance(mTabID: String) =
            ArticleFragment().apply {
                arguments = Bundle().apply {
                    putString("tabID", mTabID)

                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString("tabID")?.let { tabID = it }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_acticle, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @SuppressLint("WrongConstant")
    private fun initView() {

        var linearLayoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        rcvArticle.layoutManager = linearLayoutManager
        rcvArticle.setHasFixedSize(true)
        //Chèn một kẻ ngang giữa các phần tử
        val dividerHorizontal =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rcvArticle.addItemDecoration(dividerHorizontal)
        rcvArticle.showShimmerAdapter()
        //load more
        val endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(
                    page: Int, totalItemsCount: Int, view: RecyclerView?
                ) {
                    getData(page.toString())
                }
            }
        //add Listener
        rcvArticle.addOnScrollListener(endlessRecyclerViewScrollListener)
        //refresh data
        swipe_to_refresh.setOnRefreshListener {
            mAdaper?.clearData()
            getData("0")
            swipe_to_refresh.isRefreshing = false

        }
        getData("0")            //get data first

    }
   var getDataDone:Boolean=false
    fun getData(page: String) {
        (activity as BaseActivity).showProgress()
        val request = APIClient.getClient(APIInterface::class.java)
        var disposable = request.getArticle(tabID.toString(), page)
            .retryUntil(BooleanSupplier { getDataDone })
            .observeOn(AndroidSchedulers.mainThread())  // handle the results in the ui thread
            .subscribeOn(Schedulers.io()) // execute the call asynchronously
            .subscribe({ success ->
                success?.let {
                    (activity as BaseActivity).hideProgress()
                    getDataDone=true
                    try {
                        if (mAdaper == null) {
                            mAdaper = AdapterMain(it, activity!!)
                            rcvArticle.adapter = mAdaper
                        } else {
                            var posReal=mAdaper?.itemCount
                            mAdaper!!.addData(it)
                            try {
                                rcvArticle.scrollToPosition(posReal!!-5)
                            }catch (ex:java.lang.Exception){}
                        }

                    } catch (ex: Exception) {
                        Log.e(TAG, ex.message)
                    }
                    rcvArticle.hideShimmerAdapter()


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