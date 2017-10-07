package com.example.mine.mychatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {

    private ArrayList<Employee> employees=new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;

    String Section;
    private CustomRecyclerViewAdapter customRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_layout);

        //ReadData Part
        Intent intent=getIntent();
        String SectionId=intent.getStringExtra("ID");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("SectionsData").child(SectionId);
        databaseReference2= FirebaseDatabase.getInstance().getReference().child("UsersProfile");
        ReadUser();
        //recyclerView Part
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.AllUserRecyclerView);
        customRecyclerViewAdapter=new CustomRecyclerViewAdapter(AllUsersActivity.this,employees);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(AllUsersActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(customRecyclerViewAdapter);
    }

    private void ReadUser() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //read Data For User
                /////////////////////////////////////////////////////////
                databaseReference2.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Employee tmp=dataSnapshot.getValue(Employee.class);
                        employees.add(tmp);
                        customRecyclerViewAdapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                    /////////////////////////////////////////////////////////
                });
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
