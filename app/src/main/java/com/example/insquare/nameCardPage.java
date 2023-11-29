package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
    ImageButton QR_btn, BackBtn;

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

        // 뒤로가기
        BackBtn = findViewById(R.id.backBtn);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
                                                       

        // QR 페이지로 변환
        QR_btn = findViewById(R.id.namecard_QRcode_btn);
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