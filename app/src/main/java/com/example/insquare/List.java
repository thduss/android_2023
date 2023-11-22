package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        //하단바 부분 복붙하셈
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(item.getItemId() == R.id.list_id) {
                    intent = new Intent(List.this, List.class);
                    startActivity(intent);
                    return true;
                }
                else if(item.getItemId() == R.id.QR_id) {
                    intent = new Intent(List.this, QR.class);
                    startActivity(intent);
                    return true;
                }
                else if(item.getItemId() == R.id.map_id) {
                    intent = new Intent(List.this, Map.class);
                    startActivity(intent);
                    return true;
                }
                else if(item.getItemId() == R.id.myProfile_id) {
                    intent = new Intent(List.this, Profile.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

    }
}