package com.example.mine.mychatapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CommentAct extends AppCompatActivity {
    private ArrayList<Comment> comments;
    private ListOfCommentsAdapter listOfCommentsAdapter;
    private DatabaseReference myRef ;
    TextView t;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_page);
        Intent intent=getIntent();
        String Postid=intent.getStringExtra("ID");
        //List View Part
        comments=new ArrayList<>();
        myRef= FirebaseDatabase.getInstance().getReference().child("Comments").child(Postid);
        ReadData();
        ListView listView=(ListView) findViewById(R.id.ListOfComments);
        listOfCommentsAdapter=new ListOfCommentsAdapter(CommentAct.this,comments);
        listView.setAdapter(listOfCommentsAdapter);

        editText=(EditText) findViewById(R.id.editText);
        editText.setText("");
        ImageButton imageButton=(ImageButton) findViewById(R.id.AddCommentButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().isEmpty())
                    AddData();
                else
                    Toast.makeText(CommentAct.this,"Please Type Your Comment First",Toast.LENGTH_SHORT).show();
            }
        });



    }

    //Read data From DataBase
    private void ReadData()
    {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment tmp=dataSnapshot.getValue(Comment.class);
                comments.add(tmp);
                listOfCommentsAdapter.notifyDataSetChanged();
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

    //Add Data Into DataBase
    private void AddData()
    {
        String UserName="Eraky"; //change it later to get Current User
        //Get Time
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
        String CurrentDate=df.format(calendar.getTime());
        String Value=editText.getText().toString();
        editText.setText("");
        editText.clearFocus();
        Comment tmp=new Comment(Value,UserName,CurrentDate);
        myRef.push().setValue(tmp);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }
}
