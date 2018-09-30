package org.wen.gank.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "gank")
class GankModel(@PrimaryKey var _id: String, var url: String?,
                var type: String?, var desc: String?, var who: String?,
                var images: List<String>?, var used: Boolean, var createdAt: String?,
                var updatedAt: String?, var publishedAt: String?)