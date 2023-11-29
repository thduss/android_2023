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

                                                        
        // QR 공유 페이지로 변환
        QR_btn = findViewById(R.id.QR_btn);
        QR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Register registerInfo = dataSnapshot.getValue(Register.class);
                            if (registerInfo != null) {
                                Intent intent = new Intent(nameCardPage.this, GeneratedQRActivity.class);
                                intent.putExtra("QR_DATA", registerInfo.toQRString());
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            } else {
                                Toast.makeText(nameCardPage.this, "사용자 정보가 없습니다", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(nameCardPage.this, "데이터 불러오기에 실패했습니다", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(nameCardPage.this, "로그인 되지 않았습니다", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}