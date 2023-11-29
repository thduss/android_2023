package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    TextView Department, Name, Name2, Rank, Address, Email, Number;
    String sLogo, sDepartment, sName, sRank, sAddress, sEmail, sNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_card_page);


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
        Email = findViewById(R.id.nc_email);
        Number = findViewById(R.id.nc_number);

        Intent intent = getIntent();

        sLogo = intent.getExtras().getString("logo");
        sName = intent.getExtras().getString("username");
        sDepartment = intent.getExtras().getString("department");
        sRank = intent.getExtras().getString("position");
        sEmail = intent.getExtras().getString("email");
        sNumber = intent.getExtras().getString("number");
        sAddress = intent.getExtras().getString("adress");

        Glide.with(this)
                .load(sLogo)
                .into(this.Logo);
        Name.setText(sName);
        Name2.setText(sName);
        Department.setText(sDepartment);
        Rank.setText(sRank);
        Address.setText(sAddress);
        Email.setText(sEmail);
        Number.setText(sNumber);

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
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        // QR 페이지로 변환
        QR_btn = findViewById(R.id.QR_btn);
        QR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    String loggedInUserId = firebaseUser.getUid();

                    // MyListDB에서 현재 로그인된 사용자의 명함 정보 가져오기
                    DatabaseReference myListDBRef = FirebaseDatabase.getInstance().getReference("MyListDB").child(loggedInUserId);
                    myListDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot cardSnapshot : dataSnapshot.getChildren()) {
                                    // 현재 화면에 띄워진 명함의 고유 UID (p_uid)
                                    String currentCardUID = cardSnapshot.child("p_uid").getValue(String.class);

                                    // UserDB에서 해당 명함의 정보 가져오기
                                    DatabaseReference userDBRef = FirebaseDatabase.getInstance().getReference("UserDB").child(currentCardUID);
                                    userDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                            if (userSnapshot.exists()) {
                                                Register cardInfo = userSnapshot.getValue(Register.class);

                                                if (cardInfo != null) {
                                                    // 명함 정보를 QR 코드 데이터로 변환
                                                    String qrData = cardInfo.toQRString();

                                                    // QR 코드 데이터를 GeneratedQRActivity로 전달
                                                    Intent intent = new Intent(nameCardPage.this, GeneratedQRActivity.class);
                                                    intent.putExtra("QR_DATA", qrData);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(nameCardPage.this, "명함 정보가 없습니다", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(nameCardPage.this, "데이터 불러오기에 실패했습니다", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(nameCardPage.this, "MyListDB에서 데이터 불러오기에 실패했습니다", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(nameCardPage.this, "로그인 되지 않았습니다", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}