package com.example.td6_punkapi;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetailBeerActivity extends AppCompatActivity {

    private ProgressDialog progress;
    private TextView name;
    private TextView tagline;
    private TextView description;
    private ImageView imageBeer;
    private TextView first_brewed;
    private TextView ibu;
    private TextView abv;
    private TextView ebc;
    private TextView srm;
    private TextView ph;
    private FrameLayout colorEbc;

    private Integer id;
    private Integer color;

    private Intent intentForProduction;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);
        Bundle extras = getIntent().getExtras();

        id = extras.getInt("id");
        color = extras.getInt("colorEbc");

        name = findViewById(R.id.titre);
        tagline = findViewById(R.id.tagline1);
        description = findViewById(R.id.description);
        imageBeer = findViewById(R.id.imageBeer1);
        first_brewed =findViewById(R.id.first_brewed);
        ibu = findViewById(R.id.ibu);
        abv = findViewById(R.id.abv);
        ebc = findViewById(R.id.ebc);
        srm = findViewById(R.id.srm);
        ph = findViewById(R.id.ph);
        colorEbc = findViewById((R.id.colorEbc));

        progress = new ProgressDialog(this);
        progress.setMessage("Chargement de la bière selectionnée");
        progress.setCancelable(false);

        new FetchBeer().execute();

        Button versProduction = findViewById(R.id.versproduction);
        Button retour = findViewById(R.id.retour);

        versProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentForProduction);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public class FetchBeer extends AsyncTask<Void, String, String> {

        @Override
        public void onPreExecute() {
            super .onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;

            try {
                URL url = new URL("https://api.punkapi.com/v2/beers/"+id);
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
                JSONObject j = array.getJSONObject(0);
                String sonName = j.getString("name");
                String sonTagline = j.getString("tagline");
                String sonImageUrl = j.getString("image_url");
                String sonDate = j.getString("first_brewed");
                String sonDescription = j.getString("description");
                Double sonIbu = j.isNull("ibu") ? null : j.getDouble("ibu");
                Double sonAbv = j.isNull("abv") ? null : j.getDouble("abv");
                Double sonEbc = j.isNull("ebc") ? null : j.getDouble("ebc");
                Double sonSrm = j.isNull("srm") ? null : j.getDouble("srm");
                Double sonPh = j.isNull("ph") ? null : j.getDouble("ph");

                Double sonVolume = j.getJSONObject("boil_volume").getDouble("value");
                String sonUniteVolume = j.getJSONObject("boil_volume").getString("unit");
                Double sonMash_temp = j.getJSONObject("method").getJSONArray("mash_temp").getJSONObject(0).getJSONObject("temp").getDouble("value");
                Double sonFermentationTemp = j.getJSONObject("method").getJSONObject("fermentation").getJSONObject("temp").getDouble("value");

                JSONArray malts = j.getJSONObject("ingredients").getJSONArray("malt");
                JSONArray hops = j.getJSONObject("ingredients").getJSONArray("hops");
                JSONArray pairs = j.getJSONArray("food_pairing");

                ArrayList<String> hops_string = new ArrayList<>();
                ArrayList<String> malt_string = new ArrayList<>();
                ArrayList<String> pair_string = new ArrayList<>();

                String sonTip = j.getString("brewers_tips");


                for(int i = 0; i < hops.length(); i++){
                    hops_string.add(hops.getJSONObject(i).getString("name"));
                }

                for (int i = 0; i < malts.length(); i++){
                    malt_string.add(malts.getJSONObject(i).getString("name"));
                }

                for (int i = 0; i < pairs.length(); i++){
                    pair_string.add(pairs.getString(i));
                }

                intentForProduction = new Intent(getApplicationContext(), BeerActivityProduction.class);
                intentForProduction.putExtra("sonVolume", sonVolume);
                intentForProduction.putExtra("sonUniteVolume", sonUniteVolume);
                intentForProduction.putExtra("sonMash_temp", sonMash_temp);
                intentForProduction.putExtra("sonFermentationTemp", sonFermentationTemp);
                intentForProduction.putExtra("malts", malt_string);
                intentForProduction.putExtra("hops", hops_string);
                intentForProduction.putExtra("pairs",pair_string);
                intentForProduction.putExtra("sonTip",sonTip);


                name.setText(sonName);
                tagline.setText(sonTagline);
                Picasso.get().load(sonImageUrl).into(imageBeer);
                first_brewed.setText(sonDate);
                description.setText(sonDescription);
                ibu.setText(sonIbu == null ? "Inconnu" : sonIbu.toString());
                abv.setText(sonAbv == null ? "Inconnu" : sonAbv.toString()+"%");
                ebc.setText(sonEbc == null ? "✘" : sonEbc.toString());
                srm.setText(sonSrm == null ? "✘" : sonSrm.toString());
                ph.setText(sonPh.toString());
                System.out.println(color);
                colorEbc.setBackgroundColor(color);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progress.dismiss();
        }
    }

}
