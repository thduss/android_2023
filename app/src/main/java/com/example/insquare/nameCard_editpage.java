package com.example.insquare;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class nameCard_editpage extends AppCompatActivity {

    ImageButton return_btn;
    Button add_btn, Img_upload_btn;;
    private EditText et_address;
    EditText c_name, c_company, c_department, c_rank, detail_address, c_email, c_number, c_logo;
    String s_name, s_company, s_department, s_rank, s_detail_address, s_email, s_address, s_number, s_logo, sIndex;
    AlertDialog dialog;
    DatabaseReference dbReference;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_card_editpage);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference();

        //디비에서 가져와 띄워주기
        c_name = findViewById(R.id.e_name);
        c_company = findViewById(R.id.e_company);
        c_department = findViewById(R.id.e_department);
        c_rank = findViewById(R.id.e_rank);
        et_address = findViewById(R.id.e_address);
        detail_address = findViewById(R.id.e_detail_address);
        c_email = findViewById(R.id.e_email);
        c_number = findViewById(R.id.e_number);
        c_logo = findViewById(R.id.e_logo);

        Intent intent = getIntent();

        sIndex = intent.getExtras().getString("index"); // 리스트 번호 즉 명함 번호
        s_logo = intent.getExtras().getString("logo");
        s_name = intent.getExtras().getString("username");
        s_company = intent.getExtras().getString("company");
        s_department = intent.getExtras().getString("department");
        s_rank = intent.getExtras().getString("rank");
        s_email = intent.getExtras().getString("email");
        s_number = intent.getExtras().getString("number");
        s_address = intent.getExtras().getString("address");
        s_detail_address = intent.getExtras().getString("detail_address");
        Log.e("edit 아이템의 num 값", String.valueOf(sIndex));

        c_name.setText(s_name);
        c_company.setText(s_company);
        c_department.setText(s_department);
        c_rank.setText(s_rank);
        et_address.setText(s_address);
        detail_address.setText(s_detail_address);
        c_email.setText(s_email);
        c_number.setText(s_number);
        c_logo.setText(s_logo);

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

        //이미지 업로드 코드
        Img_upload_btn = findViewById(R.id.img_upload_btn);
        Img_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select();
            }
        });

        // 명함 수정 버튼
        add_btn = findViewById(R.id.nameCard_fix_btn);
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
                        || detailAddress.equals("") || email.equals("") || number.equals("") ) {
                    AlertDialog.Builder bd = new AlertDialog.Builder(nameCard_editpage.this);
                    dialog = bd.setMessage("입력하지 않은 칸이 존재합니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                //현재 로그인한 값 받아서
                // realtime database에 저장하는 과정
                myRegister user = new myRegister();
                user.setM_name(name);
                user.setM_company(company);
                user.setM_department(department);
                user.setM_rank(rank);
                user.setM_address(address);
                user.setM_detailAddress(detailAddress);
                user.setM_email(email);
                user.setM_number(number);
                user.setM_logo(logo);

                dbReference.child("MyNameCardDB").child(firebaseUser.getUid()).child(sIndex).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String dbAddress = firebaseUser.getUid().concat(sIndex); // 클릭한 명함의 고유값을 가져옴

                        // 기존 데이터를 업데이트
                        DatabaseReference userRef = dbReference.child("UserDB").child(dbAddress);
                        userRef.setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // 성공적으로 업데이트된 경우
                                        Toast.makeText(nameCard_editpage.this, "명함 수정 성공!", Toast.LENGTH_SHORT).show();

                                        // 수정이 완료되면 프로필 화면으로 이동
                                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                                        startActivity(intent);
                                        overridePendingTransition(0, 0);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // 에러 처리
                                        Toast.makeText(nameCard_editpage.this, "명함 수정 실패!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 에러 처리
                        Toast.makeText(nameCard_editpage.this,"명함 수정 실패!", Toast.LENGTH_SHORT).show();
                    }
                });


                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        //도로명 주소 API 코드
        et_address = (EditText) findViewById(R.id.e_address);
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

    // img 업로드
    private void select() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            c_logo.setText(selectedImageUri.toString());
        }
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