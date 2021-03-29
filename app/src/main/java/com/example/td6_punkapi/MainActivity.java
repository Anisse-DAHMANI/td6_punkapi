package com.example.td6_punkapi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.td6_punkapi.adapter.BeerAdapter;
import com.example.td6_punkapi.model.Beer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ListView listViewBeers;
    private ArrayList<Beer> beers;
    private BeerAdapter beerAdapter;

    private EditText editTextNameBeer;
    private SeekBar seekBarNumberMaxLines;
    private ProgressDialog progress;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //image loading
        progress = new ProgressDialog(this);
        progress.setMessage("Chargement des bières");
        progress.setCancelable(false);

        beers = new ArrayList<>();

        listViewBeers = findViewById(R.id.resultsBeers);
        beerAdapter = new BeerAdapter(this, beers);
        listViewBeers.setAdapter(beerAdapter);

        editTextNameBeer = findViewById(R.id.nameBeer1);
        seekBarNumberMaxLines = findViewById(R.id.seekBar);

        Button buttonRechercher = findViewById(R.id.recherche);
        TextView textViewLignesMax = findViewById(R.id.textViewNombre);


        seekBarNumberMaxLines.setMax(80);
        seekBarNumberMaxLines.setMin(5);

        seekBarNumberMaxLines.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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
                progress.show();
                FetchApiPunk fetchApiPunk = new FetchApiPunk();
                fetchApiPunk.execute();
            }
        });
    }

    public class BeerComparatorByEbc implements Comparator<Beer> {

        @Override
        public int compare(Beer b1, Beer b2) {
            return b1.getEbc().compareTo(b2.getEbc());
        }
    }


    public class FetchApiPunk extends AsyncTask<Void, String, String> {

        @Override
        public void onPreExecute() {
            super .onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            beers.clear();
            String result = null;

            String query = editTextNameBeer.getText().toString().equals("") ? "" : "beer_name=" + editTextNameBeer.getText().toString()+"&";

            try {
                URL url = new URL("https://api.punkapi.com/v2/beers?"+query+"per_page="+seekBarNumberMaxLines.getProgress());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }

            } catch (Exception  e) {
                e.printStackTrace();
            }
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onPostExecute(String s) {

            super .onPostExecute(s);
            try {
                JSONArray array = new JSONArray(s);



                for (int i = 0; i < array.length(); i++) {

                    JSONObject j = array.getJSONObject(i);
                    Integer id = j.getInt("id");
                    String name = j.getString("name");
                    String tagline = j.getString("tagline");

                    //VERIFICATION URL
                    String imageUrl = j.getString("image_url");
                    Double ebc = j.isNull("ebc") ? null : j.getDouble("ebc");
                    Beer beer = new Beer(id,name,tagline,imageUrl, ebc);
                    beers.add(beer);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            beers.sort(new BeerComparatorByEbc());
            beerAdapter.notifyDataSetChanged();
            progress.dismiss();
        }
    }
}