package com.example.airquality;

import androidx.lifecycle.ViewModelProvider;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.airquality.API.APIInterface;
import com.example.airquality.API.ApIClient;
import com.example.airquality.API.ResetPasswordBody;
import com.example.airquality.Model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    String token;
    private APIInterface apiInterface;
    String ID = null;
    Boolean getapi =false;
    EditText EditTusername, EditTemail, EditTfirstname, EditTlastname;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String Username = bundle.getString("user");
        token = bundle.getString("token");
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        EditTusername = view.findViewById(R.id.username);
        EditTemail = view.findViewById(R.id.email);
        EditTfirstname = view.findViewById(R.id.firstname);
        EditTlastname = view.findViewById(R.id.lastname);

        apiInterface = ApIClient.setToken(token);
        apiInterface = ApIClient.getClient().create(APIInterface.class);
        Call<UserResponse> call1 = apiInterface.getUser();
        call1.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                EditTusername.append(response.body().getUsername());
                EditTemail.append(response.body().getEmail());
                EditTfirstname.append(response.body().getFirstName());
                EditTlastname.append(response.body().getLastName());
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error!!!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


}