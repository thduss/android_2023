package com.example.insquare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class nameCardPage extends AppCompatActivity {
    ImageButton return_btn;
    Button edit_btn;
    Button QR_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_card_page);

        // 마이페이지로 뒤로가기 버튼
        return_btn = findViewById(R.id.back_btn);
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        // 명함 뒷 페이지로 변환
        return_btn = findViewById(R.id.return_btn);
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nameCard_backpage.class);
                startActivity(intent);
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

        // QR 페이지로 변환
        QR_btn = findViewById(R.id.namecard_QRcode_btn);
        QR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nameCard_backpage.class);
                startActivity(intent);
            }
        });

    }
}