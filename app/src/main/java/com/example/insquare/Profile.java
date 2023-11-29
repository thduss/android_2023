package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {
    ImageButton add_btn;
    ImageButton enter_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 해당 명함 페이지로 들어가는 버튼
        enter_btn = findViewById(R.id.namecard_enter);
        enter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nameCardPage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        // 해당 명함 페이지로 들어가는 버튼
        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nameCard_createpage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        //하단바 부분 복붙하셈
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(item.getItemId() == R.id.list_id) {
                    intent = new Intent(Profile.this, List.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(item.getItemId() == R.id.QR_id) {
                    intent = new Intent(Profile.this, QR.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(item.getItemId() == R.id.map_id) {
                    intent = new Intent(Profile.this, Map.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(item.getItemId() == R.id.myProfile_id) {
                    intent = new Intent(Profile.this, Profile.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
}