package com.example.insquare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailLoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText et_email, et_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseRef.child("ListDB").child(firebaseUser.getUid()).setValue("다른 사용자 ID");

        et_email = findViewById(R.id.i_email);
        et_pwd = findViewById(R.id.i_password);

        //상태 바 없애기
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //메인 액티비티창으로 넘어가게 설정
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainActivityIntent = new Intent(EmailLoginActivity.this, MainActivity.class);
                EmailLoginActivity.this.startActivity(MainActivityIntent);

            }
        });
        // 전재영 되라
        //RegisterActivity 창으로 넘어가게 설정
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent RegisterActivityIntent = new Intent(EmailLoginActivity.this, RegisterActivity.class);
                EmailLoginActivity.this.startActivity(RegisterActivityIntent);
            }
        });

        //로그인 액티비티
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = et_email.getText().toString();
                String strPwd = et_pwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EmailLoginActivity.this, "환영합니다.", Toast.LENGTH_SHORT).show();
                            Intent BottomActivityIntent = new Intent(EmailLoginActivity.this, List.class);
                            EmailLoginActivity.this.startActivity(BottomActivityIntent);
                            finish();
                        }else {
                            Toast.makeText(EmailLoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}