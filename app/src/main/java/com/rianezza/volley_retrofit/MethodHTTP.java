package com.rianezza.volley_retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MethodHTTP {
    @GET("User_Registration")
    Call<user_response> getUser();

    @POST("User_Registration")
    Call<request> sendUser(@Body user user);
}
