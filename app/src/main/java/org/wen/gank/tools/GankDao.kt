package org.wen.gank.tools

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import org.wen.gank.model.GankModel

@Dao
interface GankDao {

  @get:Query("select * from gank")
  val ganks: Flowable<List<GankModel>>

  @Query("select * from gank where type = :type limit 50")
  fun getGanksLimit(type: String): Flowable<List<GankModel>>

  @Query("select * from gank where type = :type")
  fun getGanksByType(type: String): Flowable<List<GankModel>>

  @Insert
  fun insert(model: GankModel)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun batchInsert(models: List<GankModel>)

}
