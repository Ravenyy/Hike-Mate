package com.example.dyplomowy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;


public class EmptyFinalActivity extends AppCompatActivity {
    private Button map;
    private LatLng currentLoc;
    private TextView text;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_final);
        currentLoc = getIntent().getExtras().getParcelable("currentLoc");
        text = findViewById(R.id.routeNotFoundText);
        map = findViewById(R.id.showEmptyMap);
        back = findViewById(R.id.backToLauncher);
        back.setOnClickListener(v -> {
            Intent backToStart = new Intent(this, LauncherActivity.class);
            startActivity(backToStart);
        });
        map.setOnClickListener(v -> {
            Intent getMap = new Intent(this, EmptyMapActivity.class);
            getMap.putExtra("currentLoc", currentLoc);
            startActivity(getMap);
        });
    }

}