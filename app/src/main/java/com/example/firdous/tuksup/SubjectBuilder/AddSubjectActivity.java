package com.example.firdous.tuksup.SubjectBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firdous.tuksup.Listeners.OnLessonSelectedListener;
import com.example.firdous.tuksup.R;
import com.example.firdous.tuksup.Singletons.SessionSingleton;
import com.example.firdous.tuksup.SubjectBuilder.RecyclerAdapters.MyLessonRecyclerViewAdapter;
import com.example.firdous.tuksup.models.Lesson;
import com.example.firdous.tuksup.models.Subject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AddSubjectActivity extends AppCompatActivity {

    private OnLessonSelectedListener mListener;
    private FirebaseFirestore firestoreData;
    private Subject selectedSubject;
    EditText NameField;
    EditText SubjectCode;
    EditText Lecturer;
    RecyclerView recyclerView;
    List<Lesson> lessonsList;
    ListenerRegistration registration;
    MyLessonRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        final Context _self = this;
        mListener = new OnLessonSelectedListener(){
            @Override
            public void OnLessonSelected(Lesson item) {

                AddLesson(item);
            }
        };

        toolbar = getSupportActionBar();
        toolbar.setTitle("Edit Subject Details");

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        progressBar.setVisibility(View.INVISIBLE);

        firestoreData = FirebaseFirestore.getInstance();
        selectedSubject = SessionSingleton.getInstance().getSelectedSubject();

        NameField = findViewById(R.id.subjectName);

        NameField.setText(selectedSubject.getName());

        SubjectCode = findViewById(R.id.subjectCode);

        SubjectCode.setText(selectedSubject.getCode());

        Lecturer = findViewById(R.id.subjectLecturer);
        Lecturer.setText(selectedSubject.getLecturer());

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lessonsList = new ArrayList<>();

        Query lessons = firestoreData
                .collection("lessons")
                .whereEqualTo("subjectId", selectedSubject.getId());

        registration = lessons.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("AddSubjectActivity", "Listen failed.", e);
                    return;
                }

                Lesson les;
                Lesson scrollTo = null;
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    DocumentSnapshot document =  dc.getDocument();
                    Lesson temp = document.toObject(Lesson.class);
                    les = null;
                    switch (dc.getType()) {
                        case ADDED:
                            //Log.d(TAG, "New city: " + dc.getDocument().getData());
                            lessonsList.add(temp);
                            scrollTo = temp;
                            break;
                        case MODIFIED:
                            //Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                            les = lessonsList.stream().filter(subj -> temp.getId().equals(subj.getId()))
                                    .findAny()
                                    .orElse(null);
                            if(les != null){
                                int index = lessonsList.indexOf(les);
                                lessonsList.set(index, temp);
                            }

                            break;
                        case REMOVED:
                            //Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                            les = lessonsList.stream().filter(subj -> temp.getId().equals(subj.getId()))
                                    .findAny()
                                    .orElse(null);
                            if(les != null){
                                lessonsList.remove(les);
                            }
                            break;
                    }
                }

                adapter = new MyLessonRecyclerViewAdapter(getApplicationContext() , lessonsList, mListener);

                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                if(scrollTo != null)
                    recyclerView.smoothScrollToPosition(lessonsList.indexOf(scrollTo));
            }
        });

//        lessons.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//
//                    Lesson temp = document.toObject(Lesson.class);
//                    lessonsList.add(temp);
//                }
//
//                adapter = new MyLessonRecyclerViewAdapter(getApplicationContext(), lessonsList, mListener);
//
//
//                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                recyclerView.setAdapter(adapter);
//
//
//            }
//        });


        initializeControls();

        Button actionAddLesson = findViewById(R.id.action_add_lesson);

        actionAddLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.action_add_lesson:

                        CollectionReference collection = firestoreData.collection("lessons");
                        Lesson newLesson = new Lesson("", selectedSubject.getCode(), 0,"",0,1,selectedSubject.getId(), SessionSingleton.getInstance().getUserId(),0);
                        collection.add(newLesson).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(_self,"New Lesson Created", Toast.LENGTH_SHORT).show();

                                String generatedId = documentReference.getId();

                                documentReference.update("id", generatedId);

                                newLesson.setId(generatedId);

//                                lessonsList.add(newLesson);
//
//                                adapter = new MyLessonRecyclerViewAdapter(_self,lessonsList, mListener);
//                                recyclerView.setAdapter(adapter);
//                                recyclerView.smoothScrollToPosition(lessonsList.indexOf(newLesson));


                            }
                        });
                        //AddLesson(new Lesson());
                }
            }
        });

    }

    private void initializeControls() {

       Button backBtn = findViewById(R.id.action_back_subjects);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button saveBtn = findViewById(R.id.action_save_subjects);
        Activity _self = this;

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                selectedSubject.setName(NameField.getText().toString());
                selectedSubject.setCode(SubjectCode.getText().toString());
                selectedSubject.setLecturer(Lecturer.getText().toString());
                firestoreData.collection("subjects").document(selectedSubject.getId())
                        .set(selectedSubject)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("AddSubjectActivity", "DocumentSnapshot successfully written!");
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(_self, "Success", Toast.LENGTH_LONG).show();
                                _self.finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("AddSubjectActivity", "Error writing document", e);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(_self, "Failed", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

        Button deleteBtn = findViewById(R.id.action_delete_subject);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Query lessons = firestoreData
                        .collection("lessons")
                        .whereEqualTo("subjectId", selectedSubject.getId());

                lessons.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            document.getReference().delete();
                        }

                        firestoreData.collection("subjects").document(selectedSubject.getId())
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

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(_self, "An error occurred. Please try again later", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }


    private void AddLesson(Lesson lesson) {
        SessionSingleton.getInstance().setSelectedLesson(lesson);
        startActivity(new Intent(this ,AddLessonActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

//    public class OnLessonSelectedListener {
//        // TODO: Update argument type and name
//        public void OnLessonSelected(Lesson item){
//
//        };
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registration.remove();

    }
}


