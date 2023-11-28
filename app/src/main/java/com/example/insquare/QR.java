package com.example.insquare;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QR extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    //private DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 바로 QR 코드 스캔 시작
        IntentIntegrator intentIntegrator = new IntentIntegrator(QR.this);
        intentIntegrator.setOrientationLocked(true); // 화면 방향 고정
        intentIntegrator.setPrompt("Scan a QR Code"); // QR 코드 스캔 시 표시할 메시지 설정
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE); // QR 코드만 스캔하도록 설정
        intentIntegrator.initiateScan(); // 스캔 시작
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED) {
            // 사용자가 뒤로가기 버튼을 눌렀을 때의 동작
            finish();
        } else if(intentResult != null) {
            if(intentResult.getContents() != null) {
                // QR 코드 스캔 결과 처리
                Intent intent = new Intent(QR.this, UserInfoActivity.class);
                intent.putExtra("QR_DATA", intentResult.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
