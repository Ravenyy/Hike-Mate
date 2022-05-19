package com.example.dyplomowy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class CreateRoutes extends AppCompatActivity {

    private RoutesBase routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String color =  intent.getStringExtra("colorKey");
        Log.d("kolor", color);

        Thread thread = new Thread(() -> routes =
                new RoutesBase(CreateRoutes.this, color));

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.d("threadFail", "something's not yes");
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(CreateRoutes.this, GetCurrentRoute.class);
        Log.d("mainTag", String.valueOf(routes.routesMap.keySet()));
        intent.putExtra("routesKey", routes);
        startActivity(intent);

    }
}