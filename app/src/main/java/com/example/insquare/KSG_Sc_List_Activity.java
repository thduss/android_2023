package com.example.insquare;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.Manifest;


public class KSG_Sc_List_Activity extends AppCompatActivity {
    ImageView sc_Logo;
    TextView sc_Username,sc_Username2 ,sc_Department, sc_Position, sc_Email, sc_Number, sc_Adress, sc_detail_address;
    String sLogo, sUsername, sDepartment, sPosition, sEmail, sNumber, sAdress, sUid, sCompany, sdetailAddress;


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
        sc_detail_address = findViewById(R.id.sc_tv_back_detail_address);
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
        sdetailAddress = intent.getExtras().getString("detail_address");
        sUid = intent.getExtras().getString("uid");
        sCompany = intent.getExtras().getString("company");

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
        sc_detail_address.setText(sdetailAddress);

        ImageButton backBt = findViewById(R.id.sc_bt_back);

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton saveNumBtn = findViewById(R.id.saveNumBtn);
        saveNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 권한을 확인하고 없다면 요청하는 코드
                String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};

                if (ContextCompat.checkSelfPermission(KSG_Sc_List_Activity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(KSG_Sc_List_Activity.this, Manifest.permission.WRITE_CONTACTS)
                                != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 허용되지 않았을 경우
                    // 사용자에게 권한 요청 다이얼로그 표시
                    ActivityCompat.requestPermissions(KSG_Sc_List_Activity.this, permissions, 1);
                } else {
                    // 이미 권한이 허용되어 있을 경우 주소록 액세스 코드 실행
                    // 예: 주소록 선택 Intent 시작 코드
                    new ContactAddTask().execute();
                }

            }
        });
    }


    private class ContactAddTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<ContentProviderOperation> list = new ArrayList<>();
            try {
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
                                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                .build()
                );

                list.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Email.DATA, sCompany)
                                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                                .build()
                );

                getApplicationContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, list); //주소록추가
                list.clear();
                return true;
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
                return false;
            }
        }

        // hi

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // 연락처 추가 성공
                Toast.makeText(KSG_Sc_List_Activity.this, "연락처가 추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                // 연락처 추가 실패
                Toast.makeText(KSG_Sc_List_Activity.this, "연락처 추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            // 작업이 완료된 후에 수행할 작업이 있다면 여기에 추가
        }
    }
}

