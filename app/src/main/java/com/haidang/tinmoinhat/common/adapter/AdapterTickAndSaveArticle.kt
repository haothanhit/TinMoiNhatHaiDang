package com.haidang.tinmoinhat.common.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.abstractViewHolder.BaseViewHoderPosition
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.model.ModelArticle
import com.haidang.tinmoinhat.ui.activity.LoadTickAndSaveActivity
import kotlinx.android.synthetic.main.custom_item_tick_and_save_article.view.*

class AdapterTickAndSaveArticle(
    val arrArticle: ArrayList<ModelArticle>,
    val activity: Activity,
    val tickOrSave: String
) :
    RecyclerView.Adapter<BaseViewHoderPosition<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHoderPosition<*> {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_item_tick_and_save_article, parent, false)
        return MyViewHolder(v)

    }


    inner class MyViewHolder(itemView: View) : BaseViewHoderPosition<ModelArticle>(itemView) {
        override fun bind(item: ModelArticle,pos:Int) {
            Glide.with(activity).load(item?.thumb).placeholder(R.drawable.img_place_holder)
                .into(itemView.img_save)
            itemView.txt_title_save.text = item?.title
            itemView.txt_source_save.text = item?.source

            itemView.setOnClickListener { //on click
                val intent = Intent(activity, LoadTickAndSaveActivity::class.java)
                intent.putExtra("Article", item)
                activity.startActivity(intent)

            }
            itemView.ivPlayVideoTickSave?.visibility=if(item?.isVideo!!) View.VISIBLE else View.GONE

            itemView.bt_delete_save.setOnClickListener {                 //remove 1 article

                val json =(activity as BaseActivity).getSharedPrefsString(tickOrSave)
                val type = object : TypeToken<ArrayList<ModelArticle>>() {}.type
                if (json != "") {
                    var arrayList = ArrayList<ModelArticle>()
                    try {
                        arrayList = Gson().fromJson(json, type)

                    } catch (ex: Exception) {
                        var a: ModelArticle =
                            Gson().fromJson(json, object : TypeToken<ModelArticle>() {}.type)
                        arrayList.add(a)
                    }
                    if (arrayList.size > 0) {
                        for( itArticle in arrayList)
                        {
                            if(itArticle.id.equals(item.id)) //  bai bao hien tai can xoa
                            {
                                arrayList.remove(itArticle)
                                break
                            }
                        }
                    }
                   val jsonSave = Gson().toJson(arrayList)
                (activity as BaseActivity).saveSharedPrefsString(tickOrSave, jsonSave)
                }

                    arrArticle.removeAt(pos)
                    notifyItemRemoved(pos)
                    notifyItemRangeChanged(pos, arrArticle.size)



//                Toast.makeText(activity, "Đã Xóa!", Toast.LENGTH_SHORT).show()
            }

        }
    }


    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrArticle.size
    }
    //this method is giving the size of the list
     fun clearData() {
        arrArticle.clear()
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(holder: BaseViewHoderPosition<*>, position: Int) {
        if (holder is MyViewHolder) {
            holder.bind(arrArticle[position],position)
        }
    }

}
