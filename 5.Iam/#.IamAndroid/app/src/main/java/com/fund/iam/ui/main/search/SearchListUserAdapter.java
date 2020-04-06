package com.fund.iam.ui.main.search;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.R;
import com.fund.iam.data.model.User;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class SearchListUserAdapter extends RecyclerView.Adapter<SearchListUserAdapter.ViewHolder> {

    private List<User> usersModel = new ArrayList<>();
    private List<User> usersModel_filterKeyword = new ArrayList<>();
    String KeyWord;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);

        return new SearchListUserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        switch (usersModel_filterKeyword.get(position).getJobList()) {
            case "0":
                holder.tv_jobList.setText("기획자");
                holder.tv_jobList.setBackgroundColor(Color.parseColor("#4BDFF3"));
                break;

            case "1":
                holder.tv_jobList.setText("디자이너");
                holder.tv_jobList.setBackgroundColor(Color.parseColor("#F34B4B"));
                break;

            case "2":
                holder.tv_jobList.setText("개발자");
                holder.tv_jobList.setBackgroundColor(Color.parseColor("#4EF34B"));
                break;
            default:
                holder.tv_jobList.setText("");

        }

        holder.tv_userName.setText(usersModel_filterKeyword.get(position).getUserName());
        holder.tv_email.setText(usersModel_filterKeyword.get(position).getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(),position+"",Toast.LENGTH_SHORT).show();
                Logger.i("클릭"+position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersModel_filterKeyword.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tv_email;
        public final TextView tv_userName;
        public final TextView tv_jobList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_email = itemView.findViewById(R.id.tv_email);
            tv_userName = itemView.findViewById(R.id.tv_userName);
            tv_jobList = itemView.findViewById(R.id.tv_jobList);
        }
    }

    public void setModel_Users(List<User> usersModel) {
        this.usersModel = usersModel;
    }

    public void setKeyWord(String keyWord) {
        this.KeyWord = keyWord;
        usersModel_filterKeyword.removeAll(usersModel_filterKeyword);
        for (int i=0; i<usersModel.size(); i++) {
            if(usersModel.get(i).getUserName().contains(keyWord.toUpperCase())) {
                usersModel_filterKeyword.add(usersModel.get(i));
            }
        }
        notifyDataSetChanged();
    }
}
