package com.example.insquare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class KSG_Custom_Adapter_listver extends RecyclerView.Adapter<KSG_Custom_Adapter_listver.CustomViewHolder>{
    private ArrayList<List_User> arrayList;
    private Context context;


    public KSG_Custom_Adapter_listver(ArrayList<List_User> arrayList, Context context) {
        this.arrayList = arrayList;
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
                .load(arrayList.get(position).getLogo())
                .into(holder.iv_logo);

        //회사id 정보 받아오기
        holder.tv_company.setText(arrayList.get(position).getCompany());

        //이름 정보 받아오기
        holder.tv_username.setText(arrayList.get(position).getUsername());

        //short 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = holder.getAbsoluteAdapterPosition();
                Context context = v.getContext();

                Intent intent = new Intent(context, KSG_Sc_List_Activity.class);

                intent.putExtra("logo", arrayList.get(pos).getLogo());
                intent.putExtra("username", arrayList.get(pos).getUsername());
                intent.putExtra("department", arrayList.get(pos).getDepartment());
                intent.putExtra("position", arrayList.get(pos).getPosition());

                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getAbsoluteAdapterPosition();
                Context context = v.getContext();

                Intent intent = new Intent(context, KSG_Lc_List_Activity.class);

                intent.putExtra("company", arrayList.get(pos).getCompany());
                intent.putExtra("username", arrayList.get(pos).getUsername());

                context.startActivity(intent);
                return true;
            }
        });
    }

    //정보가 몇개냐 잘 안쓰임 상관 안해도됨
    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    //삭제기능 구현할려고 만든 함수
    public void remove(int position) {

    }


    //리싸이클러뷰 연결 하는 함수 리싸이클러뷰에 정보 연결은 여기서 함
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_logo;
        TextView tv_company;
        TextView tv_username;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_logo = itemView.findViewById(R.id.iv_logo);
            this.tv_company = itemView.findViewById(R.id.tv_company);
            this.tv_username = itemView.findViewById(R.id.tv_username);


        }
    }
}
