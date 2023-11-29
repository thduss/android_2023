package com.example.insquare;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class nameCard_editpage extends AppCompatActivity {

    ImageButton return_btn;
    Button add_btn;
    private EditText et_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_card_editpage);

        // 명함 앞 페이지로 변환
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

        // 명함 추가 버튼 -> 일단 내 명함 리스트로 가도록 구현했는데 디비에 저장되도록 다시 구현 필요
        add_btn = findViewById(R.id.namecard_fix_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        //도로명 주소 API 코드
        et_address = (EditText) findViewById(R.id.et_address);
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