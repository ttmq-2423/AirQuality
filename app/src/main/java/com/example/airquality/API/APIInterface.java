package com.example.airquality.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import com.example.airquality.Model.Asset;
import com.example.airquality.Model.AssetID;
import com.example.airquality.Model.Body_request_chart;
import com.example.airquality.Model.DataPointChart;
import com.example.airquality.Model.Map;
import com.example.airquality.Model.UserResponse;

import java.util.List;

public interface APIInterface {
    @GET("api/master/map/js")
    Call<Map> getMap();
    @POST("api/master/asset/query")
    Call <AssetID[]> getAssetID();
    @GET("api/master/asset/{assetID}")
    Call<Asset> getAsset(@Path("assetID") String assetID);//, @Header("Authorization") String auth);
    @PUT("api/master/user/master/reset-password/{userId}")
    Call<Void> resetPassword(
            @Path("userId") String userID,
            @Body ResetPasswordBody body
    );
    @GET("api/master/user/user")
    Call<UserResponse> getUser();
    @POST("api/master/asset/datapoint/{assetId}/attribute/{attributeName}")
    Call<DataPointChart[]> postDatapoint(@Path("assetId") String assetID,
                                @Path("attributeName") String attributeName,
                                @Body Body_request_chart bodyRequestChart);
}
