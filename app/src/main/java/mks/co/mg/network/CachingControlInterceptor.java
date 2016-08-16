package mks.co.mg.network;

import java.io.IOException;

import mks.co.mg.GameApplication;
import mks.co.mg.utils.CommonUtilities;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mahesh on 13/8/16.
 */
public class CachingControlInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Add Cache Control only for GET methods
        if (request.method().equals("GET")) {
            if (CommonUtilities.isConnectingToInternet(GameApplication.context)) {
                // 1 day
                request = request.newBuilder()
                        .header("Cache-Control", "only-if-cached")
                        .build();
            } else {
                // 4 weeks stale
                request = request.newBuilder()
                        .header("Cache-Control", "public, max-stale=2419200")
                        .build();
            }
        }

        Response originalResponse = chain.proceed(request);
        return originalResponse;
    }
}
