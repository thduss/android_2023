package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

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
    private ArrayList<List_User> arrayList; //UserDB 리스트 / 전체 유저의 정보가 담김 리스트임
    private ArrayList<List_User> myList; //ListDB 리스트 / 로그인한 유저안에 있는 유저의 정보가 담김 리스트임
    private ArrayList<List_User> filterlist = new ArrayList<>(); //검색 필터 된 리스트
    private ArrayList<String> uidList; //UserDB Uid리스트 / 전체 유저의 Uid 정보가 담긴 리스트임
    private ArrayList<String> myUidList; //ListDB Uid리스트 / 로그인한 유저안에 있는 유저의 Uid 정보가 담긴 리스트임
    private ArrayList<String> filterUidlist = new ArrayList<>(); //검색 필터 된 uid 리스트
    private KSG_Custom_Adapter_listver adapter;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference myDatabaseReference;

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
        myList = new ArrayList<>();
        myUidList = new ArrayList<>();

        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 authentication 연동
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 로그인한 계정 객체화
        String myIdCode = firebaseUser.getUid().toString(); // 객체화한 계정의 고유값을 myIdCode로 받기
        
        //이거 없어도 될듯
        //Toast.makeText(this, myIdCode, Toast.LENGTH_SHORT).show();

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        //////////////////////////////////////////////////////////////////////////////////////////////
        myDatabaseReference = database.getReference("ListDB").child(myIdCode); //ListDB 안에 내가 추가한 계정들만 있는 DB로 경로 설정

        myDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myList.clear(); // 기존 배열리스트 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String friendUid = snapshot.getKey(); // 친구의 키값
                    myUidList.add(friendUid);

                    // "UserDB"에서 해당 사용자의 정보를 가져오기
                    DatabaseReference userDatabaseReference = database.getReference("UserDB").child(friendUid);
                    userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            // "UserDB"에서 해당 사용자의 정보를 추출
                            if (userSnapshot.exists()) {
                                List_User user = userSnapshot.getValue(List_User.class);
                                // 가져온 사용자 정보를 myList에 추가
                                myList.add(user);
                                // 데이터 변경을 어댑터에 알리기
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // UserDB에서 사용자 정보를 가져오는 과정에서 에러 발생 시 처리
                            Log.e("ListActivity", "Failed to retrieve user information: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // ListDB에서 데이터를 가져오는 과정에서 에러 발생 시 처리
                Log.e("ListActivity", "Failed to retrieve friend UIDs: " + databaseError.getMessage());
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
                    adapter.setItems(myList);
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    for (int i = 0; i < myList.size(); i++) {
                        if (myList.get(i).getP_name() != null && myList.get(i).getP_company() != null) {
                            if (myList.get(i).getP_name().toLowerCase().contains(searchText.toLowerCase()) ||
                                    myList.get(i).getP_company().toLowerCase().contains(searchText.toLowerCase())) {
                                filterlist.add(myList.get(i));
                                filterUidlist.add(myUidList.get(i));
                            }
                        }
                    }
                    adapter.setItems(filterlist);
                }
            }
        });


        
        adapter = new KSG_Custom_Adapter_listver(myList, myUidList, myIdCode ,this);
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
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.QR_id) {
                    intent = new Intent(List.this, QR.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.map_id) {
                    intent = new Intent(List.this, Map.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.myProfile_id) {
                    intent = new Intent(List.this, Profile.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

    }

}