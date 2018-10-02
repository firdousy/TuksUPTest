package com.example.firdous.tuksup.SubjectBuilder;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.firdous.tuksup.LoginActivity;
import com.example.firdous.tuksup.R;
import com.example.firdous.tuksup.Singletons.SessionSingleton;
import com.example.firdous.tuksup.SubjectBuilder.dummy.DummyContent;
import com.example.firdous.tuksup.models.Building;
import com.example.firdous.tuksup.models.Lesson;
import com.example.firdous.tuksup.models.Subject;
import com.google.android.gms.tasks.OnSuccessListener;
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

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SubjectFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FirebaseFirestore firestoreData;

    private List<Subject> subjectList;
    private MySubjectRecyclerViewAdapter  adapter;
    ListenerRegistration registration;
    RecyclerView recyclerView;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SubjectFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SubjectFragment newInstance(int columnCount) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
        }

        Context context = view.getContext();

        if(registration == null){
            attachFireStoreRegistration();
        }
//        subjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//
//                    Subject temp = document.toObject(Subject.class);
//                    subjectList.add(temp);
//                }
//
//                adapter = new MySubjectRecyclerViewAdapter(subjectList, mListener);
//
//                if (mColumnCount <= 1) {
//                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                } else {
//                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//                }
//                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                recyclerView.setAdapter(adapter);
//
//
//            }
//        });

        FloatingActionButton addSubjectBtn = view.findViewById(R.id.action_add_subject);


        addSubjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.action_add_subject:
                        //AddSubject();

                        Context _self = getContext();
                        CollectionReference collection = firestoreData.collection("subjects");
                        Subject newSubject = new Subject("", "new subject", SessionSingleton.getInstance().getUserId(), "Mr X", new ArrayList<>(), "DMY" );
                        collection.add(newSubject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(_self,"New Subject Created", Toast.LENGTH_SHORT).show();

                                String generatedId = documentReference.getId();

                                documentReference.update("id", generatedId);

                                newSubject.setId(generatedId);

//                                subjectList.add(newSubject);
//
//                                adapter = new MySubjectRecyclerViewAdapter(subjectList, mListener);
//                                recyclerView.setAdapter(adapter);
//                                recyclerView.smoothScrollToPosition(subjectList.indexOf(newSubject));


                            }
                        });

                        break;
                }
            }
        });

//        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
//        itemAnimator.setAddDuration(1000);
//        itemAnimator.setRemoveDuration(1000);
//        recyclerView.setItemAnimator(itemAnimator);

        return view;
    }

    private void attachFireStoreRegistration() {


        subjectList = new ArrayList<>();

        firestoreData = FirebaseFirestore.getInstance();
        Query subjects = firestoreData
                .collection("subjects")
                .whereEqualTo("userId", SessionSingleton.getInstance().getUserId());


        registration = subjects.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("SubjectFragment", "Listen failed.", e);
                    return;
                }

                Subject sub = new Subject();
                Subject scrollTo = null;

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                    DocumentSnapshot document =  dc.getDocument();
                    Subject temp = document.toObject(Subject.class);
                    sub = null;
                    switch (dc.getType()) {
                        case ADDED:
                            //Log.d(TAG, "New city: " + dc.getDocument().getData());
                            subjectList.add(temp);
                            scrollTo = temp;
                            break;
                        case MODIFIED:
                            //Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                            sub = subjectList.stream().filter(subj -> temp.getId().equals(subj.getId()))
                                    .findAny()
                                    .orElse(null);
                            if(sub != null){
                                int index = subjectList.indexOf(sub);
                                subjectList.set(index, temp);
                            }

                            break;
                        case REMOVED:
                            //Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                            sub = subjectList.stream().filter(subj -> temp.getId().equals(subj.getId()))
                                    .findAny()
                                    .orElse(null);
                            if(sub != null){
                                subjectList.remove(sub);
                            }
                            break;
                    }
                }

                adapter = new MySubjectRecyclerViewAdapter(subjectList, mListener);

                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                if(scrollTo != null)
                    recyclerView.smoothScrollToPosition(subjectList.indexOf(scrollTo));

//                recyclerView.smoothScrollToPosition(subjectList.indexOf(sub));
            }
        });

    }

    private void AddSubject() {

        SessionSingleton.getInstance().setSelectedSubject(new Subject());
        startActivity(new Intent(getActivity(),AddSubjectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

//        if(registration == null)
//        {
//            attachFireStoreRegistration();
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registration.remove();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Subject item);
    }
}
