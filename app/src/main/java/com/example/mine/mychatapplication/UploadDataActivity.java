package com.example.mine.mychatapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class UploadDataActivity extends AppCompatActivity {

    private String path;

    private AlertDialog successDialog;

    private AlertDialog waitDialog;

    private AlertDialog failDialog;

    private AlertDialog.Builder builder;

    private Context context=this;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaddata);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.uploadFileChatApp);



        final ListView uploadListView = (ListView) findViewById(R.id.uploadList);

        UploadListCustomAdapter uploadArrayAdapter = new UploadListCustomAdapter(this);

        uploadListView.setAdapter(uploadArrayAdapter);

        uploadArrayAdapter.notifyDataSetChanged();

        // 1. Instantiate an AlertDialog.Builder with its constructor
        builder= new AlertDialog.Builder(this);

        builder.setMessage(R.string.Wait_ms).setTitle(R.string.Wait);

        waitDialog=builder.create();

        builder.setMessage(R.string.upLoadSucceeded).setTitle(R.string.succeeded);

        successDialog=builder.create();

        builder.setMessage(R.string.upLoadFailed).setTitle(R.string.failed);

        failDialog=builder.create();

        uploadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:

                        new ReadExcel().exe(context,waitDialog,failDialog,successDialog,path + "/Divisions.xlsx", "1");


                        break;
                    case 1:

                        new ReadExcel().exe(context,waitDialog,failDialog,successDialog,path + "/Departments.xlsx", "2");

                        break;
                    case 2:

                        new ReadExcel().exe(context,waitDialog,failDialog,successDialog,path + "/Sections.xlsx", "3");

                        break;
                    case 3:

                        new ReadExcel().exe(context,waitDialog,failDialog,successDialog,path + "/Jobs_Levels.xlsx", "4");

                        break;
                    case 4:

                        new ReadExcel().exe(context,waitDialog,failDialog,successDialog,path + "/Positions.xlsx", "5");

                        break;
                    case 5:

                        new ReadExcel().exe(context,waitDialog,failDialog,successDialog,path + "/Employees.xlsx", "6");

                        break;
                    default:
                }

            }
        });

        checkFilePermissions();
    }


    @TargetApi(23)
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");

            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");

            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1001); //Any number
            }
        } else {
            Log.d("TAG", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }



}
