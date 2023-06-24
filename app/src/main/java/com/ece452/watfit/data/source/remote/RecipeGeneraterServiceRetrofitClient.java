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

public class RecipeGeneraterServiceRetrofitClient {
    public final RecipeGeneraterService recipeGeneraterService;

    public RecipeGeneraterServiceRetrofitClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(this::addApiKeyToRequests)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .client(httpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeGeneraterService = retrofit.create(RecipeGeneraterService.class);
    }

    public static synchronized RecipeGeneraterServiceRetrofitClient getInstance() {
        return new RecipeGeneraterServiceRetrofitClient();
    }

    public RecipeGeneraterService getRecipeGeneraterService() {
        return recipeGeneraterService;
    }
    private Response addApiKeyToRequests(Interceptor.Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        HttpUrl originalHttpUrl = chain.request().url();
        HttpUrl newUrl = originalHttpUrl.newBuilder()
                .addQueryParameter("apiKey", "eeadd8f318854251b83b19945deeed4e").build();
        request.url(newUrl);
        return chain.proceed(request.build());
    }
}
