package com.example;

public class UserCredentials {

    private  String email;
    private  String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getLogin() {
        return email;
    }

    public void setLogin(String login)
    {
        this.email = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
