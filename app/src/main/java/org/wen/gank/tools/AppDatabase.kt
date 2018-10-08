package org.wen.gank.tools

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import org.wen.gank.model.GankModel

@Database(entities = [GankModel::class], version = AppDatabase.version)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

  abstract fun gankDao(): GankDao

  companion object {
    const val version = 1
  }
}
