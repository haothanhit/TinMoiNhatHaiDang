package com.haidang.tinmoinhat.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.haidang.tinmoinhat.common.global.Constants.Companion.PAGE_NUMBER
import com.haidang.tinmoinhat.common.global.Constants.Companion.URL_CATE_CHINH
import com.haidang.tinmoinhat.ui.fragment.ArticleFragment


class ViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter (fm) {
    private val tabTitles = arrayOf("Tin Hot", "Pháp Luật",
        "Thế Giới", "Thể Thao", "Xã Hội", "Văn Hóa", "Kinh Tế", "Công Nghệ",
        "Giải Trí", "Giáo Dục", "Sức Khỏe", "Nhà Đất", "Xe Cộ"
    )
    private val cateID =
        arrayOf("0", "8", "1", "6", "2", "3", "4", "5", "7", "9", "10", "11", "12")
    private val URL = "&page=0"
    var url: String =URL_CATE_CHINH

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ArticleFragment.newInstance(cateID[position], tabTitles[0], url + cateID[0] + URL)
            1 -> ArticleFragment.newInstance(cateID[position], tabTitles[1], url + cateID[1] + URL)
            2 -> ArticleFragment.newInstance(cateID[position], tabTitles[2], url + cateID[2] + URL)
            3 -> ArticleFragment.newInstance(cateID[position], tabTitles[3], url + cateID[3] + URL)
            4 -> ArticleFragment.newInstance(cateID[position], tabTitles[4], url + cateID[4] + URL)
            5 -> ArticleFragment.newInstance(cateID[position], tabTitles[5], url + cateID[5] + URL)
            6 -> ArticleFragment.newInstance(cateID[position], tabTitles[6], url + cateID[6] + URL)
            7 -> ArticleFragment.newInstance(cateID[position], tabTitles[7], url + cateID[7] + URL)
            8 -> ArticleFragment.newInstance(cateID[position], tabTitles[8], url + cateID[8] + URL)
            9 -> ArticleFragment.newInstance(cateID[position], tabTitles[9], url + cateID[9] + URL)
            10 -> ArticleFragment.newInstance(cateID[position], tabTitles[10], url + cateID[10] + URL)
            11 -> ArticleFragment.newInstance(cateID[position], tabTitles[11], url + cateID[11] + URL)
            12 -> ArticleFragment.newInstance(cateID[position], tabTitles[12], url + cateID[12] + URL)

            else -> null
        }!!
 
    }
 
    override fun getCount(): Int {
        return PAGE_NUMBER
    }
 
    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

}
