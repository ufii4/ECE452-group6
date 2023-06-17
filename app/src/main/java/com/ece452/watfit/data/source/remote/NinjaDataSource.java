package com.ece452.watfit.data.source.remote;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NinjaDataSource {
    public final ExerciseService exerciseService;

    public NinjaDataSource() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(this::addApiKeyToRequests)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.api-ninjas.com/")
                .client(httpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        exerciseService = retrofit.create(ExerciseService.class);
    }

    private Response addApiKeyToRequests(Interceptor.Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder().addHeader("X-Api-Key", "nJRWQhFSXDcc1naAFGOW2g==i23iW9M0REO1zF2U");
        return chain.proceed(request.build());
    }
}
