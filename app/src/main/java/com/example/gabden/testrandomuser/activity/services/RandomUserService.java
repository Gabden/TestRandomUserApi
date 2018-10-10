package com.example.gabden.testrandomuser.activity.services;

import com.example.gabden.testrandomuser.activity.models.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RandomUserService {

    @GET("api/")
    Call<Example> getRandomUsers(@Query("results") String results);
}
