package org.wen.gank.tools

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

import org.wen.gank.model.GankModel

@Database(entities = [GankModel::class], version = AppDatabase.version)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

  abstract fun gankDao(): GankDao

  companion object {
    const val version = 1
  }
}
