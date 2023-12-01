package com.example.insquare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class KSY_Custom_Adapter_listver extends RecyclerView.Adapter<KSY_Custom_Adapter_listver.CustomViewHolder>{
    private ArrayList<myRegister> arrayList;
    private ArrayList<String> uidList;
    private String myUid;
    private Context context;


    public KSY_Custom_Adapter_listver(ArrayList<myRegister> arrayList, ArrayList<String> uidList, String myUid, Context context) {
        this.arrayList = arrayList;
        this.uidList = uidList;
        this.myUid = myUid;
        this.context = context;
    }



    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_in_view, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    //각 정보 리스트에 데이터베이스에서 정보를 가져와 삽입시켜주는 함수
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.itemView.setTag(position);
        //프로필 사진 정보 불러오기
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getM_logo())
                .into(holder.iv_logo);

        //회사id 정보 받아오기
        holder.tv_company.setText(arrayList.get(position).getM_company());

        //이름 정보 받아오기
        holder.tv_username.setText(arrayList.get(position).getM_name());

        //short 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = holder.getAbsoluteAdapterPosition();
                Context context = v.getContext();

                //클릭한 뷰 index 값
                int num = holder.getAbsoluteAdapterPosition();

                Log.e("아이템의 num 값", String.valueOf(num + 1));
                String str_pos = String.valueOf(num + 1);

                Intent intent = new Intent(context, nameCardPage.class);

                intent.putExtra("index", str_pos);
                intent.putExtra("logo", arrayList.get(pos).getM_logo());
                intent.putExtra("username", arrayList.get(pos).getM_name());
                intent.putExtra("company", arrayList.get(pos).getM_company());
                intent.putExtra("department", arrayList.get(pos).getM_department());
                intent.putExtra("rank", arrayList.get(pos).getM_rank());
                intent.putExtra("email", arrayList.get(pos).getM_email());
                intent.putExtra("address", arrayList.get(pos).getM_address());
                intent.putExtra("detail_address", arrayList.get(pos).getM_detailAddress());
                intent.putExtra("number", arrayList.get(pos).getM_number());

                context.startActivity(intent);


            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getAbsoluteAdapterPosition();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("명함첩에서 삭제");
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteItem(pos);
                            }
                        });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRegister selectedCard = arrayList.get(position);

                Intent intent = new Intent(context, nameCardPage.class);
                intent.putExtra("cardInfo", selectedCard); // myRegister 객체를 전달
                context.startActivity(intent);
            }
        });
    }

    //정보가 몇개냐 잘 안쓰임 상관 안해도됨
    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    //검색기능
    public void setItems(ArrayList<myRegister> list){
        arrayList = list;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        // 삭제할 아이템의 Firebase 키 가져오기
        String deleteKey = uidList.get(position);

        // Firebase에서 데이터 삭제
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ListDB")
                .child(myUid)
                .child(deleteKey);

        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // 데이터 삭제 성공 시
                Toast.makeText(context, "데이터 삭제 성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 데이터 삭제 실패 시
                Toast.makeText(context, "데이터 삭제 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 어댑터에서 아이템 삭제 및 UI 갱신
        arrayList.remove(position);
        uidList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }


    //리싸이클러뷰 연결 하는 함수 리싸이클러뷰에 정보 연결은 여기서 함
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_logo;
        TextView tv_company;
        TextView tv_username;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_logo = itemView.findViewById(R.id.iv_logo1);
            this.tv_company = itemView.findViewById(R.id.tv_company1);
            this.tv_username = itemView.findViewById(R.id.tv_username1);


        }
    }

}
