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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    RecyclerView rvUsers;
    UserAdapter userAdapter;
    List<UserModel> userModelList = new ArrayList<>();
    SearchView searchView;
    private boolean inSearching = false;
    private Marker selectedMarker; // 추가된 부분 (마커)
    private Marker initMarker; // 초기 마커를 클래스 변수로 선언
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("UserDB"); // DB 테이블 연결

        //db에서 내용 가져오기
        /*databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                userModelList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    UserModel user = snapshot.getValue(UserModel.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    userModelList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                userAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });*/

        rvUsers = findViewById(R.id.recyclerView);
        // 초기에 RecyclerView를 숨김
        rvUsers.setVisibility(View.GONE);
        setData(); // 데이터 입력되는 함수 호출
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
                    return true;
                }
                else if(item.getItemId() == R.id.QR_id) {
                    intent = new Intent(Map.this, QR.class);
                    startActivity(intent);
                    return true;
                }
                else if(item.getItemId() == R.id.map_id) {
                    intent = new Intent(Map.this, Map.class);
                    startActivity(intent);
                    return true;
                }
                else if(item.getItemId() == R.id.myProfile_id) {
                    intent = new Intent(Map.this, Profile.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public void setData(){
        userModelList.add(new UserModel("선강", "김","덕진구청", "전라북도 전주시 덕진구 벚꽃로 55 덕진구청", 0.0, 0.0));
        userModelList.add(new UserModel("재영", "윤","전주시청","전라북도 전주시 완산구 노송광장로 10 전주시청",0.0, 0.0));
        userModelList.add(new UserModel("소연", "김","창원시청","경상남도 창원시 성산구 중앙대로 151 창원시청",0.0, 0.0));
        userModelList.add(new UserModel("현", "진","김제시청","전라북도 김제시 중앙로 40 김제시청",0.0, 0.0));
        userModelList.add(new UserModel("재영", "전","광주광역시청","광주 서구 내방로 111 광주광역시청",0.0, 0.0));
        userModelList.add(new UserModel("유희", "이","화순군청","전라남도 화순군 화순읍 동헌길 23 화순군청",0.0, 0.0));
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
            public void onItemClick(UserModel userModel) {
                //도로명 주소 -> 위도/경도 바꾸기
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestGeocod(userModel);
                    }
                }).start();

                // 클릭한 항목의 위치에 마커 표시
                /*showMarker(userModel);

                // 키보드를 내림
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                // RecyclerView를 숨김
                rvUsers.setVisibility(View.GONE);

                // 클릭한 항목의 위치 정보를 tv_location에 표시
                TextView tvLocation = findViewById(R.id.tv_location);
                tvLocation.setText((int) userModel.getLoc1() + ", " + (int) userModel.getLoc2());*/
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

    private void showMarker(UserModel userModel) {
        if(initMarker != null){ //초기 마커 있으면 지워줌 (초기 마커는 딱히 필요없으므로 테스트 다 끝내고 삭제하기)
            initMarker.setMap(null);
        }

        if (userModel != null && mNaverMap != null) {
            // 기존 마커가 있으면 제거
            if (selectedMarker != null) {
                selectedMarker.setMap(null);
            }

            // 선택된 항목의 위치에 새로운 마커 추가
            //LatLng selectedLocation = new LatLng(35.8477707, 127.1302242); //전북대
            LatLng selectedLocation = new LatLng(userModel.getLoc1(), userModel.getLoc2());
            selectedMarker = new Marker();
            selectedMarker.setPosition(selectedLocation);
            selectedMarker.setMap(mNaverMap);

            // 지도 위치 이동
            mNaverMap.moveCamera(CameraUpdate.scrollTo(selectedLocation));
        }
    }

    public void requestGeocod(UserModel userModel){ // 도로명 주소를 위도, 경도로 바꿔주는 함수
        try{
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();
            String addr = userModel.getP_location();
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
                        tvLocation.setText(userModel.getP_location());
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

        //지도 상에 초기 (test) 마커 표시
        initMarker = new Marker();
        LatLng initLocation = new LatLng(35.8087086,126.8927011); //우리집
        initMarker.setPosition(initLocation);
        initMarker.setMap(naverMap);

        //NaverMap 객체 받아서 NaverMap 객체에 위치 소스 지정
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(mLocationSource);

        // 지도 위치 이동 및 초기 확대 수준 설정 (여기서는 14로 설정)
        //CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initLocation).zoomIn(15.0);
        //mNaverMap.moveCamera(cameraUpdate);
        CameraPosition cameraPosition = new CameraPosition(initLocation, 16.0);
        mNaverMap.moveCamera(CameraUpdate.toCameraPosition(cameraPosition));


//        mNaverMap.moveCamera(CameraUpdate.scrollTo(initLocation).zoomTo(15.0));

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