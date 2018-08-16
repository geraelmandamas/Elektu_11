package com.epifi.elektu.Utils;

import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by srgera on 28/7/18.
 */
@IgnoreExtraProperties
public class User {
    private String username;
    public int puntos;
    private String password;
    private String email;
    private String userId;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username ,String email,String password,int puntos,String userId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.puntos = puntos;
        this.userId = userId;


    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}