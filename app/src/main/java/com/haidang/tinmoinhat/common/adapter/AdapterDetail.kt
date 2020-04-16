package com.haidang.tinmoinhat.common.adapter

import android.app.Activity
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.haidang.tinmoinhat.R
import com.haidang.tinmoinhat.common.abstractViewHolder.BaseViewHoderPosition
import com.haidang.tinmoinhat.common.base.BaseActivity
import com.haidang.tinmoinhat.common.global.Constants
import com.haidang.tinmoinhat.common.model.ModelContent
import kotlinx.android.synthetic.main.custom_item_detail.view.*
import kotlinx.android.synthetic.main.custom_item_detail_ads.view.*

class AdapterDetail(val arrContent: ArrayList<ModelContent>, val activity: Activity) :
    RecyclerView.Adapter<BaseViewHoderPosition<*>>() {
    private val VIEW_ITEM = 1
    private val VIEW_ADS = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHoderPosition<*> {
        if (viewType == VIEW_ITEM) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_detail, parent, false)
            return MyViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_item_detail_ads, parent, false)
            return AdHolder(v)
        }
    }


    inner class MyViewHolder(itemView: View) : BaseViewHoderPosition<ModelContent>(itemView) {
        override fun bind(item: ModelContent, pos: Int) {
            if (item?.text.equals("")) {
                if (!item?.img.equals("")) {
                    Glide.with(itemView.context).load(item?.img).into((itemView.img_detail))
                    itemView.img_detail.visibility = View.VISIBLE
                    itemView.txt_detail.visibility = View.GONE
                }

            }
            if (item?.img.equals("")) {
                itemView.txt_detail.text = item?.text
                itemView.txt_detail.visibility = View.VISIBLE
                when ((activity as BaseActivity).getSharedPrefsString(Constants.KEY_FONT_SIZE)) {
                    Constants.KEY_SIZE_SMALL -> {

                        when (pos) {
                            0 -> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_8)
                            }
                            arrContent.size - 1 -> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_7)

                            }
                            else -> {
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_6)
                            }
                        }
                    }


                    Constants.KEY_SIZE_MEDIUM -> {
                        when (pos) {
                            0 -> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_10)
                            }
                            arrContent.size - 1 -> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_9)

                            }
                            else -> {
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_8)
                            }
                        }

                    }
                    Constants.KEY_SIZE_BIG -> {
                        when (pos) {
                            0 -> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_12)
                            }
                            arrContent.size - 1 -> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_11)

                            }
                            else -> {
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_10)
                            }
                        }

                    }
                    Constants.KEY_SIZE_VERY_BIG -> {
                        when (pos) {
                            0 -> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_14)
                            }
                            arrContent.size - 1-> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_13)

                            }
                            else -> {
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_12)
                            }
                        }

                    }
                    else -> { //default is KEY_SIZE_MEDIUM
                        when (pos) {
                            0 -> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_10)
                            }
                            arrContent.size - 1 -> {
                                itemView.txt_detail.typeface = Typeface.DEFAULT_BOLD
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_9)

                            }
                            else -> {
                                itemView.txt_detail.textSize =
                                    activity.resources.getDimension(R.dimen.text_size_8)
                            }
                        }
                    }

                }


            }


        }
    }

    inner class AdHolder(itemView: View) : BaseViewHoderPosition<ModelContent>(itemView) {
        override fun bind(item: ModelContent, pos: Int) {
            val adRequest = AdRequest.Builder().build()
            itemView.ad_view_item_ads.loadAd(adRequest)
            if (item?.text.equals("") && !item?.img.equals("")) {
                Glide.with(itemView.context).load(item?.img).into((itemView.img_detail_ads))
                itemView.img_detail_ads.visibility = View.VISIBLE
            }
            if (item.img.equals("")) {
                itemView.txt_detail_ads.text = item?.text
                itemView.txt_detail_ads.visibility = View.VISIBLE
                //get current size
                when ((activity as BaseActivity).getSharedPrefsString(Constants.KEY_FONT_SIZE)) {
                    Constants.KEY_SIZE_SMALL -> {
                        itemView.txt_detail_ads.textSize =
                            activity.resources.getDimension(R.dimen.text_size_6)

                    }
                    Constants.KEY_SIZE_MEDIUM -> {
                        itemView.txt_detail_ads.textSize =
                            activity.resources.getDimension(R.dimen.text_size_8)

                    }
                    Constants.KEY_SIZE_BIG -> {
                        itemView.txt_detail_ads.textSize =
                            activity.resources.getDimension(R.dimen.text_size_10)

                    }
                    Constants.KEY_SIZE_VERY_BIG -> {
                        itemView.txt_detail_ads.textSize =
                            activity.resources.getDimension(R.dimen.text_size_12)

                    }
                    else -> { //default is KEY_SIZE_MEDIUM
                        itemView.txt_detail_ads.textSize =
                            activity.resources.getDimension(R.dimen.text_size_8)
                    }

                }
            }

        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return arrContent.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    public fun addData(list: ArrayList<ModelContent>) {
        arrContent.addAll(list)
        notifyDataSetChanged()

    }

    override fun getItemViewType(position: Int): Int {
        // if (position == 2) return VIEW_ADS
        if (position == 6) return VIEW_ADS
        if (arrContent.size > 25) {
            if (position == arrContent.size / 2) return VIEW_ADS
        }
        return VIEW_ITEM
    }


    override fun onBindViewHolder(holder: BaseViewHoderPosition<*>, position: Int) {

        if (holder is AdHolder) {
            holder.bind(arrContent[position], position)

        } else if (holder is MyViewHolder) {
            holder.bind(arrContent[position], position)

        }
    }

}
