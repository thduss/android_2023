package com.example.insquare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //상태 바 없애기
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //EmailLoginActivity 창으로 넘어 가게 설정
        Button emailButton = findViewById(R.id.start_to_email);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EmailLoginIntent = new Intent(MainActivity.this, EmailLoginActivity.class);
                MainActivity.this.startActivity(EmailLoginIntent);
            }
        });
    }
}