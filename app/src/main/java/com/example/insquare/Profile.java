package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    Button add_btn;
    //데이터베이스 부분
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference myDatabaseReference;
    private ArrayList<List_User> arrayList; //UserDB 리스트 / 전체 유저의 정보가 담김 리스트임
    private ArrayList<List_User> myList; //ListDB 리스트 / 로그인한 유저안에 있는 유저의 정보가 담김 리스트임
    private ArrayList<List_User> filterlist = new ArrayList<>(); //검색 필터 된 리스트
    private ArrayList<String> uidList; //UserDB Uid리스트 / 전체 유저의 Uid 정보가 담긴 리스트임
    private ArrayList<String> myUidList; //ListDB Uid리스트 / 로그인한 유저안에 있는 유저의 Uid 정보가 담긴 리스트임
    private ArrayList<String> filterUidlist = new ArrayList<>(); //검색 필터 된 uid 리스트
    private FirebaseAuth mFirebaseAuth;
    private KSG_Custom_Adapter_listver adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //데이터베이스 연결해 내 명함 리스트 띄우는 부분
        recyclerView = findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)
        uidList = new ArrayList<>();
        myList = new ArrayList<>();
        myUidList = new ArrayList<>();

        mFirebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 authentication 연동
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); // 현재 로그인한 계정 객체화
        String myIdCode = firebaseUser.getUid().toString(); // 객체화한 계정의 고유값을 myIdCode로 받기

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

        // 해당 명함 페이지로 들어가는 버튼
        add_btn = findViewById(R.id.card_add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nameCard_createpage.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        // 명함 리스트 창에 어뎁터 연결
        adapter = new KSG_Custom_Adapter_listver(myList, myUidList, myIdCode ,this);
        recyclerView.setAdapter(adapter);


        //하단바 부분 복붙하셈
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(item.getItemId() == R.id.list_id) {
                    intent = new Intent(Profile.this, List.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(item.getItemId() == R.id.QR_id) {
                    intent = new Intent(Profile.this, QR.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(item.getItemId() == R.id.map_id) {
                    intent = new Intent(Profile.this, Map.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(item.getItemId() == R.id.myProfile_id) {
                    intent = new Intent(Profile.this, Profile.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
}