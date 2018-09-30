package org.wen.gank.api

import io.reactivex.Observable
import org.wen.gank.model.GankModel
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * created by Jiahui.wen 2017-07-26
 */
interface GankApi {

  @GET("/api/data/{category}/{count}/{start}")
  fun getCategoryDatas(@Path("category") category: String,
                       @Path("start") start: Int,
                       @Path("count") count: Int): Observable<HttpResult<List<GankModel>>>
}
