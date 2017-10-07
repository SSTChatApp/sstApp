package com.example.mine.mychatapplication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;


public class AllUserChatFragmet extends Fragment {

    private ArrayList<Employee> employees=new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private CustomRecyclerViewAdapter customRecyclerViewAdapter;
    private final HashMap<String,String>Deps=new HashMap<>();
    private final HashMap<String,String>Secs=new HashMap<>();


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Uid";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Uid Parameter 1.
     * @return A new instance of fragment AllUserChatFragmet.
     */
    public static AllUserChatFragmet newInstance(String Uid) {
        AllUserChatFragmet fragment = new AllUserChatFragmet();
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
        View view=inflater.inflate(R.layout.fragment_all_user_chat_fragmet, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.AllUserRecyclerView);
        //connecting to database
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference2=FirebaseDatabase.getInstance().getReference();
        customRecyclerViewAdapter=new CustomRecyclerViewAdapter(getContext(),employees);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(customRecyclerViewAdapter);
        RealData();


        return view;
    }

    private void RealData() {

        final String notfound=getString(R.string.notFound);
        final String UsersProfile=getString(R.string.UsersProfile);
        final String Departments=getString(R.string.Departments);
        final String Sections=getString(R.string.Sections);

        databaseReference2
                .child(Departments)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Department d;
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            d=ds.getValue(Department.class);
                            Deps.put(d.departmentCode,d.name);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        databaseReference2
                .child(Sections)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Section s;
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            s=ds.getValue(Section.class);
                            Secs.put(s.sectionCode,s.name);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        databaseReference.child(UsersProfile).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    final Employee emp = ds.getValue(Employee.class);
                    if (!(emp.departmentCode.isEmpty())) {

                        emp.departmentName = Deps.get(emp.departmentCode);
                    } else {
                        emp.departmentName = notfound;
                    }
                    if (!(emp.sectionCode.isEmpty())) {

                        emp.sectionName = Secs.get(emp.sectionCode);

                    } else {
                        emp.sectionName = notfound;
                    }
                    employees.add(emp);
                    customRecyclerViewAdapter.notifyItemInserted(employees.size() - 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //report error
                Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }
}
