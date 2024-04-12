package com.example.airquality;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.airquality.API.APIInterface;
import com.example.airquality.API.ApIClient;
import com.example.airquality.Model.Asset;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {



    String ID = null;
    Boolean getapi =false;
    APIInterface apiInterface;
    TextView user;
    TextView location;
    TextView Temp;
    TextView Humidity;
    TextView rainfall;
    TextView wind;
    String token;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        long timenow = System.currentTimeMillis();
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timenow), ZoneId.systemDefault());
        user = view.findViewById(R.id.as);
        user.setText(dateTime.getDayOfWeek().toString());

        location = view.findViewById(R.id.Location);
        Humidity = view.findViewById(R.id.tv_Hum);
        Temp = view.findViewById(R.id.tv_Temp);
        rainfall = view.findViewById(R.id.tv_Rain);
        wind = view.findViewById(R.id.tv_Wind);

        Bundle bundle = getArguments();
        String User = bundle.getString("user");
        String assetid = bundle.getString("assetid");
        token = bundle.getString("token");
      //  user.setText("Hi, " + User);


        apiInterface = ApIClient.setToken(token);
        apiInterface = ApIClient.getClient().create(APIInterface.class);
        callSecondAPI(assetid);
        return view;
    }

    private void callSecondAPI(String id) {
        Call<Asset> callinfo = apiInterface.getAsset(id);
        callinfo.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> callinfo, Response<Asset> responseinfo) {
                if (responseinfo.isSuccessful())
                {
                    Log.d("API CALL", responseinfo.code() + "");
                    Log.d("API CALL", responseinfo.toString());
                    Asset asset = responseinfo.body();
                    if (asset != null) {
                        getapi = true;

                        Humidity.setText(asset.attributes.hum.value+"%");
                        Temp.setText(asset.attributes.temp.value+ "\u00B0C");
                        rainfall.setText(asset.attributes.Rain.value+ "mm");
                        location.setText(asset.attributes.place.value);
                        wind.setText(asset.attributes.winsp.value+" km/h");

                    }
                }}

            @Override
            public void onFailure(Call<Asset> callinfo, Throwable t) {
            }
        });
    }



}