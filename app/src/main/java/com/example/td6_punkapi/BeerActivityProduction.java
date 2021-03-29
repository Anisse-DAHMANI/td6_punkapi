package com.example.td6_punkapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BeerActivityProduction extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_production);

        Bundle b = getIntent().getExtras();

        Double volume = b.getDouble("sonVolume");
        String unit = b.getString("sonUniteVolume");
        Double mash_temp = b.getDouble("sonMash_temp");
        Double fermentation_temp = b.getDouble("sonFermentationTemp");
        ArrayList<String> malts = (ArrayList<String>) b.getSerializable("malts");
        ArrayList<String> hops = (ArrayList<String>) b.getSerializable("hops");
        ArrayList<String> pairs = (ArrayList<String>) b.getSerializable("pairs");
        String tip = b.getString("sonTip");

        TextView vol = findViewById(R.id.boil_volume);
        TextView mash = findViewById(R.id.mash_temp);
        TextView fermentation = findViewById(R.id.fermentation);
        TextView tiptext = findViewById(R.id.tip);
        LinearLayout malts_linear = findViewById(R.id.malt_linear);
        LinearLayout hops_linear = findViewById(R.id.hops_linear);
        LinearLayout pairs_linear = findViewById(R.id.pair_linear);

        vol.setText(volume+" "+unit);
        mash.setText(mash_temp + "°C");
        fermentation.setText(fermentation_temp+"°C");
        tiptext.setText(tip);

        prepareLayoutLinear(malts, malts_linear);
        prepareLayoutLinear(hops, hops_linear);
        prepareLayoutLinear(pairs,pairs_linear);


        Button retour = findViewById(R.id.retour1);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void prepareLayoutLinear(ArrayList<String> objects, LinearLayout linear){
        for (String o : objects){
            TextView objecttext = new TextView(this);
            objecttext.setText("● " +o + " ●");
            objecttext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            linear.addView(objecttext);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
