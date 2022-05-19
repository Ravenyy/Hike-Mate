package com.example.dyplomowy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


public class LauncherActivity extends AppCompatActivity {

    private final int CAMERA_REQUEST_CODE = 333;
    Button camera;
    Button list;
    String[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        camera = findViewById(R.id.startCamera);
        list = findViewById(R.id.showList);
        list.setOnClickListener(v -> colorList());
        camera.setOnClickListener(v -> startCamera());
    }

    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void startCamera(){
        if (!checkCameraHardware(this)){
            Toast.makeText(this, "To urządzenie nie ma kamery", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(this, CameraKit.class);
            startActivity(intent);
        }
    }

    private void colorList(){
        colors = new String[]{getString(R.string.red), getString(R.string.blue),
                getString(R.string.green), getString(R.string.yellow), getString(R.string.black)};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LauncherActivity.this);
        mBuilder.setTitle(getString(R.string.chooseColor));
        mBuilder.setIcon(R.drawable.listicon);
        mBuilder.setSingleChoiceItems(colors, -1, (dialog, which) -> {
            Intent intent = new Intent(LauncherActivity.this, CreateRoutes.class);
            intent.putExtra("colorKey", colors[which]);
            startActivity(intent);

        });
        mBuilder.setNeutralButton(R.string.back, (dialog, which) -> {

        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

}