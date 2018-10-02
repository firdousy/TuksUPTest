package com.example.firdous.tuksup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.firdous.tuksup.MapNavigation.MapDirectionsFragment;

import com.example.firdous.tuksup.Singletons.SessionSingleton;
import com.example.firdous.tuksup.SubjectBuilder.AddSubjectActivity;
import com.example.firdous.tuksup.SubjectBuilder.SubjectFragment;
import com.example.firdous.tuksup.SubjectBuilder.dummy.DummyContent;
import com.example.firdous.tuksup.Timetable.TimetableFragment;
import com.example.firdous.tuksup.WeekdayView.BlankFragment;
import com.example.firdous.tuksup.models.Subject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BottomNavActivity extends AppCompatActivity implements SubjectFragment.OnListFragmentInteractionListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreData;
    private ActionBar toolbar;

    public static Subject selectedSubject;

    public static SharedPreferences sharedPreferences;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle(R.string.campusmap);
                    fragment = new MapDirectionsFragment();
                    loadFragment(fragment);
                    return true;
//                case R.id.navigation_dashboard:
//                    return true;
//                case R.id.navigation_notifications:
//                    return true;
                case R.id.navigation_subjects:
                        toolbar.setTitle(R.string.subjects);
                        fragment = new SubjectFragment();
                        loadFragment(fragment);
                    return true;
                case R.id.navigation_schedule:
                    toolbar.setTitle("Schedule");
//                    fragment = new TimetableFragment();
//                    loadFragment(fragment);

                    fragment = new BlankFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_logout:
                    signOut();
                    return true;
            }
            return false;
        }
    };

    private void signOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(this,LoginActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        firebaseAuth  = FirebaseAuth.getInstance();
        firestoreData = FirebaseFirestore.getInstance();
        toolbar = getSupportActionBar();

        setUpGlobalValues();

        CollectionReference buildings = firestoreData.collection("buildings");

        buildings.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               // List<DocumentSnapshot> l = queryDocumentSnapshots.getDocuments();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    System.out.println(document.getId() + " => " + document.getData());
                    Log.d("MainActivity", document.getData().toString());
                }
            }
        });

        Log.d("MainActivity", "Inside Done");
        toolbar.setTitle("Campus Map");
        loadFragment(new MapDirectionsFragment());
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        handleIntent(getIntent());

        //navigation.setSelectedItemId(R.id.navigation_home);

        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(thisActivity,
//                Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
//                    Manifest.permission.READ_CONTACTS)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(thisActivity,
//                        new String[]{Manifest.permission.READ_CONTACTS},
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            // Permission has already been granted
//        }

    }

    private void loadFragment(Fragment fragment) {
        // load fragment

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void onListFragmentInteraction(Subject item) {
        //existing Subject Clicked
        SessionSingleton.getInstance().setSelectedSubject(item);
        startActivity(new Intent(this,AddSubjectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    private void setUpGlobalValues(){
        sharedPreferences = getSharedPreferences("MY_DAY", MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }
}
