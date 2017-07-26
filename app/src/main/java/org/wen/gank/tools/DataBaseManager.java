package org.wen.gank.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.wen.gank.model.GankModel;

/**
 * created by Jiahui.wen 2017-07-27
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final int version = 1;
    private static final String db_name = "gank.db";

    public DataBaseManager(Context context) {
        super(context, db_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GankModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
