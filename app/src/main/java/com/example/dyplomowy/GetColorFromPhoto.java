package com.example.dyplomowy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class GetColorFromPhoto extends AppCompatActivity {
    private String color;
    private File img;
    private Palette p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img = new File(this.getCacheDir(), "photo.bmp");
        OpenCVLoader.initDebug();
        cropAndBlur(img);
    }

    @Override
    protected void onStart() {
        super.onStart();
        findColor(img);
    }

    private void cropAndBlur(File img){
        Bitmap sourceBitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
        int rectHeight = 100;
        int rectWidth = 200;
        Mat sourceMat = Imgcodecs.imread(img.getAbsolutePath());
        int bottomY = sourceBitmap.getHeight()/2 - rectHeight/2;
        int leftX = sourceBitmap.getWidth()/2 - rectWidth/2;
        Mat roiMat = sourceMat.submat(bottomY, bottomY + rectHeight, leftX, leftX + rectWidth);
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(roiMat, blurred, new Size(9, 9), 0);
        Imgcodecs.imwrite(img.getAbsolutePath(), blurred);

        Log.d("wymiarpath", img.getAbsolutePath());
        Log.d("wymiaryWidth", String.valueOf(sourceBitmap.getWidth()) + ',' + leftX);
        Log.d("wymiaryHeight", String.valueOf(sourceBitmap.getHeight()) + ',' + bottomY);
    }


    private void findColor(File img) {
        Bitmap bmp = BitmapFactory.decodeFile(img.getAbsolutePath());
        Palette p = Palette.from(bmp).generate();
        double red = Color.red(p.getDominantColor(0));
        double green = Color.green(p.getDominantColor(0));
        double blue = Color.blue(p.getDominantColor(0));
        Log.d("kolorRGB", red + ", " + green + ", " + blue);
        color = getColor(rgbToHsv(red, green, blue));
        if(color.equals("placeholder")){
            Intent noColor = new Intent(GetColorFromPhoto.this, CreateRoutes.class);
            noColor.putExtra("colorKey", color);
            startActivity(noColor);
        }
        else{
            Intent foundColor = new Intent(GetColorFromPhoto.this, CreateRoutes.class);
            foundColor.putExtra("colorKey", color);
            startActivity(foundColor);
        }
        Log.d("kolorek", color);
    }

    static double[] rgbToHsv(double r, double g, double b)
    {
        double[] hsv = new double[3];
        r = r / 255.0;
        g = g / 255.0;
        b = b / 255.0;
        double cmax = Math.max(r, Math.max(g, b));
        double cmin = Math.min(r, Math.min(g, b));
        double diff = cmax - cmin;
        double h = -1, s = -1;
        if (cmax == cmin)
            h = 0;
        else if (cmax == r)
            h = (60 * ((g - b) / diff) + 360) % 360;
        else if (cmax == g)
            h = (60 * ((b - r) / diff) + 120) % 360;
        else if (cmax == b)
            h = (60 * ((r - g) / diff) + 240) % 360;

        if (cmax == 0)
            s = 0;
        else
            s = (diff / cmax) * 100;

        double v = cmax * 100;

        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = v;
        Log.d("hsv", String.valueOf(hsv[0]));
        Log.d("hsv", String.valueOf(hsv[1]));
        Log.d("hsv", String.valueOf(hsv[2]));

        return hsv;
    }

    private String getColor(double[] hsv){
        if (hsv[1] < 10 && hsv[2] > 90)
            return "placeholder";
        else if (hsv[2] < 10)
            return getString(R.string.black);
        else {
            double deg = hsv[0];
            Log.d("hsvDeg", String.valueOf(deg));
            if (deg >= (double) 0 && deg < (double) 30)
                return getString(R.string.red);
            else if (deg >= (double) 30 && deg < (double) 90)
                return getString(R.string.yellow);
            else if (deg >= (double) 90 && deg < (double) 160)
                return getString(R.string.green);
            else if (deg >= (double) 160 && deg < (double) 195)
                return "placeholder";
            else if (deg >= (double) 195 && deg < (double) 270)
                return getString(R.string.blue);
            else if (deg >= (double) 280 && deg < (double) 320)
                return "placeholder";
            else
                return getString(R.string.red);
        }
    }
}