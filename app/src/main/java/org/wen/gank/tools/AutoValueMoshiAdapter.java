package org.wen.gank.tools;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;

/**
 * created by Jiahui.wen 2017-07-26
 */
@MoshiAdapterFactory
public abstract class AutoValueMoshiAdapter implements JsonAdapter.Factory {

    public static JsonAdapter.Factory create() {
        return new AutoValueMoshi_AutoValueMoshiAdapter();
    }
}
