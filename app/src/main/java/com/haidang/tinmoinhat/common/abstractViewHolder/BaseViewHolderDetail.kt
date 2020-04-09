package com.haidang.tinmoinhat.common.abstractViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolderDetail<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T,pos:Int)
}