package com.haidang.tinmoinhat.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.haidang.tinmoinhat.common.global.Constants.Companion.PAGE_NUMBER
import com.haidang.tinmoinhat.ui.fragment.ArticleFragment


class ViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter (fm) {
    private val tabTitles = arrayOf("Tin Hot", "Pháp Luật",
        "Thế Giới", "Thể Thao", "Xã Hội", "Văn Hóa", "Kinh Tế", "Công Nghệ",
        "Giải Trí", "Giáo Dục", "Sức Khỏe", "Nhà Đất", "Xe Cộ"
    )
    private val cateID =
        arrayOf("0", "8", "1", "6", "2", "3", "4", "5", "7", "9", "10", "11", "12")

    override fun getItem(position: Int): Fragment {
        return ArticleFragment.newInstance(cateID[position])
 
    }
 
    override fun getCount(): Int {
        return PAGE_NUMBER
    }
 
    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

}
