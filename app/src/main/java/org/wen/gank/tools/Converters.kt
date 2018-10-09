package org.wen.gank.tools

import androidx.annotation.Keep
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException

@Keep
class Converters {

  private var sJsonAdapter = Moshi.Builder().build()
      .adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))

  @TypeConverter
  fun listFromString(value: String?): List<String>? {
    try {
      return if (value == null) null else sJsonAdapter.fromJson(value)
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return null
  }

  @TypeConverter
  fun listToString(images: List<String>?): String? {
    return if (images == null) null else sJsonAdapter.toJson(images)
  }

}
