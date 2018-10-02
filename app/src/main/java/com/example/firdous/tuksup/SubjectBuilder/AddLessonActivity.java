package com.example.firdous.tuksup.SubjectBuilder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;

import com.example.firdous.tuksup.Listeners.OnBuildingSelectedListener;
import com.example.firdous.tuksup.R;
import com.example.firdous.tuksup.Singletons.SessionSingleton;
import com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters.BuildingAdapter;
import com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters.BuildingSuggestionsAdapter;
import com.example.firdous.tuksup.models.Building;
import com.example.firdous.tuksup.models.Lesson;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddLessonActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public Lesson lesson;
    private FirebaseFirestore firestoreData;
    private OnBuildingSelectedListener buildingListener;
    private List<Building> buildingList;
    //private ActionBar toolbar;
    private ProgressBar progressBar;
    EditText lectureNumber;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);

        Context _self = this;

       // toolbar = getSupportActionBar();
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        //getActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Edit Lesson Details");

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        progressBar.setVisibility(View.INVISIBLE);

        lesson = SessionSingleton.getInstance().getSelectedLesson();

        AutoCompleteTextView venues = findViewById(R.id.venue);

        buildingListener = new OnBuildingSelectedListener() {
            @Override
            public void OnBuildingSelected(Building item) {

                if(item != null){
                    lesson.setBuildingId(item.getId());
                }            }
        };


        firestoreData = FirebaseFirestore.getInstance();
        final CollectionReference buildings = firestoreData.collection("buildings");
        buildingList  = new ArrayList<>();
        buildings.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // List<DocumentSnapshot> l = queryDocumentSnapshots.getDocuments();

//
             //   buildingList = queryDocumentSnapshots.toObjects(Building.class);

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    //System.out.println(document.getId() + " => " + document.getData());
                    Building temp = document.toObject(Building.class);
                    buildingList.add(temp);
                    //Log.d("AddLessonActivity", document.getData().toString());
                }

                BuildingSuggestionsAdapter buildingAdapter = new BuildingSuggestionsAdapter(_self, R.layout.content_building_card, buildingList,
                        buildingListener);

                venues.setAdapter(buildingAdapter);

                if(lesson.getBuildingId() !=null && lesson.getBuildingId().trim() !=""){
                    Building building = buildingList.stream().filter(subj -> lesson.getBuildingId().equals(subj.getId()))
                            .findAny()
                            .orElse(null);
                    if(building != null){
                        int index = buildingList.indexOf(building);
                        venues.setSelection(index);
                    }
                }

            }
        });

        TextView lessonCodeName = findViewById(R.id.lesson_code_name);
        lessonCodeName.setText(lesson.getName());


        Spinner days = findViewById(R.id.weekday);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Weekdays, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        days.setAdapter(adapter);
        days.setSelection(lesson.getWeekDayId());
        days.setOnItemSelectedListener(this);

        Spinner session = findViewById(R.id.session);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.Sessions, android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        session.setAdapter(adapter2);
        session.setSelection(lesson.getSessionId());
        session.setOnItemSelectedListener(this);

        Spinner lectureType = findViewById(R.id.lectureType);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.Lecture_Type, android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lectureType.setAdapter(adapter3);
        lectureType.setSelection(lesson.getLectureTypeId());
        lectureType.setOnItemSelectedListener(this);


        lectureNumber = findViewById(R.id.lectureNumber);
        lectureNumber.setText(Integer.toString(lesson.getLectureNumber()));

        initializeControls();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initializeControls() {

        Button backBtn = findViewById(R.id.action_back_edit_subj);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button saveBtn = findViewById(R.id.action_save_lesson);
        Activity _self = this;

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                lesson.setLectureNumber(Integer.parseInt(lectureNumber.getText().toString()));
                firestoreData.collection("lessons").document(lesson.getId())
                        .set(lesson)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("AddLessonActivity", "DocumentSnapshot successfully written!");
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(_self, "Success", Toast.LENGTH_LONG).show();
                                _self.finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("AddLessonActivity", "Error writing document", e);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(_self, "Failed", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

        Button deleteBtn = findViewById(R.id.action_delete_lesson);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                firestoreData.collection("lessons").document(lesson.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(_self, "Successfully deleted", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);

                                _self.finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(_self, "An error occurred. Please try again later", Toast.LENGTH_LONG).show();
                            }
                        });


            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.weekday:
                lesson.setWeekDayId(position);
                break;
            case R.id.session:
                lesson.setSessionId(position);
                break;
            case  R.id.lectureType:
                lesson.setLectureTypeId(position);
                break;
            case R.id.venue:
                lesson.setBuildingId(buildingList.get(position).getId());
                TextView temp = view.findViewById(R.id.selectedVenue);
                temp.setText(buildingList.get(position).getName());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
