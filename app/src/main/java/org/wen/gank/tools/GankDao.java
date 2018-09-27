package org.wen.gank.tools;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.wen.gank.model.GankModel;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface GankDao {

    @Query("select * from gank")
    Flowable<List<GankModel>> getGanks();

    @Query("select * from gank where type = :type limit 50")
    Flowable<List<GankModel>> getGanksLimit(String type);

    @Query("select * from gank where type = :type")
    Flowable<List<GankModel>> getGanksByType(String type);

    @Insert
    void insert(GankModel model);

    @Insert
    void batchinsert(List<GankModel> models);

}
