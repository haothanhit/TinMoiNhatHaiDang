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
          mCategories.add(ModelTopic("0","Tin hot"))
          mCategories.add(ModelTopic("1","Thế giới"))
          mCategories.add(ModelTopic("2","Xã hội"))
          mCategories.add(ModelTopic("3","Văn hóa"))
          mCategories.add(ModelTopic("4","Kinh tế"))
          mCategories.add(ModelTopic("5","Công nghệ"))
          mCategories.add(ModelTopic("6","Thể thao"))
          mCategories.add(ModelTopic("7","Giải trí"))
          mCategories.add(ModelTopic("8","Pháp luật"))
          mCategories.add(ModelTopic("9","Giáo dục"))
          mCategories.add(ModelTopic("10","Sức khỏe"))
          mCategories.add(ModelTopic("11","Nhà đất"))
          mCategories.add(ModelTopic("12","Xe cộ"))
          return  mCategories

      }


  }
}