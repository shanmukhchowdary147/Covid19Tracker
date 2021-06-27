package com.example.covid19tracker;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static String A_URL="https://corona.lmao.ninja/";

    public static Retrofit getRetrofitInstance(){
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(A_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
