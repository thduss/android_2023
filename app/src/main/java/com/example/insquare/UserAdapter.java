package com.example.insquare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterVh> implements Filterable {

    public List<UserModel> userModelList = new ArrayList<>();
    public List<UserModel> getUserModelListFilter = new ArrayList<>();
    public Context context;

    // 아이템 클릭 리스너 인터페이스 추가
    public interface OnItemClickListener {
        void onItemClick(UserModel userModel);
    }

    private final OnItemClickListener onItemClickListener;

    public UserAdapter(List<UserModel> userModels, Context context, OnItemClickListener onItemClickListener){
        this.userModelList = userModels;
        this.getUserModelListFilter = userModels;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public UserAdapter.UserAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false);
        return new UserAdapterVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserAdapterVh holder, int position) {
        /*holder.itemView.setTag(position);

        holder.userCompany.setText(userModelList.get(position).getP_company());
        holder.userName.setText(userModelList.get(position).getP_name());*/

        UserModel userModel = userModelList.get(position);
        String firstName = userModel.getFirstName();
        String lastName = userModel.getLastName();
        String userCompanyName = userModel.getP_company();
        String userName = lastName + firstName;
        //String userName = userModel.getP_name();
        //String prefix = firstName.charAt(0) + " " + lastName.charAt(0);
        //이름이 한글/영어 따라 이미지 text 다르게 - 이거로 써야한다면 (일단은 다 한글이라고 가정)

        String prefix = firstName;
        //String prefix = String.valueOf(userName.charAt(1)+userName.charAt(2));

        holder.userCompany.setText(userCompanyName);
        holder.userName.setText(userName);
        holder.userPrefix.setText(prefix);

        // 아이템 클릭 이벤트 설정
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                UserModel _userModel = userModelList.get(adapterPosition);
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
                    List<UserModel> userModels = new ArrayList<>();
                    for(UserModel userModel: getUserModelListFilter){
                        if(userModel.getFirstName().toLowerCase().contains(searchStr) ||
                                userModel.getLastName().toLowerCase().contains(searchStr) ||
                                userModel.getP_company().toLowerCase().contains(searchStr)){
                            userModels.add(userModel);
                        }
                        /*if(userModel.getP_name().toLowerCase().contains(searchStr) ||
                                userModel.getP_company().toLowerCase().contains(searchStr)){
                            userModels.add(userModel);
                        }*/
                    }
                    filterResults.values = userModels;
                    filterResults.count = userModels.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userModelList = (List<UserModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public static class UserAdapterVh extends RecyclerView.ViewHolder{
        private TextView userName;
        private TextView userPrefix;
        private TextView userCompany;
        public UserAdapterVh(@NonNull View itemView){
            super(itemView);
            userName = itemView.findViewById(R.id.tvUserName);
            userPrefix = itemView.findViewById(R.id.tvPrefix);
            userCompany = itemView.findViewById(R.id.tvUserCompany);
        }
    }
}
