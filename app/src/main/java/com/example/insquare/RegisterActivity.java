package com.example.insquare;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText et_email, et_password, et_name, et_phonenum;
    RadioGroup genderGroup;
    Button registerButton;
    String userGender;
    AlertDialog dialog;

    DatabaseReference dbReference;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = findViewById(R.id.email_input);
        et_password = findViewById(R.id.password_input);
        et_name = findViewById(R.id.name_input);
        et_phonenum = findViewById(R.id.phonenum_input);

        //파이어 베이스 주석
        dbReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();;

        //상태 바 없애기
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Back Button. EmailLoginActivity창으로 넘어가게 설정
        Button backButton2 = findViewById(R.id.backButton2);
        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EmailLoginActivityIntent = new Intent(RegisterActivity.this, EmailLoginActivity.class);
                RegisterActivity.this.startActivity(EmailLoginActivityIntent);
            }
        });

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

        //회원가입 버튼 액션
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String pwd = et_password.getText().toString();
                String name = et_name.getText().toString().trim();
                String phonenum = et_phonenum.getText().toString().trim();

                if (email.equals("") || pwd.equals("") || name.equals("") || phonenum.equals("")) {
                    AlertDialog.Builder bd = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = bd.setMessage("입력하지 않은 칸이 존재합니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                
                //authentication에 내 이메일, 비번 정보 추가
                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //현재 로그인한 값 받아서
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            // realtime database에 저장하는 과정
                            Register user = new Register();
                            user.setP_uid(firebaseUser.getUid());
                            user.setP_email(firebaseUser.getEmail());
                            user.setP_pwd(pwd);
                            user.setP_name(name);
                            user.setP_number(phonenum);
                            user.setP_gender(userGender);

                            //성공 메세지 출력
                            Toast.makeText(RegisterActivity.this,"회원가입 성공!", Toast.LENGTH_SHORT).show();
                        } else{
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // 중복된 이메일 주소로 사용자를 생성하려고 했을 때의 처리
                                Toast.makeText(RegisterActivity.this,"이미 등록된 이메일 주소입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 다른 이유로 사용자 생성 실패한 경우의 처리
                                Toast.makeText(RegisterActivity.this,"회원가입 실패!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });

                //화면 전환
                Intent EmailLoginActivityIntent = new Intent(RegisterActivity.this, EmailLoginActivity.class);
                RegisterActivity.this.startActivity(EmailLoginActivityIntent);

            }
        });
    }
}