package com.example.insquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Map extends AppCompatActivity implements OnMapReadyCallback {
    private ArrayList<String> myUidList;
    RecyclerView rvUsers;
    UserAdapter userAdapter;
    List<List_User> userModelList = new ArrayList<>();
    SearchView searchView;
    private boolean inSearching = false;
    private Marker selectedMarker; // 추가된 부분 (마커)
    private FusedLocationSource mLocationSource;
    private NaverMap mNaverMap;
    private static final String TAG = "Map";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };
    // DB 연결
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        String myIdCode = firebaseUser.getUid().toString();
        myUidList = new ArrayList<>();

        databaseReference = database.getReference("ListDB").child(myIdCode); // DB 테이블 연결

        //db에서 내용 가져오기
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModelList.clear(); // 기존 배열리스트 초기화

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
                                userModelList.add(user);
                                // 데이터 변경을 어댑터에 알리기
                                userAdapter.notifyDataSetChanged();
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

        rvUsers = findViewById(R.id.recyclerView);
        // 초기에 RecyclerView를 숨김
        rvUsers.setVisibility(View.GONE);

        prepareRecycleView();

        //검색 기능 구현
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색 상태 업데이트
                boolean isSearching = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: newText = " + newText);

                try {
                    // 검색 상태 업데이트
                    boolean isSearching = !newText.isEmpty();

                    String searchStr = newText;
                    userAdapter.getFilter().filter(newText);

                    // 검색 중이면 RecyclerView를 표시하고, 그렇지 않으면 숨김
                    if (isSearching) {
                        showFullList();
                    } else {
                        hideRecyclerView();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error in onQueryTextChange", e);
                }

                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SearchView가 확장될 때
                rvUsers.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // SearchView가 축소될 때
                rvUsers.setVisibility(View.GONE);
                return false;
            }
        });

        //지도객체 생성
        {
            FragmentManager fm = getSupportFragmentManager();
            MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance();
                fm.beginTransaction().add(R.id.map, mapFragment).commit();
            }

            //getMapAsync를 호출하여 비동기로 onMapReady 콜백 메소드 호출
            //onMapReady에서 NaverMap 객체 받음
            mapFragment.getMapAsync((OnMapReadyCallback) this);

            //위치 반환하는 구현체인 FusedLocationSource 생성
            mLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
        }

        //하단바
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(item.getItemId() == R.id.list_id) {
                    intent = new Intent(Map.this, List.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(item.getItemId() == R.id.QR_id) {
                    intent = new Intent(Map.this, QR.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(item.getItemId() == R.id.map_id) {
                    intent = new Intent(Map.this, Map.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if(item.getItemId() == R.id.myProfile_id) {
                    intent = new Intent(Map.this, Profile.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }


    public void prepareRecycleView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvUsers.setLayoutManager(linearLayoutManager);
        preAdapter();

        // 초기에는 전체 목록 표시
        showFullList();
    }

    public void preAdapter() {
        userAdapter = new UserAdapter(userModelList, this, new UserAdapter.OnItemClickListener() {
            public void onItemClick(List_User userModel) {
                //도로명 주소 -> 위도/경도 바꾸기
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestGeocod(userModel);
                    }
                }).start();
            }
        });
        rvUsers.setAdapter(userAdapter);
    }

    private void showFullList() { //리사이클러뷰 보이기
        rvUsers.setVisibility(View.VISIBLE);
        // 다른 UI 업데이트 로직 추가 (필요에 따라)
    }

    private void hideRecyclerView() { //리사이클러뷰 숨기기
        rvUsers.setVisibility(View.GONE);
        // 다른 UI 업데이트 로직 추가 (필요에 따라)
    }

    private void showMarker(List_User userModel) {
        if (userModel != null && mNaverMap != null) {
            // 기존 마커가 있으면 제거
            if (selectedMarker != null) {
                selectedMarker.setMap(null);
            }

            // 선택된 항목의 위치에 새로운 마커 추가
            LatLng selectedLocation = new LatLng(userModel.getLoc1(), userModel.getLoc2());
            selectedMarker = new Marker();
            selectedMarker.setPosition(selectedLocation);
            selectedMarker.setMap(mNaverMap);

            // 지도 위치 이동 및 초기 확대 수준 설정 (여기서는 14로 설정)
            CameraPosition cameraPosition = new CameraPosition(selectedLocation, 16.0);
            mNaverMap.moveCamera(CameraUpdate.toCameraPosition(cameraPosition));
        }
    }

    public void requestGeocod(List_User userModel){ // 도로명 주소를 위도, 경도로 바꿔주는 함수
        try{
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();
            String addr = userModel.getP_address();
            String query = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(addr, "UTF-8");
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn != null){
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");

                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "dpncqq1sjt");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", "dcYW2xe4m8O3v2RrHLPTqG1JM1u8pNVovzLb3sdp");

                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();

                if(responseCode == 200){
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }

                int indexFirst;
                int indexLast;

                indexFirst = stringBuilder.indexOf("\"x\":\"");
                indexLast = stringBuilder.indexOf("\",\"y\":");
                String x = stringBuilder.substring(indexFirst + 5, indexLast); //경도

                indexFirst = stringBuilder.indexOf("\"y\":\"");
                indexLast = stringBuilder.indexOf("\",\"distance\":");
                String y = stringBuilder.substring(indexFirst + 5, indexLast); //위도

                // UserModel에 위도 및 경도 설정
                userModel.setLoc1(Double.parseDouble(y));
                userModel.setLoc2(Double.parseDouble(x));

                // UI 업데이트를 메인 스레드에서 수행
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 여기에서 필요한 UI 업데이트 작업 수행
                        // 예: RecyclerView 업데이트, 마커 표시 등
                        userAdapter.notifyDataSetChanged();
                        showMarker(userModel);

                        // 키보드를 내림
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                        // RecyclerView를 숨김
                        rvUsers.setVisibility(View.GONE);

                        // 클릭한 항목의 위치 정보를 tv_location에 표시
                        TextView tvLocation = findViewById(R.id.tv_location);
                        tvLocation.setText(userModel.getP_address() +" "+ userModel.getP_detail_address());
                    }
                });

                bufferedReader.close();
                conn.disconnect();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Log.d(TAG, "onMapReady");

        //NaverMap 객체 받아서 NaverMap 객체에 위치 소스 지정
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(mLocationSource);

        //권한 확인. 결과는 onRequestPermissionResult 콜백 메소드 호출
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //request code와 권한획득 여부 확인
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(mNaverMap != null){
                    mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                } else {
                    Log.e(TAG, "NaverMap is null");
                }
            } else {
                Log.e(TAG, "Location permission not granted");
            }
        }
    }
}