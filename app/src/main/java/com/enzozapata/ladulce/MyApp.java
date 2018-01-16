package com.enzozapata.ladulce;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;

public class MyApp extends Application {


    private String id_fb="1583201355107984";
    private String foto_perfil;
    private String puesto="Puesto 1";
    private String rango="Feriante";
    private String nombre_apellido="Enzo Zapata";
    private String wpup;
    public String getId_fb() {
        return id_fb;
    }

    public void setId_fb(String id_fb) {
        this.id_fb = id_fb;
    }

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getNombre_apellido() {
        return nombre_apellido;
    }

    public void setNombre_apellido(String nombre_apellido) {
        this.nombre_apellido = nombre_apellido;
    }

    public String getWpup() {
        return wpup;
    }

    public void setWpup(String wpup) {
        this.wpup = wpup;
    }
}