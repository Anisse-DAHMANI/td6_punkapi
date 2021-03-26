package com.example.td6_punkapi.model;

import android.graphics.Color;

import java.util.HashMap;

public class Beer {

    private String name;
    private String tagline;
    private String urlImage;
    private Integer srm;

    public Beer(String name, String tagline, String urlImage, Integer srm){
        this.name = name;
        this.tagline = tagline;
        this.urlImage = urlImage;
        this.srm = srm;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public Integer getSrm() {
        return srm;
    }

    @Override
    public String toString() {
        return "Beer{" +
                "name='" + name + '\'' +
                ", tagline='" + tagline + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
