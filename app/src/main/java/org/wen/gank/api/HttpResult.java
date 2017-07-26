package org.wen.gank.api;

import com.squareup.moshi.Json;

/**
 * created by Jiahui.wen 2017-07-26
 */
public class HttpResult<T> {

    @Json(name = "error") public final boolean error;

    public final T results;

    public HttpResult(boolean error, T results) {
        this.error = error;
        this.results = results;
    }
}
