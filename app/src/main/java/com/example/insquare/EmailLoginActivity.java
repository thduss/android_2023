package com.example.insquare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EmailLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        //상태 바 없애기
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //EmailLoginActivity 창으로 넘어가게 설정
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainActivityIntent = new Intent(EmailLoginActivity.this, MainActivity.class);
                EmailLoginActivity.this.startActivity(MainActivityIntent);
            }
        });

        //RegisterActivity 창으로 넘어가게 설정
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterActivityIntent = new Intent(EmailLoginActivity.this, RegisterActivity.class);
                EmailLoginActivity.this.startActivity(RegisterActivityIntent);
            }
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BottomActivityIntent = new Intent(EmailLoginActivity.this, bottom_menuBar.class);
                EmailLoginActivity.this.startActivity(BottomActivityIntent);
            }
        });
    }
}