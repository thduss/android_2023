package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wajahatkarim3.easyflipview.EasyFlipView;
import com.google.firebase.firestore.auth.User;

public class nameCardPage extends AppCompatActivity {
    ImageButton QR_btn, BackBtn, Fix_btn;
    ImageView Logo;
    TextView Department, Name, Name2, Rank, Address, Email, Number, Detail_address;
    String sLogo, sDepartment, sName, sRank, sAddress, sEmail, sNumber, sDetail_address, sCompany, sIndex;

    //전재영 추가

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 authentication 연동
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myDatabaseReference = database.getReference();

    // 전재영 추가 끝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_card_page);
        Intent intent = getIntent();

        //전재영 추가
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 로그인한 계정 객체화
        //전재영 추가 끝


        // cardInfo(class타입 객체)로 data를 넘김
        List_User cardInfo = (List_User) intent.getSerializableExtra("cardInfo");


        final EasyFlipView easyFlipView = (EasyFlipView) findViewById(R.id.flipview);
        easyFlipView.setFlipDuration(400);
        easyFlipView.setFlipEnabled(true);

        // 페이지 앞/뒤 전환
        findViewById(R.id.frontPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
            }
        });
        findViewById(R.id.backPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
            }
        });

        //DB에서 가져와 띄워주기
        Logo = findViewById(R.id.logo);
        Department = findViewById(R.id.nc_department);
        Name = findViewById(R.id.nc_name);
        Name2 = findViewById(R.id.name2);
        Rank = findViewById(R.id.nc_rank);
        Address = findViewById(R.id.nc_address);
        Detail_address = findViewById(R.id.nc_detail_address);
        Email = findViewById(R.id.nc_email);
        Number = findViewById(R.id.nc_number);

        sIndex = intent.getExtras().getString("index");
        sLogo = cardInfo.getP_logo();
        sName = cardInfo.getP_name();
        sCompany = cardInfo.getP_company();
        sDepartment = cardInfo.getP_department();
        sRank = cardInfo.getP_position();
        sEmail = cardInfo.getP_email();
        sNumber = cardInfo.getP_number();
        sAddress = cardInfo.getP_address();
        sDetail_address = cardInfo.getP_detail_address();

        Glide.with(this)
                .load(sLogo)
                .into(this.Logo);
        Name.setText(sName);
        Name2.setText(sName);
        Department.setText(sDepartment);
        Rank.setText(sRank);
        Address.setText(sAddress);
        Detail_address.setText(sDetail_address);
        Email.setText(sEmail);
        Number.setText(sNumber);
        Log.e("아이템의 num 값", String.valueOf(sIndex));

        // 뒤로가기
        BackBtn = findViewById(R.id.backBtn);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
                                                       
        //수정 페이지로 변환
        Fix_btn = findViewById(R.id.fix_btn);
        Fix_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nameCard_editpage.class);

                intent.putExtra("index", sIndex);
                intent.putExtra("logo", sLogo);
                intent.putExtra("username", sName);
                intent.putExtra("company", sCompany);
                intent.putExtra("department", sDepartment);
                intent.putExtra("rank", sRank);
                intent.putExtra("email", sEmail);
                intent.putExtra("address", sAddress);
                intent.putExtra("detail_address", sDetail_address);
                intent.putExtra("number", sNumber);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        // QR 버튼 설정 및 클릭 이벤트 처리
        QR_btn = findViewById(R.id.QR_btn);
        QR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardInfo != null) {
                    String qrUid =
                            (firebaseUser.getUid().toString()).concat(intent.getExtras().getString("index"));
                    // 객체화한 계정의 고유값을 myIdCode로 받기

                    Intent qrIntent = new Intent(nameCardPage.this, GeneratedQRActivity.class);
                    qrIntent.putExtra("QR_DATA", qrUid);
                    startActivity(qrIntent);
                } else {
                    Toast.makeText(nameCardPage.this, "명함 정보가 없습니다", Toast.LENGTH_LONG).show();
                }
            }
        });
    }





}