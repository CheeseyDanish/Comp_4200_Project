package com.example.comp_4200_project;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WordPressAPI {
    @FormUrlEncoded
    @POST("/wp-json/jwt-auth/v1/token")
    Call<JsonObject> authenticateUser(
            @Field("username") String username,
            @Field("password") String password
    );
}