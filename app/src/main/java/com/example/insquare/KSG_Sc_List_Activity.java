package com.example.insquare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.wajahatkarim3.easyflipview.EasyFlipView;

public class KSG_Sc_List_Activity extends AppCompatActivity {
    ImageView sc_Logo;
    TextView sc_Username,sc_Username2 ,sc_Department, sc_Position;
    String sLogo, sUsername, sDepartment, sPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sc_page);

        final EasyFlipView easyFlipView = (EasyFlipView) findViewById(R.id.flipview);
        easyFlipView.setFlipDuration(400);
        easyFlipView.setFlipEnabled(true);

        findViewById(R.id.frontcardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
            }
        });
        findViewById(R.id.backcardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
            }
        });

        sc_Logo = findViewById(R.id.sc_tv_logo);
        sc_Username = findViewById(R.id.sc_tv_name);
        sc_Username2 = findViewById(R.id.sc_tv_back_name);
        sc_Department = findViewById(R.id.sc_tv_name2);
        sc_Position = findViewById(R.id.sc_tv_name3);

        Intent intent = getIntent();

        sLogo = intent.getExtras().getString("logo");
        sUsername = intent.getExtras().getString("username");
        sDepartment = intent.getExtras().getString("department");
        sPosition = intent.getExtras().getString("position");

        Glide.with(this)
                .load(sLogo)
                .into(this.sc_Logo);
        sc_Username.setText(sUsername);
        sc_Department.setText(sDepartment);
        sc_Position.setText(sPosition);

        ImageButton backBt = findViewById(R.id.sc_bt_back);
        ImageButton qrBt = findViewById(R.id.sc_bt_qr);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        qrBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //QR코드 보이게끔 구현할 예정
                finish();
            }
        });

    }
}
