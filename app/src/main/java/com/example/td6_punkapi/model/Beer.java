package com.example.td6_punkapi.model;

import android.graphics.Color;

import com.example.td6_punkapi.R;

import java.util.HashMap;

public class Beer {

    private Integer id;
    private String name;
    private String tagline;
    private String urlImage;
    private Integer ebc;

    public Beer(Integer id, String name, String tagline, String urlImage, Double ebc){
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.urlImage = urlImage;


        //Preparer un bon image

        this.urlImage = (urlImage == null ? "drawable://"+ R.drawable.photo_available : urlImage);

        //Preparer un bon ebc
        if(ebc == null) this.ebc = 0;
        else {
            Integer[] list_ebc = {0,4,6,8,12,16,20,26,33,39,47,57,69,79};
            for(Integer e : list_ebc) if(ebc > e) this.ebc = e;
        }
    }

    public Integer getId() {
        return id;
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

    public Integer getEbc() {
        return ebc;
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
