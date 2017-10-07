package com.example.mine.mychatapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.NOTIFICATION_SERVICE;



public class NotificationAlarm extends BroadcastReceiver {

    private final static String ROOT="CHAT_NOTIFICATIONS";
    private static final String CHAT_USER_ID = "UID";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        //  Log.d("ahmed", "onChildAddednotifi: "+intent.getStringExtra("id"));

        if(intent!=null&&intent.hasExtra("id")){

            final String USERS_PROFILE = context.getResources().getString(R.string.UsersProfile);

            String id = intent.getStringExtra("id");

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child(USERS_PROFILE).child(id).child(ROOT).addChildEventListener(new ChildEventListener() {

                @Override

                public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                    //Log.d("ahmed", "onChildAddednotifi: "+dataSnapshot.getValue().toString());

                    //Log.d("ahmed", "onCreate: " + "done");

                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);

                    String id = dataSnapshot.getKey();

                    NotificationCompat.Builder mBuilder =

                            new NotificationCompat.Builder(context)

                                    .setSmallIcon(R.drawable.ic_launcher)

                                    .setContentTitle(message.writer)

                                    .setContentText(message.body)

                                    .setAutoCancel(true);



                    Intent resultIntent = new Intent(context, ChatActivity.class);

                    resultIntent.putExtra(CHAT_USER_ID, id);

                    PendingIntent resultPendingIntent =

                            PendingIntent.getActivity(

                                    context,

                                    0,

                                    resultIntent,

                                    PendingIntent.FLAG_UPDATE_CURRENT

                            );

                    mBuilder.setContentIntent(resultPendingIntent);





                    // Sets an ID for the notification

                    int mNotificationId = 001;


                    // Gets an instance of the NotificationManager service

                    NotificationManager mNotifyMgr =

                            (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                    // Builds the notification and issues it.

                    mNotifyMgr.notify("ahmed",mNotificationId, mBuilder.build());

                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 500 milliseconds

                    v.vibrate(500);





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
}
