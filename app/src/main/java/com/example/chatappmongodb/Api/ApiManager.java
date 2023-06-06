package com.example.chatappmongodb.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static final  String BASE_URL ="http://192.168.1.9:7172";
    private static ApiService apiService;
    private static ApiManager instance;

    private ApiManager(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static ApiManager getInstance() {
        if(instance == null){
            instance = new ApiManager();
        }
        return instance;
    }

    public static ApiService getApiService() {
        return apiService;
    }
}
