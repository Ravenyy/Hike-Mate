package com.example.dyplomowy;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutesBase implements Parcelable {
    public Map<String, List<String[]>> routesMap = new HashMap<String, List<String[]>>();


    public RoutesBase(Context context, String color) {
        this.routesMap = mapLatLng(shortenList(listFiles(context), color, context), context);
    }


    protected RoutesBase(Parcel in) {
        in.readMap(routesMap, RoutesBase.class.getClassLoader());
    }

    public static final Creator<RoutesBase> CREATOR = new Creator<RoutesBase>() {
        @Override
        public RoutesBase createFromParcel(Parcel in) {
            return new RoutesBase(in);
        }

        @Override
        public RoutesBase[] newArray(int size) {
            return new RoutesBase[size];
        }
    };

    private String[] listFiles(Context context) {
        String[] fileList = new String[0];
        String dir = "maps";
        if (context == null) {
            Log.d("somesingwong", "I should never be here :(");
        } else {
            Resources res = context.getResources();
            AssetManager am = res.getAssets();
            try {
                fileList = am.list(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileList;
    }


    private List<String> shortenList(String[] list, String color, Context context){
        List<String> shortenedList = new ArrayList<>();
        if(color.equals(context.getString(R.string.red)))
            shortenedList = scanBigList(context.getString(R.string.red), 'R', list);
        else if(color.equals(context.getString(R.string.blue)))
            shortenedList = scanBigList(context.getString(R.string.blue), 'B', list);
        else if(color.equals(context.getString(R.string.green)))
            shortenedList = scanBigList(context.getString(R.string.green), 'G', list);
        else if(color.equals(context.getString(R.string.yellow)))
            shortenedList = scanBigList(context.getString(R.string.yellow), 'Y', list);
        else if(color.equals(context.getString(R.string.black)))
            shortenedList = scanBigList(context.getString(R.string.black), 'C', list);
        else{
            Log.d("somesingwong", "This should not have happened");
        }
        return shortenedList;
    }


    private List<String> scanBigList(String color, char colorChar, String[] list){
        List<String> shortenedList = new ArrayList<>();
        for (String s : list){
            char ch = s.charAt(0);
            if (ch == colorChar)
                shortenedList.add(s);
        }
        return shortenedList;
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

    static Map<String, List<String[]>> mapLatLng(List<String> fileList, Context context){
        Map<String, List<String[]>> map = new HashMap<>();
        for (String s : fileList)
            Log.d("isNull?", s);
        Log.d("contextNull?", String.valueOf(context));
        for (String s : fileList){
            try {
                map.put(s, readCSV(s, context));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(routesMap);
    }
}
