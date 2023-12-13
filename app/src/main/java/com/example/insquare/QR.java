package com.example.insquare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QR extends AppCompatActivity {

    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mFirebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFirebaseAuth = FirebaseAuth.getInstance();

        // 바로 QR 코드 스캔 시작
        IntentIntegrator intentIntegrator = new IntentIntegrator(QR.this);
        intentIntegrator.setOrientationLocked(true); // 화면 방향 고정
        intentIntegrator.setPrompt("QR코드를 스캔해주세요"); // QR 코드 스캔 시 표시할 메시지 설정
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
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 로그인한 계정 객체화
                String myIdCode = firebaseUser.getUid().toString(); // 객체화한 계정의 고유값을 myIdCode로 받기
                Log.d("JEONJAE",intentResult.getContents().concat(myIdCode));
                dbReference.child("ListDB").child(myIdCode).child(intentResult.getContents()).setValue("");
                // QR 코드 스캔 결과 처리
                Intent intent = new Intent(QR.this, List.class);
                startActivity(intent);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}