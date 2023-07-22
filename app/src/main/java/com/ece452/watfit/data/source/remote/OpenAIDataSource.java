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


public class OpenAIDataSource {
    public final SuggestionService suggestionService;


    public OpenAIDataSource() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(this::addApiKeyToRequests)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .client(httpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

            suggestionService = retrofit.create(SuggestionService.class);
    }

    private Response addApiKeyToRequests(Interceptor.Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer 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");
        return chain.proceed(request.build());
    }
}
