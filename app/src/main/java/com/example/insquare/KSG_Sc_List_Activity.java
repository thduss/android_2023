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
    TextView sc_Username,sc_Username2 ,sc_Department, sc_Position, sc_Email, sc_Number, sc_Adress;
    String sLogo, sUsername, sDepartment, sPosition, sEmail, sNumber, sAdress;

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
        sc_Adress = findViewById(R.id.sc_tv_back_adress);
        sc_Email = findViewById(R.id.sc_tv_back_email);
        sc_Number = findViewById(R.id.sc_tv_back_number);

        Intent intent = getIntent();

        sLogo = intent.getExtras().getString("logo");
        sUsername = intent.getExtras().getString("username");
        sDepartment = intent.getExtras().getString("department");
        sPosition = intent.getExtras().getString("position");
        sEmail = intent.getExtras().getString("email");
        sNumber = intent.getExtras().getString("number");
        sAdress = intent.getExtras().getString("adress");

        Glide.with(this)
                .load(sLogo)
                .into(this.sc_Logo);
        sc_Username.setText(sUsername);
        sc_Username2.setText(sUsername);
        sc_Department.setText(sDepartment);
        sc_Position.setText(sPosition);
        sc_Adress.setText(sAdress);
        sc_Email.setText(sEmail);
        sc_Number.setText(sNumber);

        ImageButton backBt = findViewById(R.id.sc_bt_back);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
