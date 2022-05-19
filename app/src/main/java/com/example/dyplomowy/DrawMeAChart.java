package com.example.dyplomowy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DrawMeAChart extends AppCompatActivity {

    private LineChart lineChart;
    private LineData lineData;
    private List<Entry> entryList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_me_achart);
        lineChart = findViewById(R.id.lineChart);
        String currentRoute = getIntent().getStringExtra("currentName");

        try {
            List<String[]> lista = readThirdColumn(currentRoute, this);
            for (String[] s : lista)
                stringList.add(s[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < stringList.size(); i++) {
            entryList.add(new Entry(i, Float.parseFloat(stringList.get(i))));
        }
        LineDataSet lineDataSet = new LineDataSet(entryList, "Wysokość w metrach");
        XAxis axis = lineChart.getXAxis();
        YAxis ayis = lineChart.getAxisRight();
        ayis.setEnabled(false);
        axis.setEnabled(false);
        lineDataSet.setLineWidth(2);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setHighlightEnabled(false);

        lineData = new LineData(lineDataSet);
        lineChart.setPinchZoom(true);
        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(stringList.size());
        lineChart.invalidate();

    }

    static List<String[]> readThirdColumn(String filename, Context context) throws IOException {
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

}