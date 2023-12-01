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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_card_page);
        Intent intent = getIntent();
        String currentCardUID = intent.getStringExtra("selectedCardUID"); // 인텐트에서 고유 UID 받기


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
        sLogo = intent.getExtras().getString("logo");
        sName = intent.getExtras().getString("username");
        sCompany = intent.getExtras().getString("company");
        sDepartment = intent.getExtras().getString("department");
        sRank = intent.getExtras().getString("rank");
        sEmail = intent.getExtras().getString("email");
        sNumber = intent.getExtras().getString("number");
        sAddress = intent.getExtras().getString("address");
        sDetail_address = intent.getExtras().getString("detail_address");

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
        // QR 페이지로 변환
        myRegister cardInfo = (myRegister) intent.getSerializableExtra("cardInfo");

        // QR 버튼 설정 및 클릭 이벤트 처리
        QR_btn = findViewById(R.id.QR_btn);
        QR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardInfo != null) {
                    String qrData = cardInfo.getM_name() + ", " +
                            cardInfo.getM_company() + ", " +
                            cardInfo.getM_department() + ", " +
                            cardInfo.getM_rank() + ", " +
                            cardInfo.getM_address() + ", " +
                            cardInfo.getM_email() + ", " +
                            cardInfo.getM_number() + ", " +
                            cardInfo.getM_logo();

                    Intent qrIntent = new Intent(nameCardPage.this, GeneratedQRActivity.class);
                    qrIntent.putExtra("QR_DATA", qrData);
                    startActivity(qrIntent);
                } else {
                    Toast.makeText(nameCardPage.this, "명함 정보가 없습니다", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}