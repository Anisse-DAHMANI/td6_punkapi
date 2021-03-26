package com.example.td6_punkapi.model;

import java.util.HashMap;

public class Beer {

    private String name;
    private String tagline;
    private String urlImage;


    public Beer(String name, String tagline, String urlImage){
        this.name = name;
        this.tagline = tagline;
        this.urlImage = urlImage;
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
}
