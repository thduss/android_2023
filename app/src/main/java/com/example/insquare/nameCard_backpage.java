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

    public class nameCard_backpage extends AppCompatActivity {
        ImageButton return_btn;
        Button edit_btn, QR_btn;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseDatabase database;
        private DatabaseReference myDatabaseReference;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_name_card_backpage);

            // 명함 앞 페이지로 변환
            return_btn = findViewById(R.id.return_btn);
            return_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), nameCardPage.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });

            // 수정 페이지로 변환
            edit_btn = findViewById(R.id.namecard_edit_btn);
            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), nameCard_editpage.class);
                    startActivity(intent);
                }
            });

            BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
            bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    if(item.getItemId() == R.id.list_id) {
                        intent = new Intent(nameCard_backpage.this, List.class);
                        startActivity(intent);
                        return true;
                    }
                    else if(item.getItemId() == R.id.QR_id) {
                        intent = new Intent(nameCard_backpage.this, QR.class);
                        startActivity(intent);
                        return true;
                    }
                    else if(item.getItemId() == R.id.map_id) {
                        intent = new Intent(nameCard_backpage.this, Map.class);
                        startActivity(intent);
                        return true;
                    }
                    else if(item.getItemId() == R.id.myProfile_id) {
                        intent = new Intent(nameCard_backpage.this, Profile.class);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
            });

            // QR 페이지로 전환
            QR_btn = findViewById(R.id.namecard_QRcode_btn);
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
                                    Intent intent = new Intent(nameCard_backpage.this, GeneratedQRActivity.class);
                                    intent.putExtra("QR_DATA", registerInfo.toQRString());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(nameCard_backpage.this, "사용자 정보가 없습니다", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(nameCard_backpage.this, "데이터 불러오기에 실패했습니다", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(nameCard_backpage.this, "로그인 되지 않았습니다", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }