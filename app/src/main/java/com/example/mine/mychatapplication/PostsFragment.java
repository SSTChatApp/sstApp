package com.example.mine.mychatapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PostsFragment extends Fragment {


    ArrayList<Comment> comments;

    // for Post Adapter
    private ArrayList<Post> posts;
    private ArrayList<String> PostId;
    private DatabaseReference ReferenceForPosts;
    private EditText editText;
    private Button PostButton;
    private PostAdapter adapter;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Uid";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Uid Parameter 1.
     * @return A new instance of fragment AllSectionsFragment.
     */
    public static PostsFragment newInstance(String Uid) {
        PostsFragment fragment = new PostsFragment();
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
        View view= inflater.inflate(R.layout.fragment_posts, container, false);
         editText =  view.findViewById(R.id.PostEditText);
        PostButton =  view.findViewById(R.id.PostButton);

        //make recycle and Set layout manager to position the items
        // Initialize empty array of messages
        // Create adapter passing in the sample user data
        // Attach the adapter to the recyclerView to populate items
        RecyclerView recyclerViewContacts =  view.findViewById(R.id.RecyclerViewContacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        posts= new ArrayList<>() ;
        PostId=new ArrayList<>();
        ReferenceForPosts = FirebaseDatabase.getInstance().getReference().child("Posts");
        ReadDataForPosts();
        adapter = new PostAdapter(getContext(), posts);
        recyclerViewContacts.setAdapter(adapter);

        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDataForPosts();
            }
        });


        return view;
    }

    // read All Posts call ones
    private void ReadDataForPosts()
    {
        ReferenceForPosts.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post tmp=dataSnapshot.getValue(Post.class);
                tmp.id=dataSnapshot.getKey();
                posts.add(tmp);
                adapter.notifyDataSetChanged();

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
    private void AddDataForPosts()
    {
        String UserName="Saher"; //change it later to get Current User

        //Get Time
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
        String CurrentDate=df.format(calendar.getTime());

        //change it to the editText You Want
        String Value=editText.getText().toString();
        editText.setText("");
        editText.clearFocus();

        // Create Post And Add it to Database
        Post tmp=new Post(UserName,CurrentDate,Value);
        ReferenceForPosts.push().setValue(tmp);
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


}
