package org.wen.gank.tools;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import org.wen.gank.model.GankModel;

@Database(entities = {GankModel.class}, version = AppDatabase.version)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final int version = 1;

    public abstract GankDao gankDao();
}
