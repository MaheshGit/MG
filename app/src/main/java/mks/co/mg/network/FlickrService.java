package mks.co.mg.network;

import mks.co.mg.network.model.FlickrResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Mahesh on 13/8/16.
 */
public interface FlickrService {
    @GET("photos_public.gne")
    Call<FlickrResponse> getFLickrImages(@Query("format") String json, @Query("nojsoncallback") int nojsoncallback);

    // complete url : https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1
}
