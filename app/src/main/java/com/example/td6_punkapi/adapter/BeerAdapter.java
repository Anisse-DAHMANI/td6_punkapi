package com.example.td6_punkapi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.td6_punkapi.R;
import com.example.td6_punkapi.model.Beer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BeerAdapter extends ArrayAdapter<Beer> {

    public BeerAdapter(@NonNull Context context, ArrayList<Beer> beers) {
        super(context, 0, beers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_beer, parent, false);
        }

        TextView textViewNameBeer = currentItemView.findViewById(R.id.nameBeer2);
        TextView textViewTaglineBeer = currentItemView.findViewById(R.id.taglineBeer);
        ImageView imageBeer = currentItemView.findViewById(R.id.imageBeer);

        Beer beer = getItem(position);

        textViewNameBeer.setText(beer.getName());
        textViewTaglineBeer.setText(beer.getTagline());

        Picasso.get().load(beer.getUrlImage()).into(imageBeer);

        currentItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("It's clicked !");
            }
        });

        return super.getView(position, convertView, parent);
    }
}
