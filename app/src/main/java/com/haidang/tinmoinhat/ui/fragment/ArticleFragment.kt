package com.haidang.tinmoinhat.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.adapter.AdapterMain
import com.haidang.tinmoinhat.common.base.BaseFragment
import com.haidang.tinmoinhat.common.listener.EndlessRecyclerViewScrollListener
import com.haidang.tinmoinhat.common.model.ModelArticle
import com.haidang.tinmoinhat.common.retrofit.APIClient
import com.haidang.tinmoinhat.common.retrofit.APIInterface
import com.haidang.tinmoinhat.ui.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_acticle.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class ArticleFragment : BaseFragment() {
    private var tabTitle: String? = ""
    private var category: String? = ""
    private var tabID: String? = ""
    private val TAG:String =ArticleFragment:: class.java.getSimpleName()
    private var mAdaper:AdapterMain?=null
    companion object {
        @JvmStatic
        fun newInstance(mTabID: String, mTabTitle: String, mCategory: String) =
            ArticleFragment().apply {
                arguments = Bundle().apply {
                    putString("tabID", mTabID)
                    putString("tabTitle", mTabTitle)
                    putString("category", mCategory)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString("tabID")?.let { tabID = it }
        arguments?.getString("tabTitle")?.let { tabTitle = it }
        arguments?.getString("category")?.let { category = it }
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

        var  linearLayoutManager= LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        rcvArticle.layoutManager =linearLayoutManager
        rcvArticle.setHasFixedSize(true)
        //Chèn một kẻ ngang giữa các phần tử
        val dividerHorizontal =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rcvArticle.addItemDecoration(dividerHorizontal)
        getData("0")            //get data first
        //load more
        val endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?
                ) {
                    getData(page.toString())
                }
            }
              //add Listener
        rcvArticle.addOnScrollListener(endlessRecyclerViewScrollListener)
        //refresh data
        swipe_to_refresh.setOnRefreshListener {
            mAdaper!!.clearData()
            getData("0")

        }

    }
   fun  getData(page:String){
       val request = APIClient.getClient(APIInterface::class.java)
       val call = request.getArticle(tabID.toString(), page)
       Log.d(TAG, call.request().url.toString())

       call.enqueue(object : Callback<ArrayList<ModelArticle>> {
           override fun onResponse(call: Call<ArrayList<ModelArticle>>, response: Response<ArrayList<ModelArticle>>)
           {
               try {
                   (activity as MainActivity).setTabBottom(R.id.navigation_news)

               }catch (ex:Exception){}
               if(response.isSuccessful &&response.body()!=null){

                   try {
                       if(swipe_to_refresh.isRefreshing){
                           swipe_to_refresh.isRefreshing=false
                           mAdaper?.clearData()
                       }
                       if(mAdaper==null) {
                           mAdaper=AdapterMain(response.body()!!,activity!!)
                           rcvArticle.adapter = mAdaper
                       }else{
                           mAdaper!!.addData(response.body()!!)
                       }

                   }catch (ex:Exception){
                       Log.e(TAG, ex.message)
                   }
               }
           }
           override fun onFailure(call: Call<ArrayList<ModelArticle>>, t: Throwable) {
               Log.e(TAG, t.message)
           }
       })
   }
}