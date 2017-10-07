package com.example.mine.mychatapplication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AllSectonsFragment extends Fragment {

    private ArrayList<Section> sections;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private AllSectionsAdapter allSectionsAdapter;
    private String userProfileID;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Uid";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Uid Parameter 1.
     * @return A new instance of fragment AllSectonsFragment.
     */
    public static AllSectonsFragment newInstance(String Uid) {
        AllSectonsFragment fragment = new AllSectonsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, Uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String userID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.all_sections, container, false);
        sections=new ArrayList<>();
        //connecting to database
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Sections");
        int x = 5;
        RealData();

        //recyclerView part
        RecyclerView recyclerView = view.findViewById(R.id.AllSectoinsRecyclerView);
        allSectionsAdapter=new AllSectionsAdapter(getContext(),sections);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(allSectionsAdapter);


        return view;
    }

    private void RealData() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Section tmp=dataSnapshot.getValue(Section.class);
                tmp.ID=dataSnapshot.getKey();
                sections.add(tmp);
                allSectionsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
