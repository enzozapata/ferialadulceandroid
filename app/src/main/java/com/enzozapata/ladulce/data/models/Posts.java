package com.enzozapata.ladulce.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by enzoz on 09/01/18.
 */

public class Posts {
    public String id;
    public String titulo;
    public String fecha_pub;
    public String icon;
    public String precio;
    public String icon_full;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha_pub() {
        return fecha_pub;
    }

    public void setFecha_pub(String fecha_pub) {
        this.fecha_pub = fecha_pub;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getIcon_full() {
        return icon_full;
    }

    public void setIcon_full(String icon_full) {
        this.icon_full = icon_full;
    }
}