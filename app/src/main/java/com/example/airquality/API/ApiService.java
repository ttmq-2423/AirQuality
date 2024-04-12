package com.example.airquality.API;


import com.example.airquality.Model.Post;
import com.example.airquality.Model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("auth/realms/master/protocol/openid-connect/token")
     Call<Post> login(@Field("client_id") String client_id, @Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);


    @GET
    Call<String> getHtmlFromUrl(@Url String url);
    //@GET("")
    //Call<ResponseBody> doSomething(@Header("authentication") String token);


}
