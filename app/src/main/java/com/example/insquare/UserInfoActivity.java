package com.example.insquare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// QR코드 스캔하여 데이터 받으면 정보 띄우는 액티비티
public class UserInfoActivity extends AppCompatActivity {
    private EditText nameEditText, companyEditText, departmentEditText, positionEditText, addressEditText, emailEditText, phoneEditText;
    private Button saveButton;
    private ImageView logoImageView;
    private String[] parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // EditText, Button 및 ImageView 초기화
        nameEditText = findViewById(R.id.nameEditText);
        companyEditText = findViewById(R.id.companyEditText);
        departmentEditText = findViewById(R.id.departmentEditText);
        positionEditText = findViewById(R.id.positionEditText);
        addressEditText = findViewById(R.id.addressEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        saveButton = findViewById(R.id.saveButton);
        logoImageView = findViewById(R.id.logoImageView);

        // QR 코드 데이터 받아 파싱하고 화면에 표시
        String qrData = getIntent().getStringExtra("QR_DATA");
        parseAndDisplayQRData(qrData);

        // 저장 버튼 클릭 이벤트 처리
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
    }

    // QR 데이터 파싱 및 화면에 표시하는 메서드
    private void parseAndDisplayQRData(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 8) {
            nameEditText.setText(parts[0]);
            companyEditText.setText(parts[1]);
            departmentEditText.setText(parts[2]);
            positionEditText.setText(parts[3]);
            addressEditText.setText(parts[4]);
            emailEditText.setText(parts[5]);
            phoneEditText.setText(parts[6]);

            // 로고 URL을 사용하여 ImageView에 이미지 로딩
            Glide.with(this).load(this.parts[7]).into(logoImageView);
        }
    }

    // 사용자 정보를 Firebase에 저장하는 메서드
    private void saveUserInfo() {
        // 각 EditText에서 정보 추출
        String name = nameEditText.getText().toString();
        String company = companyEditText.getText().toString();
        String department = departmentEditText.getText().toString();
        String position = positionEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneEditText.getText().toString();
        String logoUrl = parts[7];

        // UserInfo 객체 생성
        Register register = new Register(name, company, department, position, address, phoneNumber, email, logoUrl);

        // Firebase 데이터베이스 참조 생성
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ListDB");


        // Firebase에 사용자 정보 저장
        databaseReference.push().setValue(register)
                .addOnSuccessListener(aVoid -> {
                    // 데이터 저장 성공 시 사용자에게 알림
                    Toast.makeText(UserInfoActivity.this, "저장하였습니다", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // 데이터 저장 실패 시 사용자에게 알림
                    Toast.makeText(UserInfoActivity.this, "저장에 실패했습니다", Toast.LENGTH_SHORT).show();
                });
    }
}
