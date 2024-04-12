package com.example.airquality;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.airquality.API.APIInterface;
import com.example.airquality.API.ApIClient;
import com.example.airquality.API.ApiService;
import com.example.airquality.API.ResetPasswordBody;
import com.example.airquality.Model.Post;
import com.example.airquality.Model.User;
import com.example.airquality.Model.UserResponse;
import com.google.android.gms.common.stats.WakeLockEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends Fragment {
    WebView webView;
    RelativeLayout loading;

    String token;
    private APIInterface apiInterface;
    String ID = null;
    Boolean getapi =false;
    EditText EditTusername, EditTpassword, EdiTresetpassword, EditTresetpasswordconfirm;

    User user;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String Username = bundle.getString("user");
        token = bundle.getString("token");

        View view = inflater.inflate(R.layout.fragment_reset_password,container,false);

        EditTusername = view.findViewById(R.id.rs_username);
        EditTusername.setText(Username);
        EditTpassword = view.findViewById(R.id.rs_oldPwd);
        EdiTresetpassword = view.findViewById(R.id.rs_newPwd);
        EditTresetpasswordconfirm = view.findViewById(R.id.rs_rePwd);
        webView = view.findViewById(R.id.webView);
        loading = view.findViewById(R.id.loading);

        webView.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);


        apiInterface = ApIClient.setToken(token);
        apiInterface = ApIClient.getClient().create(APIInterface.class);

        Button reset = view.findViewById(R.id.resetbtn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<UserResponse> call1 = apiInterface.getUser();
                call1.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if(response.isSuccessful()){
                            Log.d("API CALL", response.toString());
                            if(EdiTresetpassword.getText().toString().equals(EditTresetpasswordconfirm.getText().toString())){
                                ResetPasswordBody body = new ResetPasswordBody("String",EdiTresetpassword.getText().toString(), true);
                                Call<Void> call2 = apiInterface.resetPassword(response.body().getId(), body);
                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Log.d("API CALL update", response.toString());
                                        if (response.isSuccessful()) {
                                            loading.setVisibility((View.VISIBLE));
                                            webView.setVisibility(View.VISIBLE);
                                            WebSettings webSettings = webView.getSettings();
                                            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                                            CookieManager.getInstance().removeAllCookies(null);
                                            CookieManager.getInstance().flush();
                                            webSettings.setJavaScriptEnabled(true);
                                            webView.loadUrl("https://uiot.ixxc.dev");
                                            webView.setWebViewClient(new WebViewClient() {
                                                boolean canIntent = false;
                                                @Override
                                                public void onPageFinished(WebView view, String url) {
                                                    if (url.contains("openid-connect/auth")) {
                                                        canIntent = true;
                                                        String javascript =
                                                                "document.forms[0].elements['username'].value = '" + user.getUsername() + "';" +
                                                                        "document.forms[0].elements['password'].value = '" + user.getPassword() + "';" +
                                                                        "document.forms[0].submit();";
                                                        view.evaluateJavascript(javascript, null);
                                                    }
                                                    if (url.contains("login-actions/required-action")) {
                                                        String javascript =
                                                                "document.forms[0].elements['password-new'].value = '" + user.getPassword() + "';" +
                                                                        "document.forms[0].elements['password-confirm'].value = '" + user.getPassword() + "';" +
                                                                        "document.forms[0].submit();";
                                                        view.evaluateJavascript(javascript, null);
                                                    }
                                                    if (url.contains("manager") && canIntent == true) {
                                                        loading.setVisibility((View.GONE));
                                                        webView.setVisibility(View.GONE);
                                                        Toast.makeText(getActivity(), "Success changed password", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getActivity(), "network error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getActivity(),"Confirm password not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getActivity(),"Wrong username or password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Network error!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }


}