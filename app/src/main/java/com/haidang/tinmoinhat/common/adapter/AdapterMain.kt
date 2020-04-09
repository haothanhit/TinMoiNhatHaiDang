package com.haidang.tinmoinhat.common.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.gson.Gson
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.abstractViewHolder.BaseViewHolder
import com.haidang.tinmoinhat.common.model.ModelArticle
import com.haidang.tinmoinhat.ui.activity.DetailActivity
import kotlinx.android.synthetic.main.custom_item_recycler_view_ads.view.*
import kotlinx.android.synthetic.main.custom_item_recycler_view_main.view.*


class AdapterMain(val arrArticle: ArrayList<ModelArticle>,val activity: Activity) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val VIEW_ITEM = 1
    private val VIEW_ADS = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        if (viewType == VIEW_ITEM) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_recycler_view_main, parent, false)
            return MyViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_recycler_view_ads, parent, false)
            return AdHolder(v)
        }
    }


    inner class MyViewHolder(itemView: View) : BaseViewHolder<ModelArticle>(itemView) {
        override fun bind(item: ModelArticle) {
            itemView.txt_title_item_main.text = item?.title
            itemView.txt_source_item_main.text = item?.source
            Glide.with(activity).load(item?.thumb).placeholder(R.drawable.img_place_holder)
                .into(itemView.img_item_main)
            itemView.card_view_item.setOnClickListener { //on click
                //save relate
                val appSharedPrefs: SharedPreferences =
                    activity.getSharedPreferences("relate", Context.MODE_PRIVATE)
                val prefsEditor = appSharedPrefs.edit()
                val gson = Gson()
                val json = gson.toJson(arrArticle)
                prefsEditor.putString("relate", json)
                prefsEditor.commit()
                prefsEditor.apply()

                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("Article", item)
                activity.startActivity(intent)

            }

        }
    }

    inner class AdHolder(itemView: View) : BaseViewHolder<ModelArticle>(itemView) {
        override fun bind(item: ModelArticle) {
            val adRequest = AdRequest.Builder().build()
            itemView.ad_view_main_ads.loadAd(adRequest)
            itemView.txt_title_item_main_ads.text = item?.title
            itemView.txt_source_item_main_ads.text = item?.source
            Glide.with(activity).load(item?.thumb).placeholder(R.drawable.img_place_holder)
                .into(itemView.img_item_main_ads)
            itemView.card_view_item_ads.setOnClickListener {  //on click
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

    override fun getItemViewType(position: Int): Int {
        return if (position % 6 == 0 && position != 0) {
            VIEW_ADS
        } else VIEW_ITEM
    }


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is AdHolder) {
            holder.bind(arrArticle[position])

        } else if (holder is MyViewHolder) {
            holder.bind(arrArticle[position])

        }
    }

}
