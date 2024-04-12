package com.example.airquality.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {
        @SerializedName("access_token")
        @Expose
        private String access_token;
        public String getAccess_token() {
            return access_token;
        }
}
