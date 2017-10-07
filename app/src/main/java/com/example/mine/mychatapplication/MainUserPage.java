package com.example.mine.mychatapplication;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainUserPage extends AppCompatActivity {

    private static final String NATIONAL_ID = "NationalID";
    private static final String SIM_CARD_SERIAL_NUMBER = "SimCardSerialNumber";
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_user_page_activity);
        //Check File Permissions to get the phonenumber;
        //get user id

        SharedPreferences activation_file = getSharedPreferences(getString(R.string.Activation_File), MODE_PRIVATE);

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        final String nationalIdNumber = activation_file.getString(NATIONAL_ID, "");


          //  Log.d("ahmed", "onCreate: "+Integer.MAX_VALUE);
        final String SimCardSerialNumber = tm.getSimSerialNumber();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        //Log.d("ahmed", "onCreate: " + databaseReference.child(getString(R.string.UsersProfile)).child(nationalIdNumber)
        //        .child(SIM_CARD_SERIAL_NUMBER));
        //Log.d("ahmed", "onCreate: " + SimCardSerialNumber);
        databaseReference.child(getString(R.string.UsersProfile)).child(nationalIdNumber)
                .child(SIM_CARD_SERIAL_NUMBER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() || !dataSnapshot.getValue().toString().equals(SimCardSerialNumber)) {

                    //Log.d("ahmed", "onDataChange: "+SimCardSerialNumber);
                    Intent intent = new Intent(getBaseContext(), BlankActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //ToolBar

        //setSupportActionBar(toolbar);
        //Log.d("ahmed", "onCreate: "+nationalIdNumber);
        Intent alarmIntent = new Intent(this, NotificationAlarm.class);
        alarmIntent.putExtra("id",nationalIdNumber);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);



        //TabLayout
        TabLayout tableLayout = (TabLayout) findViewById(R.id.MainActiTab);
        tableLayout.addTab(tableLayout.newTab().setText("All Users"));
        tableLayout.addTab(tableLayout.newTab().setText("Posts"));
        int numberOfTabs = 2;

        //ViewPager
        viewPager = (ViewPager) findViewById(R.id.MainActiPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), numberOfTabs);
        viewPager.setAdapter(viewPagerAdapter);

        //set when change the taps
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
