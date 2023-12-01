package com.example.insquare;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

public class nameCard_editpage extends AppCompatActivity {

    ImageButton return_btn;
    Button add_btn, Img_upload_btn;;
    private EditText et_address;
    EditText c_name, c_company, c_department, c_rank, detail_address, c_email, c_number, c_logo;
    String s_name, s_company, s_department, s_rank, s_detail_address, s_email, s_address, s_number, s_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_card_editpage);

        //디비에서 가져와 띄워주기
        c_name = findViewById(R.id.e_name);
        c_company = findViewById(R.id.e_company);
        c_department = findViewById(R.id.e_department);
        c_rank = findViewById(R.id.e_rank);
        et_address = findViewById(R.id.e_address);
        detail_address = findViewById(R.id.e_detail_address);
        c_email = findViewById(R.id.e_email);
        c_number = findViewById(R.id.e_number);
        c_logo = findViewById(R.id.e_logo);

        Intent intent = getIntent();

        s_logo = intent.getExtras().getString("logo");
        s_name = intent.getExtras().getString("username");
        s_company = intent.getExtras().getString("company");
        s_department = intent.getExtras().getString("department");
        s_rank = intent.getExtras().getString("rank");
        s_email = intent.getExtras().getString("email");
        s_number = intent.getExtras().getString("number");
        s_address = intent.getExtras().getString("address");
        s_detail_address = intent.getExtras().getString("detail_address");

        c_name.setText(s_name);
        c_company.setText(s_company);
        c_department.setText(s_department);
        c_rank.setText(s_rank);
        et_address.setText(s_address);
        detail_address.setText(s_detail_address);
        c_email.setText(s_email);
        c_number.setText(s_number);
        c_logo.setText(s_logo);

        // 마이페이지로 뒤로가기 버튼
        return_btn = findViewById(R.id.back_btn);
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nameCardPage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        //이미지 업로드 코드
        Img_upload_btn = findViewById(R.id.img_upload_btn);
        Img_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
            }
        });

        // 명함 추가 버튼 -> 일단 내 명함 리스트로 가도록 구현했는데 디비에 저장되도록 다시 구현 필요
        add_btn = findViewById(R.id.nameCard_fix_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        //도로명 주소 API 코드
        et_address = (EditText) findViewById(R.id.e_address);
        et_address.setFocusable(false);
        et_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //주소 검색 웹 뷰 화면으로 이동
                Intent intent = new Intent(nameCard_editpage.this, WebViewActivity.class);
                getSearchResult.launch(intent);
            }
        });
    }

    // img 업로드
    private void select() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            c_logo.setText(selectedImageUri.toString());
        }
    }

    //도로명 주소 API 코드
    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            // WebViewActivity로 부터의 결과 값이 전달 (setResult에 의해)
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        String data = result.getData().getStringExtra("data");
                        et_address.setText(data);                    }
                }
            }
    );
}