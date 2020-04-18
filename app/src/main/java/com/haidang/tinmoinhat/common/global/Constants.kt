package com.haidang.tinmoinhat.common.global

import com.haidang.tinmoinhat.common.model.ModelTopic

class Constants {
  companion object {
    //bottom navigation
    const val TAB_HOME = "HomeFragment"
    const val TAB_SETTING = "SettingFragment"
    const val  PAGE_NUMBER = 13
    const val URL_CATE_CHINH = "http://66.42.59.92:8090/api/"
    const val URL_CATE_CHINH1 = "get_arc_by_catid?"

    const val URL_DATA_CHINH = "http://66.42.59.92:8090/api/"
    const val URL_DATA_CHINH1 = "get_noi_dung_tin_new?"



// list  key SharedPreferences
    const val KEY_RELATE = "relate"
    const val KEY_RECENT_READING = "recentReading"
      const val KEY_SAVE_ARTICLE = "saveArticle"
      const val KEY_THEME = "theme"
    const val KEY_FONT_SIZE = "fontSize"
    const val KEY_SIZE_SMALL = "sizeSmall"
    const val KEY_SIZE_MEDIUM = "sizeMedium"
    const val KEY_SIZE_BIG = "sizeBig"
    const val KEY_SIZE_VERY_BIG = "sizeVeryBig"
      const val KEY_COUNT_ADS_FULL = "countADSFull"


      fun getALLTopic():ArrayList<ModelTopic>{
          var mCategories =ArrayList<ModelTopic>( )
          mCategories.add(ModelTopic("0","Tin Hot"))
          mCategories.add(ModelTopic("1","Thế Giới"))
          mCategories.add(ModelTopic("2","Xã Hội"))
          mCategories.add(ModelTopic("3","Văn Hóa"))
          mCategories.add(ModelTopic("4","Kinh Tế"))
          mCategories.add(ModelTopic("5","Công Nghệ"))
          mCategories.add(ModelTopic("6","Thể Thao"))
          mCategories.add(ModelTopic("7","Giải Trí"))
          mCategories.add(ModelTopic("8","Pháp Luật"))
          mCategories.add(ModelTopic("9","Giáo Dục"))
          mCategories.add(ModelTopic("10","Sức Khỏe"))
          mCategories.add(ModelTopic("11","Nhà Đất"))
          mCategories.add(ModelTopic("12","Xe Cộ"))
          return  mCategories

      }


  }
}