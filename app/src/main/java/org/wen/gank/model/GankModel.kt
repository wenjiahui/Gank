package org.wen.gank.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gank")
class GankModel(@PrimaryKey var _id: String, var url: String?,
                var type: String?, var desc: String?, var who: String?,
                var images: List<String>?, var used: Boolean, var createdAt: String?,
                var updatedAt: String?, var publishedAt: String?)