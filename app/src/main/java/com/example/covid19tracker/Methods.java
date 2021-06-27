package com.example.covid19tracker;

import com.example.covid19tracker.Models.CountryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Methods
{
    @GET("v2/countries/")
    Call<List<CountryModel>> getContDAta();
    @GET("v2/all/")
    Call<List<CountryModel>> getAllDAta();

}
