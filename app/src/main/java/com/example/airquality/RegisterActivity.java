package com.example.airquality;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {

    private EditText userName;
    private EditText email;
    private EditText password;
    private EditText Re_Password;
    private Button bt_sign_up;
    private Button bt_back;
    private WebView webView;
    private MyWebViewClient myWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        Re_Password = findViewById(R.id.RePassword);
        bt_sign_up = findViewById(R.id.button_SignUp);
        bt_back = findViewById(R.id.button_Back);
        webView = findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        bt_sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     showWebView();
                }
            });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
        private void showWebView() {
            boolean check = false;
            if (userName.getText().toString().isEmpty()){
                showToast(RegisterActivity.this,"Please specific username!!!");
                return;
            }
            if(email.getText().toString().isEmpty() ){
                showToast(RegisterActivity.this,"Please specific email!!!");
                return;
            }
            if(password.getText().toString().isEmpty() ){
                showToast(RegisterActivity.this,"Please fill in password!!!");
                return;
            }
            if(!(password.getText().toString().equals(Re_Password.getText().toString()))){
                showToast(RegisterActivity.this,"Password confirm not match!!!");
                return;
            }

                String url = "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/registrations?client_id=openremote&redirect_uri=https://uiot.ixxc.dev/callback&response_type=code";
                String redirectUri = "https://uiot.ixxc.dev/callback";

                myWebViewClient = new MyWebViewClient(redirectUri,userName.getText().toString(),email.getText().toString(),password.getText().toString(),Re_Password.getText().toString());
                webView.setWebViewClient(myWebViewClient);
                webView.loadUrl(url);
                // webView.setVisibility(View.VISIBLE);
        }
        private void showToast(Activity activity, String message) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
}