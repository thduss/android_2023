package com.example.insquare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class KSG_Lc_List_Activity extends Activity {
    TextView lc_Id;
    TextView lc_Username;
    String lId, lUsername;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_list_lc_popup);

        lc_Id = findViewById(R.id.tv_id2);
        lc_Username = findViewById(R.id.tv_userName2);

        Intent intent = getIntent();

        lId = intent.getExtras().getString("company");
        lUsername = intent.getExtras().getString("username");

        lc_Id.setText(lId);
        lc_Username.setText(lUsername);

        ImageButton backBt = findViewById(R.id.bt_back2);
        Button delBt = findViewById(R.id.bt_delete2);

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
