package com.example.airquality.Model;

public class User {
    private String client_id;
    private String username;
    private String password;
    private String grant_type;

    public User( String username, String password) {
        this.client_id = "openremote";
        this.username = username;
        this.password = password;
        this.grant_type = "password";
    }

    public String getClient_id() {
        return client_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrant_type() {
        return grant_type;
    }


}
