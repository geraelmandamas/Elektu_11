package com.epifi.elektu.Utils;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Puntos {
    private int puntos;
    private String userId;
    public Puntos() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Puntos(int puntos,String userId) {
        this.puntos = puntos;
        this.userId = userId;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }


}