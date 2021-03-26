package com.example.td6_punkapi.adapter;

import android.content.Context;
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

import com.example.td6_punkapi.R;
import com.example.td6_punkapi.model.Beer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BeerAdapter extends ArrayAdapter<Beer> {

    private HashMap<Integer, String> srmCode;

    public BeerAdapter(@NonNull Context context, ArrayList<Beer> beers) {
        super(context, 0, beers);
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
        textViewNameBeer.setText(beer.getName());
        textViewTaglineBeer.setText(beer.getTagline());
        Picasso.get().load(beer.getUrlImage()).into(imageBeer);
        Integer srm =  beer.getSrm();
        if(!srmCode.containsKey(beer.getSrm())) srm = 30;
        linearLayoutH.setBackgroundColor(Color.parseColor(srmCode.get(srm)));

        currentItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("It's clicked !");
            }
        });

        return currentItemView;
    }

    private void intialisationHashMapSrm(){
        String[] codes_color = {"#FFFFFF","#FFE699", "#FFD878", "#FFCA5A", "#FFBF42", "#FBB123", "#F8A600", "#F39C00", "#EA8F00", "#E58500", "#DE7C00", "#D77200", "#CF6900", "#CB6200", "#C35900", "#945534", "#925536", "#844C33", "#744229", "#683A22", "#573512", "#492A0B", "#3A2102", "#361F1B", "#261716",
                "#231716", "#19100F", "#16100F", "#120D0C","#100B0A", "#050B0A"};
        srmCode = new HashMap<>();
        Integer i = 0;
        for(String code_color : codes_color){
            srmCode.put(i,code_color);
            i++;
        }

    }
}
