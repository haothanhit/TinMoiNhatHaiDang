package com.haidang.tinmoinhat.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.haidang.tinmoinhat.common.global.Constants
import com.haidang.tinmoinhat.common.global.Constants.Companion.PAGE_NUMBER
import com.haidang.tinmoinhat.common.model.ModelTopic
import com.haidang.tinmoinhat.ui.fragment.ArticleFragment


class ViewPagerAdapter internal constructor(fm: FragmentManager,private val arrList: ArrayList<ModelTopic>) : FragmentStatePagerAdapter (fm) {


    override fun getItem(position: Int): Fragment {

        return ArticleFragment.newInstance(arrList[position].id!!)
 
    }
 
    override fun getCount(): Int {
        return PAGE_NUMBER
    }
 
    override fun getPageTitle(position: Int): CharSequence? {
        return arrList[position].name!!
    }

}
