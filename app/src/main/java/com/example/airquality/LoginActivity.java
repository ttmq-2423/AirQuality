package com.example.airquality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airquality.API.ApiService;
import com.example.airquality.Model.Post;
import com.example.airquality.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUserName;
    private EditText editTextPassWord;
    private Button buttonLogIn;
    private Button buttonBack;
    private static String token= null;
    private TextView textViewForgotpass;

    //Url: https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/token
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd- HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://uiot.ixxc.dev/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUserName = (EditText) findViewById(R.id.username);
        editTextPassWord =(EditText)  findViewById(R.id.password);
        buttonLogIn =(Button) findViewById(R.id.loginbtn);
        buttonBack = (Button) findViewById(R.id.button_Back);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity    .this, MainActivity.class);
                startActivity(intent);
            }
        });
        //thêm để commit
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editTextUserName.getText().toString()) ||TextUtils.isEmpty(editTextPassWord.getText().toString())){
                    Toast.makeText(LoginActivity.this,"Username/Password required",Toast.LENGTH_SHORT).show();
                }else{
                    LoginCallAPI();
                    /*if(token!=null){
                        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                        Bundle mybundle = new Bundle();
                        mybundle.putString("token",token);
                        intent.putExtra("mypackage",mybundle);
                        startActivity(intent);
                    }*/

                }
            }
        });
    }
    private void LoginCallAPI() {
        user = new User(editTextUserName.getText().toString(),editTextPassWord.getText().toString());
        Call<Post> call = apiService.login(user.getClient_id(),user.getUsername(),user.getPassword(),user.getGrant_type());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Log in Successful!!!",Toast.LENGTH_SHORT).show();
                    token = response.body().getAccess_token();
                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                    Bundle mybundle = new Bundle();
                    mybundle.putString("token",token);
                    mybundle.putString("user",editTextUserName.getText().toString());
                    intent.putExtra("mypackage",mybundle);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Log in Error, please check your username or password!!!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Throwable "+ getLocalClassName(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}