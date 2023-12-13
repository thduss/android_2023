package com.example.insquare;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;


public class KSG_Sc_List_Activity extends AppCompatActivity {
    ImageView sc_Logo;
    TextView sc_Username,sc_Username2 ,sc_Department, sc_Position, sc_Email, sc_Number, sc_Adress;
    String sLogo, sUsername, sDepartment, sPosition, sEmail, sNumber, sAdress, sUid;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sc_page);

        final EasyFlipView easyFlipView = (EasyFlipView) findViewById(R.id.flipview);
        easyFlipView.setFlipDuration(400);
        easyFlipView.setFlipEnabled(true);

        findViewById(R.id.frontcardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
            }
        });
        findViewById(R.id.backcardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
            }
        });

        sc_Logo = findViewById(R.id.sc_tv_logo);
        sc_Username = findViewById(R.id.sc_tv_name);
        sc_Username2 = findViewById(R.id.sc_tv_back_name);
        sc_Department = findViewById(R.id.sc_tv_name2);
        sc_Position = findViewById(R.id.sc_tv_name3);
        sc_Adress = findViewById(R.id.sc_tv_back_adress);
        sc_Email = findViewById(R.id.sc_tv_back_email);
        sc_Number = findViewById(R.id.sc_tv_back_number);

        Intent intent = getIntent();

        sLogo = intent.getExtras().getString("logo");
        sUsername = intent.getExtras().getString("username");
        sDepartment = intent.getExtras().getString("department");
        sPosition = intent.getExtras().getString("position");
        sEmail = intent.getExtras().getString("email");
        sNumber = intent.getExtras().getString("number");
        sAdress = intent.getExtras().getString("adress");
        sUid = intent.getExtras().getString("uid");

        Glide.with(this)
                .load(sLogo)
                .into(this.sc_Logo);
        sc_Username.setText(sUsername);
        sc_Username2.setText(sUsername);
        sc_Department.setText(sDepartment);
        sc_Position.setText(sPosition);
        sc_Adress.setText(sAdress);
        sc_Email.setText(sEmail);
        sc_Number.setText(sNumber);

        ImageButton backBt = findViewById(R.id.sc_bt_back);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageButton saveNumBtn = findViewById(R.id.saveNumBtn);
        saveNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            new Thread(){
                @Override
                public void run() {

                    ArrayList<ContentProviderOperation> list = new ArrayList<>();
                    try{
                        list.add(
                                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                        .build()
                        );

                        list.add(
                                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, sUsername)

                                        .build()
                        );

                        list.add(
                                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, sNumber)
                                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE , ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)

                                        .build()
                        );

                        list.add(
                                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, sEmail)
                                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE , ContactsContract.CommonDataKinds.Email.TYPE_WORK)

                                        .build()
                        );

                        getApplicationContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, list);
                        list.clear();
                    } catch(RemoteException e){
                        e.printStackTrace();
                    }catch(OperationApplicationException e){

                    }
                }
            }.start();
        }
        }
    }
