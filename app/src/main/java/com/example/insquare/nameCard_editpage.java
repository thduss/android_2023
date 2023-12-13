package com.example.insquare;

import static android.content.ContentValues.TAG;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class nameCard_editpage extends AppCompatActivity {

    ImageButton return_btn;
    ImageView iv_upload_image;
    Button add_btn, img_upload_btn;
    private EditText et_address;
    EditText c_name, c_company, c_department, c_rank, detail_address, c_email, c_number, c_logo;
    String s_name, s_company, s_department, s_rank, s_detail_address, s_email, s_address, s_number, s_logo, sIndex;
    AlertDialog dialog;
    DatabaseReference dbReference;
    FirebaseAuth mFirebaseAuth;
    private Uri imageUri;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = mStorage.getReference();
    private UploadTask uploadTask = null; // 파일 업로드하는 객체
    private String url;

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

        //갤러리 접근 코드
        iv_upload_image = findViewById(R.id.iv_upload_image);
        iv_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityResult.launch(intent);
            }
        });
        // storage 저장 코드
        img_upload_btn = findViewById(R.id.img_upload_btn);
        img_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사진을 스토리지에 올리는 코드
                // 이미지 파일 경로 지정 (/item/사용자 documentId / IAMGE_DOCUMENTID_UPLOADID_.png)
                String filename = "image_" + System.currentTimeMillis() + ".png";
                storageRef = mStorage.getReference().child("images").child(filename);
                uploadTask = storageRef.putFile(imageUri);
                //파일 업로드 시작
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //업로드 성공 시 이미지를 올린 url 가져오기
                        Log.d(TAG, "onSuccess: upload");
                        downloadUri(); // 업로드 성공 시 업로드한 파일 Uri 다운받기
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url = uri.toString();
                                Log.d("uri : ", uri.toString());
                                c_logo.setText(url);
//                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //업로드 실패 시 동작
                        Log.d(TAG, "onFailure: upload");
                    }
                });

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
                List_User user = new List_User();
                user.setP_name(name);
                user.setP_company(company);
                user.setP_department(department);
                user.setP_position(rank);
                user.setP_address(address);
                user.setP_detail_address(detailAddress);
                user.setP_email(email);
                user.setP_number(number);
                user.setP_logo(logo);

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
    // 갤러리 접근 코드
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if ( result.getResultCode() == RESULT_OK && result.getData() != null) {
                imageUri = result.getData().getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    iv_upload_image.setImageBitmap(bitmap);	//이미지를 띄울 이미지뷰 설정
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    // 지정한 경로(reference)에 대한 uri 을 다운로드하는 method
    // uri를 통해 이미지를 불러올 수 있음
    void downloadUri() {
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: download");
            }
        });
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