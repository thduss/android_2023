package com.example.insquare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class nameCard_editpage extends AppCompatActivity {

    ImageButton back_btn;
    Button add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_card_editpage);

        // 명함 앞 페이지로 변환
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nameCardPage.class);
                startActivity(intent);
            }
        });

        // 명함 추가 버튼 -> 일단 내 명함 리스트로 가도록 구현했는데 디비에 저장되도록 다시 구현 필요
        add_btn = findViewById(R.id.namecard_add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });
    }
}