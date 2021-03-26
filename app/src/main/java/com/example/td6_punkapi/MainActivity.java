package com.example.td6_punkapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;

import com.example.td6_punkapi.adapter.BeerAdapter;
import com.example.td6_punkapi.model.Beer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewBeers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Beer> beers = new ArrayList<>();

        EditText editTextNameBeer = findViewById(R.id.nameBeer1);
        Button buttonRechercher = findViewById(R.id.recherche);
        SeekBar seekBarNumberMaxLines = findViewById(R.id.seekBar);

        final int step = 1;
        final int max = 150;
        final int min = 5;
        double valueSeekBar = 0.0;
        seekBarNumberMaxLines.setMax((max - min) / step );

        seekBarNumberMaxLines.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                valueSeekBar = min + (i * step);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonRechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_beer = editTextNameBeer.getText().toString();
                if(name_beer != "") beers = getBeersByFetchUrl(name_beer, seekBarNumberMaxLines.get);
            }
        });

        listViewBeers = findViewById(R.id.resultsBeers);
        BeerAdapter beerAdapter = new BeerAdapter(this, beers);
        listViewBeers.setAdapter(beerAdapter);
    }


    private ArrayList<Beer> getBeersByFetchUrl(String query, Integer numberMaxLines){
        //UN PAGE
        ArrayList<Beer> beers = new ArrayList<>();

        final Integer per_page = 80;

        if(numberMaxLines > 80){
            Integer i = 0;
            while(numberMaxLines > 80){
                beers.addAll(getBeersByJson(query, i, 80, 80));
                numberMaxLines-=80;
                i++;
            }
            beers.addAll(getBeersByJson(query, i, per_page, numberMaxLines));
        }

        return beers;
    }

    private ArrayList<Beer> getBeersByJson(String query, Integer i, Integer per_page, Integer limit){
        final Integer[] countLimit = {limit};
        ArrayList<Beer> beers = new ArrayList<>();
        Ion.with(this).load("https://api.punkapi.com/v2/beers?beer_name="+query+"&page="+i+"&per_page="+per_page).asJsonArray().setCallback(new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray result) {
                for(JsonElement jsonElement : result){
                    String name = jsonElement.getAsJsonObject().get("name").getAsString();
                    String tagling = jsonElement.getAsJsonObject().get("tagline").getAsString();
                    String imageUrl = jsonElement.getAsJsonObject().get("image_url").getAsString();
                    beers.add(new Beer(name,tagling,imageUrl));
                    if(countLimit[0]-- == 0) break;
                }
            }
        });
        return beers;
    }


}