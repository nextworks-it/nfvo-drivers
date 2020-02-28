package it.nextworks.openapi;

import java.io.IOException;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public final class FixContentTypeInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request fixedRequest = originalRequest.newBuilder().
                header("Content-Type", "application/json").build();

        return chain.proceed(fixedRequest);
    }
}