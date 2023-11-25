package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class List extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rv_adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<List_User> arrayList;
    private ArrayList<List_User> filterlist = new ArrayList<>(); //검색 필터 된 리스트
    private ArrayList<String> uidList;
    private ArrayList<String> filterUidlist = new ArrayList<>(); //검색 필터 된 uid 리스트
    private KSG_Custom_Adapter_listver adapter;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private EditText editText;
    private FirebaseAuth mFirebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = findViewById(R.id.recyclerView_List); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)
        uidList = new ArrayList<>();


        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 authentication 연동
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 로그인한 계정 객체화
        String myIdCode = firebaseUser.getUid().toString(); // 객체화한 계정의 고유값을 myIdCode로 받기

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("ListDB").child(myIdCode); //ListDB 안에 내가 추가한 계정들만 있는 DB로 경로 설정

        // 원래 코드 주석처리 해놔서 리스트 안뜰거임!
        // 여기에 그 경로 안에 있는 고유값들만을 받아오는 반복문 설정이 필요해

        //databaseReference = database.getReference("UserDB"); // DB 테이블 연결 선강
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    List_User user = snapshot.getValue(List_User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    String uidKey = snapshot.getKey(); //uid key값 받아오기
                    arrayList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    uidList.add(uidKey); //uid key값 리스트에 추가
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        //edit 텍스트 구현하기
        editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editText.getText().toString();
                filterlist.clear();

                if(searchText.equals("")){
                    adapter.setItems(arrayList);
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).getP_name() != null && arrayList.get(i).getP_company() != null) {
                            if (arrayList.get(i).getP_name().toLowerCase().contains(searchText.toLowerCase()) ||
                                    arrayList.get(i).getP_company().toLowerCase().contains(searchText.toLowerCase())) {
                                filterlist.add(arrayList.get(i));
                                filterUidlist.add(uidList.get(i));
                            }
                        }
                    }
                        adapter.setItems(filterlist);
                    }
                }
        });


        adapter = new KSG_Custom_Adapter_listver(arrayList, uidList, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        //하단바 부분 복붙하셈
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if (item.getItemId() == R.id.list_id) {
                    intent = new Intent(List.this, List.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.QR_id) {
                    intent = new Intent(List.this, QR.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.map_id) {
                    intent = new Intent(List.this, Map.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.myProfile_id) {
                    intent = new Intent(List.this, Profile.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

}
