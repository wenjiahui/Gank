package org.wen.gank.tools;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Keep;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.util.List;

@Keep
public class Converters {

    static JsonAdapter<List<String>> sJsonAdapter = new Moshi.Builder().build()
            .adapter(Types.newParameterizedType(List.class, String.class));

    @TypeConverter
    public static List<String> listFromString(String value) {
        try {
            return value == null ? null : sJsonAdapter.fromJson(value);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String listToString(List<String> images) {
        return images == null ? null : sJsonAdapter.toJson(images);
    }

}
