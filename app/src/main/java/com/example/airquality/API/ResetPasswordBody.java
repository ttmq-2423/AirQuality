package com.example.airquality.API;

public class ResetPasswordBody {
    private String type;
    private String value;
    private boolean temporary;

    public ResetPasswordBody(String type, String value, boolean temporary) {
        this.type = type;
        this.value = value;
        this.temporary = temporary;
    }
}
