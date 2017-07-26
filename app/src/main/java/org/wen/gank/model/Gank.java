package org.wen.gank.model;

/**
 * created by Jiahui.wen 2017-07-27
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.RowMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@AutoValue
public abstract class Gank implements GankModel {

    @Json(name = "desc")
    @Nullable
    @Override
    public abstract String description();

    public abstract Gank withDescription(String description);

    private final static JsonAdapter<List<String>> adapter =
        new Moshi.Builder().build().adapter(Types.newParameterizedType(List.class, String.class));

    public static final Factory<Gank> FACTORY = new Gank.Factory<>(new Creator<Gank>() {
        @Override
        public Gank create(@NonNull String _id, @NonNull String url, @Nullable String type,
            @Nullable String description, @Nullable String who, @Nullable List<String> images,
            @Nullable Boolean used, @Nullable String createdAt, @Nullable String updatedAt,
            @Nullable String publishedAt) {
            return new AutoValue_Gank(_id, url, type, who, images, used, createdAt, updatedAt,
                publishedAt, description);
        }
    }, new ColumnAdapter<List<String>, String>() {
        @NonNull
        @Override
        public List<String> decode(String databaseValue) {
            if (databaseValue == null) return Collections.emptyList();
            try {
                return adapter.fromJson(databaseValue);
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }

        @Override
        public String encode(@NonNull List<String> value) {
            return adapter.toJson(value);
        }
    });

    public static final RowMapper<Gank> MAPPER = FACTORY.selectAllMapper();

    public static JsonAdapter<Gank> jsonAdapter(Moshi moshi) {
        return new AutoValue_Gank.MoshiJsonAdapter(moshi);
    }

    @AutoValue.Builder
    public static abstract class Builder {
        abstract Builder _id(String id);

        abstract Builder url(String url);

        abstract Builder type(String type);

        abstract Builder description(String desc);

        abstract Builder who(String who);

        @Nullable
        abstract Builder images(List<String> images);

        @Nullable
        abstract Builder used(Boolean used);

        @Nullable
        abstract Builder createdAt(String createdAt);

        @Nullable
        abstract Builder updatedAt(String updatedAt);

        @Nullable
        abstract Builder publishedAt(String publishedAt);

        abstract Gank build();
    }
}
