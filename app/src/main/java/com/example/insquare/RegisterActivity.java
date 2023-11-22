package com.example.insquare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText et_email, et_password, et_name, et_phonenum;
    RadioGroup genderGroup;
    Button registerButton;
    String userGender;

    //파이어 베이스 주석
    //제발 되라 - 윤재영
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //상태 바 없애기
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //EmailLoginActivity창으로 넘어가게 설정
        Button backButton2 = findViewById(R.id.backButton2);
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EmailLoginActivityIntent = new Intent(RegisterActivity.this, EmailLoginActivity.class);
                RegisterActivity.this.startActivity(EmailLoginActivityIntent);
            }
        });

        et_email = findViewById(R.id.email_input);
        et_password = findViewById(R.id.password_input);
        et_name = findViewById(R.id.name_input);
        et_phonenum = findViewById(R.id.phonenum_input);

        //genderGroup버튼 성별 판별하기
        genderGroup = findViewById(R.id.genderGroup);
        int genderGroupID = genderGroup.getCheckedRadioButtonId();
        userGender = ((RadioButton) findViewById(genderGroupID)).getText().toString();
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radiogroup, int i) {
                RadioButton genderButton = (RadioButton) findViewById(i);
                userGender = genderButton.getText().toString();
            }
        });

        // 아이디 중복 버튼 잠정 보류
        Button overlapButton = findViewById(R.id.overlapButton);
        overlapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //회원가입 버튼 액션
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser(et_email.getText().toString(), et_password.getText().toString(),
                        et_name.getText().toString(), et_phonenum.getText().toString(),
                        userGender);
                Intent EmailLoginActivityIntent = new Intent(RegisterActivity.this, EmailLoginActivity.class);
                RegisterActivity.this.startActivity(EmailLoginActivityIntent);
            }
        });
    }

    public void addUser(String p_email, String p_pwd, String p_name, String p_number, String p_gender) {
        Register register = new Register(p_email, p_pwd, p_name, p_number, p_gender);
        databaseReference.child("UserDB").child(p_email).setValue(register);
    }
}