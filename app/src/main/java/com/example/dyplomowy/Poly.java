package com.example.dyplomowy;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Poly implements Parcelable {
    public List<LatLng> currentRoute;
    public String currentRouteName = "placeholder";
    public LatLng currentLoc;

    public Poly(RoutesBase routes, LatLng currentLoc){
        this.currentRoute = scanRoutes(routes.routesMap, currentLoc);
        this.currentLoc = currentLoc;
        Log.d("polyTag", String.valueOf(routes.routesMap.keySet()));
    }

    protected Poly(Parcel in) {
        currentRoute = in.createTypedArrayList(LatLng.CREATOR);
        currentRouteName = in.readString();
        currentLoc = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<Poly> CREATOR = new Creator<Poly>() {
        @Override
        public Poly createFromParcel(Parcel in) {
            return new Poly(in);
        }

        @Override
        public Poly[] newArray(int size) {
            return new Poly[size];
        }
    };

    private List<LatLng> scanRoutes(Map<String, List<String[]>> map, LatLng currentLoc){
        for (String s : map.keySet()){
            List<LatLng> route = createLatLngList(s, map);
            if (PolyUtil.isLocationOnPath(currentLoc, route, true, 100)) {
                currentRouteName = s;
                return route;
            }
        }
        return null;
    }

    private List<LatLng> createLatLngList(String route, Map<String, List<String[]>> map){
        List<LatLng> lista = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(map.get(route)).size(); i++){
            LatLng coords = new LatLng(
                    Double.parseDouble(Objects.requireNonNull(map.get(route)).get(i)[0]),
                    Double.parseDouble(Objects.requireNonNull(map.get(route)).get(i)[1])
            );
            lista.add(coords);
        }
        return lista;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(currentRoute);
        dest.writeString(currentRouteName);
        dest.writeParcelable(currentLoc, flags);
    }
}
