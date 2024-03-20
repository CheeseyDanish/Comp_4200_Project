package com.example.comp_4200_project;

import com.google.gson.JsonObject;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WordPressAPI {
    @Multipart
    @POST("/wp-json/api/v1/token")
    Call<JsonObject> authenticateUser(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );
}