package com.example.mine.mychatapplication;




import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class MainUserPage extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_user_page_activity);
        //Check File Permissions to get the phonenumber;
        //get user id








        //ToolBar

        /*
        Intent alarmIntent = new Intent(this, NotificationAlarm.class);
        alarmIntent.putExtra("id",nationalIdNumber);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        */


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
