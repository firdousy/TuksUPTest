package com.example.firdous.tuksup.CustomComponents;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.app.Activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.firdous.tuksup.R;
import com.example.firdous.tuksup.models.Building;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class CustomPopUp implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomPopUp(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custominfo_window, null);

        TextView name = view.findViewById(R.id.place_name);
        ImageView img = view.findViewById(R.id.photo_place);


        name.setText(marker.getTitle());

        Building infoWindowData = (Building) marker.getTag();

        int imageId = context.getResources().getIdentifier(infoWindowData.getShortName().toLowerCase()+".jpg",
                "drawable", context.getPackageName());
        img.setImageResource(imageId);


        return view;
    }

//    static class MarkerCallback implements Drawable.Callback {
//        Marker marker=null;
//
//        MarkerCallback(Marker marker) {
//            this.marker=marker;
//        }
//
//        @Override
//        public void onError() {
//            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
//        }
//
//        @Override
//        public void onSuccess() {
//            if (marker != null && marker.isInfoWindowShown()) {
//                marker.showInfoWindow();
//            }
//        }
//    }
}
