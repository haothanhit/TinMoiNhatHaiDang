package com.haidang.tinmoinhat.common.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.gson.Gson
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.abstractViewHolder.BaseViewHolder
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.global.Constants.Companion.KEY_RELATE
import com.haidang.tinmoinhat.common.model.ModelArticle
import com.haidang.tinmoinhat.ui.activity.DetailActivity
import kotlinx.android.synthetic.main.custom_item_recycler_view_ads.view.*
import kotlinx.android.synthetic.main.custom_item_recycler_view_main.view.*


class AdapterMain(val arrArticle: ArrayList<ModelArticle>, val activity: Activity) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val VIEW_ITEM = 1
    private val VIEW_ADS = 2
    private val VIEW_ITEM_1 = 3
    private val VIEW_ITEM_FIRST=4
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        if (viewType == VIEW_ITEM) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_recycler_view_main, parent, false)
            return MyViewHolder(v)
        }else if(viewType==VIEW_ITEM_1){
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_recycler_view_main_1, parent, false)
            return MyViewHolder(v)
        }
        else if(viewType==VIEW_ITEM_FIRST){
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_recycler_view_main_first, parent, false)
            return MyViewHolder(v)
        }
        else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_recycler_view_ads, parent, false)
            return AdHolder(v)
        }
    }




    inner class MyViewHolder(itemView: View) : BaseViewHolder<ModelArticle>(itemView) {
        override fun bind(item: ModelArticle) {
            Glide.with(activity).load(item?.thumb).placeholder(R.drawable.img_place_holder)
                .into(itemView.img_item_main)
            itemView.txt_title_item_main.text = item?.title
            itemView.txt_source_item_main.text = item?.source
            itemView.ivPlayVideoMain?.visibility=if(item?.isVideo!!) View.VISIBLE else View.GONE
            itemView.setOnClickListener { //on click
                //save relate
                val json = Gson().toJson(arrArticle)
                (activity as BaseActivity).saveSharedPrefsString(KEY_RELATE, json)
                //Switch screen
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("Article", item)
                activity.startActivity(intent)

            }

        }
    }

    inner class AdHolder(itemView: View) : BaseViewHolder<ModelArticle>(itemView) {
        override fun bind(item: ModelArticle) {
            Glide.with(activity).load(item?.thumb).placeholder(R.drawable.img_place_holder)
                .into(itemView.img_item_main_ads)
            val adRequest = AdRequest.Builder().build()
            itemView.ad_view_main_ads.loadAd(adRequest)
            itemView.txt_title_item_main_ads.text = item?.title
            itemView.txt_source_item_main_ads.text = item?.source
            itemView.ivPlayVideoMainAds?.visibility=if(item?.isVideo!!) View.VISIBLE else View.GONE
            itemView.setOnClickListener {  //on click
                //save relate
                val json = Gson().toJson(arrArticle)
                (activity as BaseActivity).saveSharedPrefsString(KEY_RELATE, json)
                //Switch screen
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("Article", item)
                activity.startActivity(intent)

            }

        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrArticle.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    public fun addData(list: ArrayList<ModelArticle>) {
        arrArticle.addAll(list)
        notifyDataSetChanged()

    }

    public fun clearData() {
        arrArticle?.clear()
        //notifyDataSetChanged()

    }

    override fun getItemViewType(position: Int): Int {
        if(position==0) return VIEW_ITEM_FIRST
       return  when(position%12){
            0->VIEW_ADS
            5,10->VIEW_ITEM_1
            else->VIEW_ITEM
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is AdHolder) {
            holder.bind(arrArticle[position])

        } else if (holder is MyViewHolder) {
            holder.bind(arrArticle[position])

        }
    }

}
