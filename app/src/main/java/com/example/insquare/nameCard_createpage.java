package com.example.insquare;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;
public class nameCard_createpage extends AppCompatActivity {
    ImageButton back_btn;
    Button add_btn;
    private EditText et_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_card_createpage);

        //profile로 돌아가기
        back_btn = findViewById(R.id.create_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        // 명함 추가 버튼 -> 일단 내 Profile로 가도록 구현했는데 디비에 저장되도록 다시 구현 필요
        add_btn = findViewById(R.id.namecard_add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        //도로명 주소 API 코드
        et_address = (EditText) findViewById(R.id.et_address);
        et_address.setFocusable(false);
        et_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //주소 검색 웹 뷰 화면으로 이동
                Intent intent = new Intent(nameCard_createpage.this, WebViewActivity.class);
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