package org.wen.gank.api

import com.squareup.moshi.Json

/**
 * created by Jiahui.wen 2017-07-26
 */
data class HttpResult<T>(@Json(name = "error") val error: Boolean, @Json(name = "results") val results: T)
