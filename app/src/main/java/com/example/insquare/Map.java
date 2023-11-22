package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Map extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //하단바
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(item.getItemId() == R.id.list_id) {
                    intent = new Intent(Map.this, List.class);
                    startActivity(intent);
                    return true;
                }
                else if(item.getItemId() == R.id.QR_id) {
                    intent = new Intent(Map.this, QR.class);
                    startActivity(intent);
                    return true;
                }
                else if(item.getItemId() == R.id.map_id) {
                    intent = new Intent(Map.this, Map.class);
                    startActivity(intent);
                    return true;
                }
                else if(item.getItemId() == R.id.myProfile_id) {
                    intent = new Intent(Map.this, Profile.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}