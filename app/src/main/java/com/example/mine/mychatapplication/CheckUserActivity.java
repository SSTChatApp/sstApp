package com.example.mine.mychatapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;


public class CheckUserActivity extends AppCompatActivity {

    private EditText nationalId;
    private Button activate;
    private Toast toast=null;
    private static final String SIM_CARD_SERIAL_NUMBER = "SimCardSerialNumber";
    private static final String NATIONAL_ID = "NationalID";
    private static final String FIRST_USE = "FirstUse";
    private SharedPreferences activation_file;
    private TelephonyManager tm;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check_user);
        nationalId = (EditText) findViewById(R.id.NationalIdEditText);
        activate = (Button) findViewById(R.id.ActivateButton);
        activation_file = getSharedPreferences(getString(R.string.Activation_File), MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        nationalId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().trim().length() > 0) {

                    activate.setEnabled(true);
                    activate.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_RED));

                } else {

                    activate.setEnabled(false);
                    activate.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_GRAY));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (activation_file.getString(FIRST_USE, "True").equals("True")) {
           // Log.d("ahmed", "onCreate: Activationfragment   :aaa");

            builder.setMessage("You Must Allow the permission to continue with Activation  or the App will Stop Immediately");
            builder.setTitle("Warning");
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.alertDialogButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    checkFilePermissions();
                    dialogInterface.dismiss();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

        } else {

            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            final String nationalIdNumber = activation_file.getString(NATIONAL_ID, "");

            final String SimCardSerialNumber = tm.getSimSerialNumber();


            checkUser(SimCardSerialNumber,nationalIdNumber);






        }


    }


    public void setActivation(View view) {

        final String simCardSerialNumber = tm.getSimSerialNumber();
        final String nationalIdNumber = nationalId.getText().toString().trim();

        if(!simCardSerialNumber.trim().isEmpty()){


            databaseReference.child(getString(R.string.UsersProfile))
                    .child(nationalIdNumber)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {

                        //toast this id is wrong
                        if(toast!=null)
                            toast.cancel();
                        toast=Toast.makeText(getApplicationContext(),"The ID you entered is wrong", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        Log.d("ahmed", "onDataChange: "+!dataSnapshot.exists());
                        saveLoginData(simCardSerialNumber,nationalIdNumber);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }
        else{
            //toast there is no sim card to use
            if(toast!=null)
                toast.cancel();
            toast=Toast.makeText(getApplicationContext(),"There is no SIM card in This Phone to read", Toast.LENGTH_LONG);
            toast.show();
        }



    }


    @TargetApi(23)
    private void checkFilePermissions() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_PHONE_STATE");

            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{"android.permission.READ_PHONE_STATE"}, 1001); //Any number
            }
        } else {

            Log.d("TAG", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001 && grantResults[0] == PERMISSION_GRANTED) {

            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        }
        else{
            finish();
        }


    }
    private void saveLoginData(final String simCardSerialNumber,final String nationalIdNumber){



        databaseReference.child(getString(R.string.UsersProfile)).child(nationalIdNumber)
                .child(SIM_CARD_SERIAL_NUMBER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    databaseReference.child(getString(R.string.UsersProfile)).child(nationalIdNumber)
                            .child(SIM_CARD_SERIAL_NUMBER).setValue(simCardSerialNumber);


                }


                activation_file.edit().putString(NATIONAL_ID, nationalIdNumber).apply();

                activation_file.edit().putString(FIRST_USE, "Flase").apply();

               checkUser(simCardSerialNumber,nationalIdNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private  void checkUser(final String SimCardSerialNumber,final String nationalIdNumber){

        databaseReference.child(getString(R.string.UsersProfile)).child(nationalIdNumber)
                .child(SIM_CARD_SERIAL_NUMBER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent;
                if (!dataSnapshot.exists() || !dataSnapshot.getValue().toString().equals(SimCardSerialNumber)) {


                    if(toast!=null)
                        toast.cancel();
                    toast=Toast.makeText(getApplicationContext(), "Sim Card Has Changed", Toast.LENGTH_LONG);
                    toast.show();



                }
                else{

                    intent = new Intent(getBaseContext(), MainUserPage.class);
                    startActivity(intent);
                    finish();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

}

