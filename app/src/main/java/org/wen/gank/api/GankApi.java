package org.wen.gank.api;

import io.reactivex.Observable;
import java.util.List;
import org.wen.gank.model.Gank;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * created by Jiahui.wen 2017-07-26
 */
public interface GankApi {

    @GET("/api/data/{category}/{count}/{start}")
    Observable<HttpResult<List<Gank>>> getCategoryDatas(@Path("category") String category,
        @Path("start") int start, @Path("count") int count);
}
