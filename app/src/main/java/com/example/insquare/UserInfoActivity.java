package com.example.insquare;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInfoActivity extends AppCompatActivity {
    private EditText nameEditText, companyEditText, departmentEditText, positionEditText, addressEditText, emailEditText, phoneEditText;
    private ImageButton saveButton;
    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // EditText, ImageButton, ImageView 초기화
        nameEditText = findViewById(R.id.nameEditText);
        companyEditText = findViewById(R.id.companyEditText);
        departmentEditText = findViewById(R.id.departmentEditText);
        positionEditText = findViewById(R.id.positionEditText);
        addressEditText = findViewById(R.id.addressEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        saveButton = findViewById(R.id.saveButton);
        logoImageView = findViewById(R.id.logoImageView);

        // QR 코드 데이터 받아오기 및 파싱
        String qrData = getIntent().getStringExtra("QR_DATA");
        parseQRDataAndDisplay(qrData);

        // 저장 버튼 클릭 리스너 설정
        saveButton.setOnClickListener(v -> saveUserInfo());
    }

    // QR 데이터 파싱 및 화면에 표시하는 메소드
    private void parseQRDataAndDisplay(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 7) {
            nameEditText.setText(parts[0]);
            companyEditText.setText(parts[1]);
            departmentEditText.setText(parts[2]);
            positionEditText.setText(parts[3]);
            addressEditText.setText(parts[4]);
            emailEditText.setText(parts[5]);
            phoneEditText.setText(parts[6]);

            // Glide를 사용하여 로고 이미지를 ImageView에 로드
            Glide.with(this).load(parts[7]).into(logoImageView);
        }
    }

    // 사용자 정보를 Firebase에 저장하는 메소드
    private void saveUserInfo() {
        // EditText에서 정보 추출
        String name = nameEditText.getText().toString();
        String company = companyEditText.getText().toString();
        String department = departmentEditText.getText().toString();
        String position = positionEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String logo = ""; // QR 데이터의 8번째 부분이 로고 URL이라고 가정
        if (!phoneEditText.getText().toString().isEmpty()) {
            logo = phoneEditText.getText().toString();
        }

        // List_User 객체 생성 및 설정
        List_User user = new List_User();
        user.setP_name(name);
        user.setP_company(company);
        user.setP_department(department);
        user.setP_position(position);
        user.setP_address(address);
        user.setP_email(email);
        user.setP_number(phone);
        user.setP_logo(logo);

        // Firebase에 List_User 객체 저장
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ListDB");
        databaseReference.push().setValue(user)
                .addOnSuccessListener(aVoid -> Toast.makeText(UserInfoActivity.this, "저장 성공", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(UserInfoActivity.this, "저장 실패", Toast.LENGTH_SHORT).show());
    }
}
