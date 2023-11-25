package com.example.insquare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class KSG_Lc_List_Activity extends Activity {
    TextView lc_Id;
    TextView lc_Username;
    String lId, lUsername, lKey;
    FirebaseDatabase database;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_list_lc_popup);

        lc_Id = findViewById(R.id.tv_id2);
        lc_Username = findViewById(R.id.tv_userName2);

        Intent intent = getIntent();

        lId = intent.getExtras().getString("company");
        lUsername = intent.getExtras().getString("username");
        lKey = intent.getExtras().getString("key");

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

                database.getReference().child("UserDB").child(lKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(KSG_Lc_List_Activity.this, "삭제 성공"
                        , Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(KSG_Lc_List_Activity.this, "삭제 실패"
                                + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                finish();
            }
        });

    }
}
