package com.example.td6_punkapi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.td6_punkapi.DetailBeerActivity;
import com.example.td6_punkapi.MainActivity;
import com.example.td6_punkapi.R;
import com.example.td6_punkapi.model.Beer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class BeerAdapter extends ArrayAdapter<Beer> {
    private Activity myActivity;
    private HashMap<Integer, String> ebcCode;
    private String[] codes_color = {"#FFFFFF","#F8F753","#F6F513", "#ECE61A", "#D5BC26", "#BF923B", "#BF813A", "#BC6733", "#8D4C32", "#5D341A", "#261716", "#0F0B0A", "#080707", "#030403","#030403"};
    private Integer[] list_ebc = {0,4,6,8,12,16,20,26,33,39,47,57,69,79};

    public BeerAdapter(@NonNull Activity myActivity, ArrayList<Beer> beers) {
        super(myActivity, 0, beers);
        this.myActivity = myActivity;
        intialisationHashMapSrm();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //INITIALISATION
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_beer, parent, false);
        }
        TextView textViewNameBeer = currentItemView.findViewById(R.id.nameBeer2);
        TextView textViewTaglineBeer = currentItemView.findViewById(R.id.taglineBeer);
        ImageView imageBeer = currentItemView.findViewById(R.id.imageBeer);
        LinearLayout linearLayoutH = currentItemView.findViewById(R.id.linearLayout3);
        Beer beer = getItem(position);


        //TRAITEMENT
        Picasso.get().load(beer.getUrlImage()).into(imageBeer);
        Integer ebc =  beer.getEbc();
        Integer color = Color.parseColor(ebcCode.get(ebc));
        linearLayoutH.setBackgroundColor(color);

        textViewNameBeer.setText(beer.getName());
        textViewTaglineBeer.setText(beer.getTagline());


        int colortext = Color.BLACK;
        if(ebc > 33) colortext =Color.WHITE;

        textViewNameBeer.setTextColor(colortext);
        textViewTaglineBeer.setTextColor(colortext);

        currentItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, DetailBeerActivity.class);
                Integer id = beer.getId();
                intent.putExtra("id",id);
                intent.putExtra("colorEbc", color);
                myActivity.startActivity(intent);
                myActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return currentItemView;
    }

    private void intialisationHashMapSrm(){
        ebcCode = new HashMap<>();
        for(int i = 0; i < codes_color.length-1; i++){
            ebcCode.put(list_ebc[i],codes_color[i]);
        }
    }
}
