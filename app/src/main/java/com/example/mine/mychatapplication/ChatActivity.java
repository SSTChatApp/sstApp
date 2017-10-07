package com.example.mine.mychatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private String currentUserId;
    private String chatUserId;
    private static final String NATIONAL_ID = "NationalID";
    private static final String CHAT_USER_ID = "UID";
    private static final String CHAT_USER_NAME = "UName";
    private final static String ROOT="CHAT_NOTIFICATIONS";
    private RecyclerView recyclerView;
    private String USERS_PROFILE;
    private String GROUPS;
    private String GROUPS_DATA;
    private DatabaseReference databaseReference;
    private String key;
    private DateFormat dateFormat;
    private final ArrayList<ChatMessage> friendlyMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences activation_file = getSharedPreferences(getString(R.string.Activation_File), MODE_PRIVATE);
        Intent intent = getIntent();
        currentUserId = activation_file.getString(NATIONAL_ID, "");
        chatUserId = intent.getStringExtra(CHAT_USER_ID);
        String chatUserName = intent.getStringExtra(CHAT_USER_NAME);
        USERS_PROFILE = getResources().getString(R.string.UsersProfile);
        GROUPS = getResources().getString(R.string.Groups);
        GROUPS_DATA = getResources().getString(R.string.GroupsData);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");



        key = ((currentUserId.compareTo(chatUserId) > 0) ? chatUserId + currentUserId : currentUserId + chatUserId);

        RealData();
        final EditText mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        final Button mSendButton = (Button) findViewById(R.id.sendButton);


        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessageAdapter = new MessageAdapter(this, friendlyMessages);
        recyclerView.setAdapter(mMessageAdapter);


        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);

                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add message to list and notify the adapter
                ChatMessage message =
                        new ChatMessage(mMessageEditText.getText().toString(),"",dateFormat.format(new Date()), null);
               // friendlyMessages.add(message);
                databaseReference.child(GROUPS_DATA).child(key).push().setValue(message);
                databaseReference.child(USERS_PROFILE).child(chatUserId).child(ROOT).child(currentUserId).setValue(message);
               // mMessageAdapter.notifyDataSetChanged();
                // Clear input box
                mMessageEditText.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void RealData() {


        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2.child(GROUPS_DATA).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    databaseReference.child(USERS_PROFILE)
                            .child(chatUserId).child(GROUPS).child(key).setValue("True");

                    databaseReference.child(USERS_PROFILE)
                            .child(currentUserId).child(GROUPS).child(key).setValue("True");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child(GROUPS_DATA).child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                friendlyMessages.add(dataSnapshot.getValue(ChatMessage.class));
                int pos=friendlyMessages.size() - 1;
                mMessageAdapter.notifyItemInserted(pos);
                recyclerView.smoothScrollToPosition(pos);


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