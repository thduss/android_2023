package com.example.insquare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneratedQRActivity extends AppCompatActivity {

    ImageButton return_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_qractivity);

        ImageView imageView = findViewById(R.id.qr_code_generated);

        return_btn = findViewById(R.id.back_btn);
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        // 인텐트에서 QR 데이터 가져오기
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String qrData = extras.getString("QR_DATA");

            // QR 코드 생성을 위한 MultiFormatWriter
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                // QR 코드를 위한 BitMatrix 생성
                BitMatrix bitMatrix = multiFormatWriter.encode(qrData, BarcodeFormat.QR_CODE, 300, 300);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

                // BitMatrix를 Bitmap으로 변환
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                // ImageView에 QR 코드 표시
                imageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                // QR 코드 생성 중 오류 발생 시 사용자에게 알림
                Toast.makeText(this, "QR코드 생성 오류 발생", Toast.LENGTH_LONG).show();
            }
        }
    }
}
