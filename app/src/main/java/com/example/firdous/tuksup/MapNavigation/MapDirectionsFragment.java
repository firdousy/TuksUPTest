package com.example.firdous.tuksup.MapNavigation;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firdous.tuksup.BottomNavActivity;
import com.example.firdous.tuksup.CustomComponents.CustomPopUp;
import com.example.firdous.tuksup.Listeners.OnBuildingSelectedListener;
import com.example.firdous.tuksup.R;
import com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters.BuildingAdapter;
import com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters.BuildingSuggestionsAdapter;
import com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters.DirectionsRecyclerViewAdapter;
import com.example.firdous.tuksup.models.Building;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //MapDirectionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapDirectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//
public class MapDirectionsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap map;
    private MapView mapView;
    private View rootView;
    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private List<Building> buildingList;
    private Building startLocation;
    private Building endDestination;
    private Marker begin;
    private Marker end;
    private final String MAP_KEY = "AIzaSyCTK0aSlgowQYjJQND6qP8vMUYFo7JN9z0";
    private ArrayList<Polyline> polylines;
    private ArrayList<Marker> markers;
    GridLayout searchViewBox;
    ImageButton searchMode;
    ImageButton infoMode;
    ImageButton toggleView;
    RecyclerView recyclerView;
    GridLayout directionsBox;
    Context _th;

    private int CAMERA_TILT = 60;
    private FusedLocationProviderClient mFusedLocationClient;


    //private MapFragment mMapFragment;
    private FirebaseFirestore firestoreData;

    // private OnFragmentInteractionListener mListener;

    public MapDirectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapDirectionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapDirectionsFragment newInstance() {
        MapDirectionsFragment fragment = new MapDirectionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Do a null check to confirm that we have not already instantiated the
        // map.
//        if (mMapFragment == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
//            // Check if we were successful in obtaining the map.
//            if (mMapFragment != null) {
//                mMapFragment.getMapAsync(this);
//            }
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _th = getContext();
        rootView = inflater.inflate(R.layout.fragment_map_directions, container, false);

        searchViewBox = rootView.findViewById(R.id.searchViewBox);
        searchMode = rootView.findViewById(R.id.searchMode);

        searchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchBox();
            }
        });

        infoMode = rootView.findViewById(R.id.infoMode);
        infoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearchBox();
            }
        });

        polylines = new ArrayList<>();
        markers = new ArrayList<>();


        hideSearchBox();

        directionsBox = rootView.findViewById(R.id.directionsList);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));

        ImageButton closeDirectionsBox = rootView.findViewById(R.id.action_close_directions);
        closeDirectionsBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDirectionsBox();
            }
        });

        if (map == null) {
            mapView = (MapView) rootView.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);

            // Gets to GoogleMap from the MapView and does initialization stuff
            mapView.getMapAsync(this);


        }

        // Inflate the layout for this fragment

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        return rootView;
    }

    private void showSearchBox() {

        searchViewBox.setAlpha(0f);
        searchViewBox.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        searchViewBox.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(null);

        infoMode.setAlpha(0f);
        infoMode.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        infoMode.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(null);

        searchMode.animate()
                .alpha(0f)
                .setDuration(800)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        searchMode.setVisibility(View.GONE);
                    }
                });

        removeMapMarkers();

    }

    private void hideSearchBox() {


        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        searchViewBox.animate()
                .alpha(0f)
                .setDuration(800)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        searchViewBox.setVisibility(View.GONE);
                    }
                });

        searchMode.setAlpha(0f);
        searchMode.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        searchMode.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(null);

        infoMode.animate()
                .alpha(0f)
                .setDuration(800)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        infoMode.setVisibility(View.GONE);
                    }
                });

        addMapMarkers();
    }

    //  @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        //mListener = null;
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        toggleView = rootView.findViewById(R.id.toggleView);
        toggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CAMERA_TILT == 0) {
                    CAMERA_TILT = 60;
                } else {
                    CAMERA_TILT = 0;
                }

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(CAMERA_TILT)
                        .zoom(map.getCameraPosition().zoom)
                        .target(map.getCameraPosition().target)
                        .bearing(0)
                        .build();                   // Creates a CameraPosition from the builder
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        });


        //map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(true);


        map.setMinZoomPreference(17);
        map.setMaxZoomPreference(30);

        CustomPopUp customInfoWindow = new CustomPopUp(getActivity());
        map.setInfoWindowAdapter(customInfoWindow);

        enableMyLocation();
        //Initialize Google Play Services
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(rootView.getContext(),
//                    ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//
//                map.setMyLocationEnabled(true);
//            }
//            else{
//
//                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);.
//            }
//        } else {
//            map.setMyLocationEnabled(true);
//        }

        //MapsInitializer.initialize(this.getActivity());

//            try {
//                MapsInitializer.initialize(this.getActivity());
//            } catch (GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }

        // Updates the location and zoom of the MapView

        LatLng UnivPretoria = new LatLng(-25.7545492, 28.2292589);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(UnivPretoria, 19);


        //CameraPosition
        map.animateCamera(cameraUpdate);

        LatLngBounds campus = new LatLngBounds(new LatLng(-25.75577, 28.22527), new LatLng(-25.75174, 28.24019));
        map.setLatLngBoundsForCameraTarget(campus);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(UnivPretoria)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(CAMERA_TILT)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        firestoreData = FirebaseFirestore.getInstance();
        final CollectionReference buildings = firestoreData.collection("buildings");

        buildingList = new ArrayList<>();

        if (map.isMyLocationEnabled()) {

            getLastKnownLocation();

//            GeoPoint geoPoint = new GeoPoint(map.lo().getLatitude(), map.getMyLocation().getLongitude());
//           Building temp = new Building("-1", "Current Location", "location", geoPoint , new ArrayList<String>());
//            buildingList.add(temp);
        }



        buildings.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    //System.out.println(document.getId() + " => " + document.getData());
                    Building temp = document.toObject(Building.class);
                    buildingList.add(temp);
                    //Log.d("LessonActivity", document.getData().toString());
                }


                setLocationsAvailable();
                addMapMarkers();
               // List<String> test = buildingList.stream().map( Building -> Building.getName()).collect(Collectors.toList());

            }
        });

        Button directionsBtn = rootView.findViewById(R.id.directionsBtn);

        directionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(end != null && begin != null){
                    GetDirections();

                }
                else{

                    if(begin == null){
                        Toast.makeText(getActivity(), "Select a start point", Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(getActivity(), "Select a end point", Toast.LENGTH_LONG).show();
                    }


                }

            }
        });



        //map.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //show search bar A
        //show search bar B
        //show 3d layer toggle button

        //if location enabled && location within the map bounds
        // add location to list

    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Building loc = buildingList.stream().filter(subj -> "_1".equals(subj.getId()))
                                    .findAny()
                                    .orElse(null);
                            if(loc != null){
                                int index = buildingList.indexOf(loc);
                                loc.setLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
                                buildingList.set(index, loc);
                            }else{
                                Building temp = new Building("-1", "Current Location", "location", new GeoPoint(location.getLatitude(), location.getLongitude()) , new ArrayList<String>());

                                buildingList.add(temp);
                            }
                            //return new GeoPoint(location.getLatitude(), location.getLongitude());
                            // Logic to handle location object
                        }
                    }
                });
    }

    private void removeMapMarkers() {

        for (Marker item: markers) {
            item.remove();
        }

        markers.clear();
    }

    private void addMapMarkers() {

        if(markers.size() > 0)
            removeMapMarkers();

        markers = new ArrayList<>();

        if(buildingList != null && buildingList.size() > 0){
            for (Building item: buildingList) {
                LatLng pos = new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude());

                if(item.getId()!= "_1"){
                    Marker temp = map.addMarker(new MarkerOptions()
                            .position(pos)
                            .title(item.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker()));

                    temp.setTag(item != null ? item : new Building());

                    markers.add(temp);

                }

            }
        }

    }

    private void setLocationsAvailable() {

        BuildingSuggestionsAdapter startingAdapter = new BuildingSuggestionsAdapter(getActivity(), R.layout.content_building_card, buildingList,
                new OnBuildingSelectedListener() {
                    @Override
                    public void OnBuildingSelected(Building item) {
                        startLocation = item;

                        if(item != null){
                            LatLng pos = new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude());

                            if(begin == null)
                            {

                                begin = map.addMarker(new MarkerOptions()
                                        .position(pos)
                                        .title(item.getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a)));
                                       // BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                begin.setTag(item != null ? item : new Building());

                                zoomToExtent();

                            }
                            else if(!(begin.getPosition().latitude == item.getLocation().getLatitude() && begin.getPosition().longitude == item.getLocation().getLongitude()))
                            {
                                begin.setPosition(pos);
                                begin.setTag(item != null ? item : new Building());
                                begin.setTitle(item.getName());
                                //map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 19));

                                zoomToExtent();
                            }
                        }
                        else {
                            begin.remove();
                        }

                    }
                });

        BuildingSuggestionsAdapter destAdapter = new BuildingSuggestionsAdapter(getActivity(), R.layout.content_building_card ,buildingList,
                new OnBuildingSelectedListener() {
                    @Override
                    public void OnBuildingSelected(Building item) {
                        startLocation = item;

                        if(item != null){

                            LatLng pos = new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude());

                            if(end == null){
                                end = map.addMarker(new MarkerOptions()
                                        .position(pos)
                                        .title(item.getName())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b)));
                                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                end.setTag(item != null ? item : new Building());

                                zoomToExtent();
                            }
                            else if(!(end.getPosition().latitude == item.getLocation().getLatitude() && end.getPosition().longitude == item.getLocation().getLongitude())){
                                end.setPosition(pos);
                                end.setTag(item != null ? item : new Building());
                                end.setTitle(item.getName());

                                zoomToExtent();
                            }
                        }
                        else
                        {
                            end.remove();
                        }

                    }
                });

        AutoCompleteTextView startPoint = rootView.findViewById(R.id.startPoint);
        startPoint.setThreshold(0);
        startPoint.setAdapter(startingAdapter);

        AutoCompleteTextView endPoint = rootView.findViewById(R.id.endPoint);
        endPoint.setThreshold(0);
        endPoint.setAdapter(destAdapter);
    }

    private void zoomToExtent() {

        if(begin != null && end != null){


            LatLngBounds extent = LatLngBounds.builder()
                    .include(begin.getPosition())
                    .include(end.getPosition())
                    .build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(extent, 50), 300, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .bearing(0)                // Sets the orientation of the camera to east
                            .tilt(CAMERA_TILT)
                            .zoom(map.getCameraPosition().zoom)
                            .target(map.getCameraPosition().target)
                            .bearing(0)
                            .build();                   // Creates a CameraPosition from the builder
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                @Override
                public void onCancel() {

                }
            });


        }
        else if(begin!= null){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(begin.getPosition(), 19));
        }
        else {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(end.getPosition(), 19));
        }
    }

    private void enableMyLocation() {
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(rootView.getContext(),
                    ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }
            else{
                map.getUiSettings().setMyLocationButtonEnabled(false);

                ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        } else {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

        }

    }

    public void GetDirections()
    {

        //if search bar A && search bar B
        //get Directions
        String urlReq = getRequestUrl();

        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(urlReq);

        //requestDirection(urlReq);
    }

    private void showDirectionsBox() {

        directionsBox.setAlpha(1f);
        directionsBox.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        directionsBox.animate();
//                .alpha(1f)
//                .setDuration(1)
//                .setListener(null);

    }

    private void hideDirectionsBox(){
        directionsBox.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        directionsBox.setVisibility(View.GONE);
                    }
                });
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    private String getRequestUrl(){
        String str_org = "origin="+ begin.getPosition().latitude+ ","+ begin.getPosition().longitude;
        String dest = "destination="+ end.getPosition().latitude+ ","+ end.getPosition().longitude;

        String sensor = "sensor=false";

        String mode = "mode=walking";

        PackageManager pm = getActivity().getPackageManager();
        PackageItemInfo info;

        String apikey = "key="+MAP_KEY;

        String param = str_org + "&"+ dest + "&"+sensor+"&"+ mode + "&"+apikey;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+param;

        return  url;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(locationAccepted)
                        enableMyLocation();
                        //map.setMyLocationEnabled(true);
                }

                break;
            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }


    //When the connect request has successfully completed
//    @Override
//    public void onConnected(Bundle bundle) {}
//
//    //Called when the client is temporarily in a disconnected state.
//    @Override
//    public void onConnectionSuspended(int i) {
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, Direction > {

        @Override
        protected Direction doInBackground(String... strings) {
            JSONObject jsonObject = null;
            //List<List<HashMap<String, String>>> routes = null;
            Direction direction = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                direction = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return direction;
        }

        @Override
        //protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
        protected void onPostExecute(Direction direction) {
            //Get list route and display it into the map

            List<List<HashMap<String, String>>> lists = direction.routes;

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            if (polylines.size() > 0)
                clearExistingPolylines();
            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                Polyline line = map.addPolyline(polylineOptions);
                polylines.add(line);
            } else {
                Toast.makeText(getContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

            showDirectionsBox();


            TextView time = rootView.findViewById(R.id.totalTime);

            String t = "Estimated Time: <b>"+(direction.totalTime/60) + " minute(s)</b>";
            time.setText(Html.fromHtml(t, Html.FROM_HTML_MODE_LEGACY));

            DirectionsRecyclerViewAdapter adapter = new DirectionsRecyclerViewAdapter(_th, direction.directions);

            recyclerView.setAdapter(adapter);


        }
    }

    private void clearExistingPolylines() {

        for (Polyline line:polylines) {
            line.remove();
        }

        polylines.clear();
    }
}
