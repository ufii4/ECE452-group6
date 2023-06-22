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


public class SpoonacularDataSource {
    public final RecipeService recipeService;
    public final IngredientService ingredientService;

    public SpoonacularDataSource() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(this::addApiKeyToRequests)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .client(httpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeService = retrofit.create(RecipeService.class);
        ingredientService = retrofit.create(IngredientService.class);
    }

    private Response addApiKeyToRequests(Interceptor.Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        HttpUrl originalHttpUrl = chain.request().url();
        HttpUrl newUrl = originalHttpUrl.newBuilder()
                .addQueryParameter("apiKey", "3284e6ceb69b4f919a229fbafcef9624").build();
        request.url(newUrl);
        return chain.proceed(request.build());
    }
}
