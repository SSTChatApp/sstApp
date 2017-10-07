package com.example.mine.mychatapplication;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;


public class CreateFilesActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createfiles);



        SharedPreferences activation_file = getSharedPreferences(getString(R.string.Activation_File), MODE_PRIVATE);


        if (activation_file.getString("FirstRun", "True").equals("True")) {

            checkFilePermissions();

            activation_file.edit().putString("FirstRun", "False").apply();


        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1001&&grantResults[0]==PERMISSION_GRANTED&&grantResults[1]==PERMISSION_GRANTED){


            WriteExcel we = new WriteExcel();

            File f = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.uploadFileChatApp));

            we.setOutputFile(f);
            we.getContext(this);
            we.execute();


        }

    }


}
