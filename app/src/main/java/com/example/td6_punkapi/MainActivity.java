package com.example.td6_punkapi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.td6_punkapi.adapter.BeerAdapter;
import com.example.td6_punkapi.model.Beer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView listViewBeers;
    private ArrayList<Beer> beers;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beers = new ArrayList<>();

        EditText editTextNameBeer = findViewById(R.id.nameBeer1);
        Button buttonRechercher = findViewById(R.id.recherche);
        SeekBar seekBarNumberMaxLines = findViewById(R.id.seekBar);
        TextView textViewLignesMax = findViewById(R.id.textViewNombre);


        seekBarNumberMaxLines.setMax(150);
        seekBarNumberMaxLines.setMin(5);

        seekBarNumberMaxLines.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            double valueSeekBar = 0.0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewLignesMax.setText(i+" Max. de lignes");
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
                System.out.println(seekBarNumberMaxLines.getProgress());
                if(name_beer != "") addBeersByFetchUrl(name_beer, seekBarNumberMaxLines.getProgress());
                for(Beer beer : beers){
                    System.out.println(beer.toString());
                }
            }
        });

        listViewBeers = findViewById(R.id.resultsBeers);
        BeerAdapter beerAdapter = new BeerAdapter(this, beers);
        listViewBeers.setAdapter(beerAdapter);
    }


    private void addBeersByFetchUrl(String query, Integer numberMaxLines){
        //UN PAGE
        beers = new ArrayList<>();

        final Integer per_page = 80;
        System.out.println("Starting");
        Integer i = 1;
        while(numberMaxLines > 80){
            addBeersByJson(query, i, 80, 80);
            numberMaxLines-=80;
            i++;
        }
        addBeersByJson(query, i, 80,numberMaxLines);
    }

    public interface customCallBack {
        void onCompleted(Exception e, Response<JsonObject> response);
    }


    private void addBeersByJson(String query, Integer i, Integer per_page, Integer limit){
        Ion.with(this).load("https://api.punkapi.com/v2/beers?beer_name="+query+"&page="+i+"&per_page="+per_page).asJsonArray().setCallback(new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray result) {
                Integer countLimit = limit;
                for(JsonElement jsonElement : result){
                    JsonObject j = jsonElement.getAsJsonObject();
                    String name = j.get("name").getAsString();
                    String tagling = j.get("tagline").getAsString();

                    //VERIFICATION URL
                    JsonElement jsonImageUrl = j.get("image_url");
                    String imageUrl;
                    if(jsonImageUrl.isJsonNull()) imageUrl = "available";
                    else imageUrl = jsonImageUrl.getAsString();

                    //VERIFICATION SRM
                    JsonElement jsonSrm = j.get("srm");
                    Integer srm;
                    if(jsonSrm.isJsonNull()) srm = 0;
                    else srm = Math.round(jsonSrm.getAsFloat());


                    Beer beer = new Beer(name,tagling,imageUrl, srm);
                    beers.add(beer);
                    if(countLimit-- == 0) break;
                }
            }
        });

    }


}