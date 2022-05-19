package com.example.dyplomowy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FinalActivity extends AppCompatActivity {
    private Button map;
    private Button altitudeProfile;
    private LatLng currentLoc;
    private TextView routeName;
    private TextView routeLength;
    private TextView routeAltitude;
    private Poly poly;
    private String[] rData;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        poly = getIntent().getExtras().getParcelable("currentRoute");
        routeName = findViewById(R.id.routeFoundName);
        routeLength = findViewById(R.id.routeLength);
        routeAltitude = findViewById(R.id.altitudeDifference);
        map = findViewById(R.id.showMap);
        altitudeProfile = findViewById(R.id.showAltitude);
        try {
            rData = getRouteData(poly.currentRouteName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        back = findViewById(R.id.backToLauncher);

        back.setOnClickListener(v -> {
            Intent backToStart = new Intent(this, LauncherActivity.class);
            startActivity(backToStart);
        });

        map.setOnClickListener(v -> {
            Intent getMap = new Intent(this, MapsActivity.class);
            getMap.putExtra("currentRoute", poly);
            startActivity(getMap);
        });

        altitudeProfile.setOnClickListener(v -> {
            Intent getMap = new Intent(this, DrawMeAChart.class);
            getMap.putExtra("currentName", poly.currentRouteName);
            startActivity(getMap);
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        routeName.setText(String.format(getString(R.string.youreCurrentlyOnRoute), rData[0]));
        routeLength.setText(String.format(getString(R.string.fullRouteLength), rData[1]));
        routeAltitude.setText(String.format(getString(R.string.altitudeDiff), rData[2]));
    }

    static List<String[]> readCSV(String filename, Context context) throws IOException {
        List<String[]> currentLine = new ArrayList<>();
        InputStream is = context.getAssets().open("maps/" + filename);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        String csvSplitBy = ",";

        br.readLine();

        while ((line = br.readLine()) != null) {
            String[] row = line.split(csvSplitBy);
            currentLine.add(row);
        }
        return currentLine;
    }

    private String[] getRouteData(String fileName) throws IOException {
        String[] routeData = new String[3];
        List<String[]> dist = readCSV("distances.csv", this);
        List<String[]> elev = readCSV("elevations.csv", this);
        switch (fileName) {
            case "BKartuski.plt":
                routeData[0] = getString(R.string.BKartuski_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "CDomowPodcienianych.plt":
                routeData[0] = getString(R.string.CDomowPodcienianych_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "CWiezaKolibkowska.plt":
                routeData[0] = getString(R.string.CWiezaKolibkowska_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "CWzgorzSzymbarskich.plt":
                routeData[0] = getString(R.string.CWzgorzSzymbarskich_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "CZagorskiejStrogi.plt":
                routeData[0] = getString(R.string.CZagorskiejStrogi_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "CZrodlaMarii.plt":
                routeData[0] = getString(R.string.CZrodlaMarii_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "GSkarszewski.plt":
                routeData[0] = getString(R.string.GSkarszewski_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "GWyspySobieszewskiej.plt":
                routeData[0] = getString(R.string.GWyspySobieszewskiej_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "RFortyfikacjiNadmorskich.plt":
                routeData[0] = getString(R.string.RFortyfikacjiNadmorskich_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "RMotlawski.plt":
                routeData[0] = getString(R.string.RMotlawski_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "RWejherowski.plt":
                routeData[0] = getString(R.string.RWejherowski_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "YBursztynowy.plt":
                routeData[0] = getString(R.string.YBursztynowy_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "YKepyRedlowskiej.plt":
                routeData[0] = getString(R.string.YKepyRedlowskiej_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
            case "YTrojmiejski.plt":
                routeData[0] = getString(R.string.YTrojmiejski_plt);
                for (String[] s : dist){
                    if(s[0].equals(fileName)) {
                        routeData[1] = s[1];
                        break;
                    }
                }
                for (String[] s : elev){
                    if(s[0].equals(fileName)) {
                        routeData[2] = s[1];
                        break;
                    }
                }
                break;
        }
        return routeData;
    }

}