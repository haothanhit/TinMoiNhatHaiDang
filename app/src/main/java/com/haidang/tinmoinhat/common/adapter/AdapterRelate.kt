package com.haidang.tinmoinhat.common.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.abstractViewHolder.BaseViewHolder
import com.haidang.tinmoinhat.common.model.ModelArticle
import com.haidang.tinmoinhat.ui.activity.DetailActivity
import kotlinx.android.synthetic.main.custom_item_relate.view.*


class AdapterRelate(val arrArticle: ArrayList<ModelArticle>,val activity:Activity) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_item_relate, parent, false)
        return MyViewHolder(v)

    }


    inner class MyViewHolder(itemView: View) : BaseViewHolder<ModelArticle>(itemView) {
        override fun bind(item: ModelArticle) {
            itemView.txt_title_relate.text = item?.title
            itemView.txt_source_relate.text = item?.source
            Glide.with(activity).load(item?.thumb).placeholder(R.drawable.img_place_holder)
                .into(itemView.img_relate)
            itemView.card_view_item_Relate.setOnClickListener { //on click
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("Article", item)
                activity.startActivity(intent)
                activity.finish()

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
        return position
    }


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is MyViewHolder) {
            holder.bind(arrArticle[position])
        }
    }

}
