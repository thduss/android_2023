package com.example.insquare;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import android.net.Uri;

public class nameCard_createpage extends AppCompatActivity {
    ImageButton back_btn;
    Button add_btn, Img_upload_btn;
    private EditText et_address;
    EditText c_name, c_company, c_department, c_rank, detail_address, c_email, c_number, c_logo;
    AlertDialog dialog;
    DatabaseReference dbReference;
    FirebaseAuth mFirebaseAuth;
    private Uri uri;
    private final int GALLERY_CODE = 10;
    private FirebaseStorage storage;

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

        //도로명 주소 API 코드
        et_address = (EditText) findViewById(R.id.et_address2);
        et_address.setFocusable(false);
        et_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //주소 검색 웹 뷰 화면으로 이동
                Intent intent = new Intent(nameCard_createpage.this, WebViewActivity.class);
                getSearchResult.launch(intent);
            }
        });

        //버튼 누르면 디비 저장
        c_name = findViewById(R.id.c_name);
        c_company = findViewById(R.id.c_company);
        c_department = findViewById(R.id.c_department);
        c_rank = findViewById(R.id.c_rank);
        et_address = findViewById(R.id.et_address2);
        detail_address = findViewById(R.id.detail_address);
        c_email = findViewById(R.id.c_email);
        c_number = findViewById(R.id.c_number);
        c_logo = findViewById(R.id.c_logo);

        add_btn = findViewById(R.id.namecard_add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = c_name.getText().toString();
                String company = c_company.getText().toString();
                String department = c_department.getText().toString();
                String rank = c_rank.getText().toString();
                String address = et_address.getText().toString();
                String detailAddress = detail_address.getText().toString();
                String email = c_email.getText().toString();
                String number = c_number.getText().toString();
                String logo = c_logo.getText().toString();

                if (name.equals("") || company.equals("") || department.equals("") || rank.equals("") || address.equals("")
                        || detailAddress.equals("") || email.equals("") || number.equals("") || logo.equals("") ) {
                    AlertDialog.Builder bd = new AlertDialog.Builder(nameCard_createpage.this);
                    dialog = bd.setMessage("입력하지 않은 칸이 존재합니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                //현재 로그인한 값 받아서
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                // realtime database에 저장하는 과정
                myRegister user = new myRegister();
                user.setM_uid(firebaseUser.getUid());
                user.setM_name(name);
                user.setM_company(company);
                user.setM_department(department);
                user.setM_rank(rank);
                user.setM_address(address);
                user.setM_detailAddress(detailAddress);
                user.setM_email(email);
                user.setM_number(number);
                user.setM_logo(logo);

                dbReference.child("MyList").child(firebaseUser.getUid()).setValue(user);

                //성공 메세지 출력
                Toast.makeText(nameCard_createpage.this,"회원가입 성공!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // img 업로드
    private void select() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT );
        intent.setType("image/*");
        launcher.launch(intent);
    }
    private void upload() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Study");
        storageReference.child("images").child("image").putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(nameCard_createpage.this, "업로드에 성공했습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(nameCard_createpage.this, "업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        Log.d("test", uri.toString());

                    }
                }
            }
    );


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