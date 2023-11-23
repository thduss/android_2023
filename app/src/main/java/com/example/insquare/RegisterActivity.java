package com.example.insquare;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    EditText et_id, et_password, et_name, et_phonenum;
    RadioGroup genderGroup;
    Button registerButton;
    String userGender;
    AlertDialog dialog;
    boolean overlap = false;

    //파이어 베이스 주석
    //제발 되라 - 윤재영
    //test - 진현
    //test2 - 김소연
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_id = findViewById(R.id.id_input);
        et_password = findViewById(R.id.password_input);
        et_name = findViewById(R.id.name_input);
        et_phonenum = findViewById(R.id.phonenum_input);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

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

        // 아이디 중복 버튼 액션
        Button overlapButton = findViewById(R.id.overlapButton);
        overlapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString().trim();
                if (overlap) return;

                if (id.equals("")) {
                    AlertDialog.Builder bd = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = bd.setMessage("아이디가 비어있습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                databaseReference.child("UserDB").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            AlertDialog.Builder bd = new AlertDialog.Builder(RegisterActivity.this);
                            dialog = bd.setMessage("사용 가능한 아이디입니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();
                            et_id.setEnabled(false);
                            overlap = true;
                            et_id.setBackgroundColor(Color.parseColor("#808080"));
                            overlapButton.setBackgroundColor(Color.parseColor("#808080"));
                        } else {
                            AlertDialog.Builder bd = new AlertDialog.Builder(RegisterActivity.this);
                            dialog = bd.setMessage("사용 할 수 없는 아이디입니다.")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error){
                        Log.e("FirebaseDatabase", "Error: " + error.getMessage());
                    }
                });
            }
        });


        //회원가입 버튼 액션
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString().trim();
                String pwd = et_password.getText().toString().trim();
                String name = et_name.getText().toString().trim();
                String phonenum = et_phonenum.getText().toString().trim();
                if (!overlap) {
                    AlertDialog.Builder bd = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = bd.setMessage("아이디 중복 확인이 되지 않았습니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                if (id.equals("") || pwd.equals("") || name.equals("") || phonenum.equals("")) {
                    AlertDialog.Builder bd = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = bd.setMessage("입력하지 않은 칸이 존재합니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                //DataBase에 추가
                addUser(et_id.getText().toString().trim(), et_password.getText().toString().trim(),
                        et_name.getText().toString(), et_phonenum.getText().toString(),
                        userGender);
                //화면 전환
                Intent EmailLoginActivityIntent = new Intent(RegisterActivity.this, EmailLoginActivity.class);
                RegisterActivity.this.startActivity(EmailLoginActivityIntent);
                //성공 메세지 출력
                Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //파이어 베이스 연결
    public void addUser(String p_id, String p_pwd, String p_name, String p_number, String p_gender) {
        Register register = new Register(p_id, p_pwd, p_name, p_number, p_gender);
        databaseReference.child("UserDB").child(p_id).setValue(register);
    }
}