package com.example.insquare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterVh> implements Filterable {

    public List<List_User> userModelList = new ArrayList<>();
    public List<List_User> getUserModelListFilter = new ArrayList<>();
    public Context context;

    // 아이템 클릭 리스너 인터페이스 추가
    public interface OnItemClickListener {
        void onItemClick(List_User userModel);
    }

    private final OnItemClickListener onItemClickListener;

    public UserAdapter(List<List_User> userModels, Context context, OnItemClickListener onItemClickListener){
        this.userModelList = userModels;
        this.getUserModelListFilter = userModels;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public UserAdapter.UserAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false);
        UserAdapter.UserAdapterVh holder = new UserAdapter.UserAdapterVh(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserAdapterVh holder, int position) {
        holder.itemView.setTag(position);

        holder.userCompany.setText(userModelList.get(position).getP_company());
        holder.userName.setText(userModelList.get(position).getP_name());
        Glide.with(holder.itemView).load(userModelList.get(position).getP_logo()).into(holder.userLogo);

        // 아이템 클릭 이벤트 설정
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAbsoluteAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                List_User _userModel = userModelList.get(adapterPosition);
                onItemClickListener.onItemClick(_userModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    @Override
    public Filter getFilter() { //검색 후 일치하는 리스트만 보여주기 위한 메소드
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if(constraint == null || constraint.length() == 0){
                    filterResults.values = getUserModelListFilter;
                    filterResults.count = getUserModelListFilter.size();
                }else {
                    String searchStr = constraint.toString().toLowerCase();
                    List<List_User> userModels = new ArrayList<>();
                    for(List_User userModel: getUserModelListFilter){
                        if(userModel.getP_name() != null && userModel.getP_address() != null && userModel.getP_company() != null) {
                            if (userModel.getP_name().toLowerCase().contains(searchStr) ||
                                    userModel.getP_company().toLowerCase().contains(searchStr)) {
                                userModels.add(userModel);
                            }
                        }
                    }
                    filterResults.values = userModels;
                    filterResults.count = userModels.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userModelList = (List<List_User>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public static class UserAdapterVh extends RecyclerView.ViewHolder{
        private TextView userName;
        private ImageView userLogo;
        private TextView userCompany;
        public UserAdapterVh(@NonNull View itemView){
            super(itemView);
            userName = itemView.findViewById(R.id.tvUserName);
            userLogo = itemView.findViewById(R.id.rlImage);
            userCompany = itemView.findViewById(R.id.tvUserCompany);
        }
    }
}
