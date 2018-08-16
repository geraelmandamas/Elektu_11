package com.epifi.elektu.Utils;

public class MatchingClass {

    private String Username;
    // private String image;
    private String  puntos;


    public MatchingClass(String username, String puntos) {
        Username = username;
        this.puntos = puntos;
    }

    public MatchingClass() {
    }


    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }
}