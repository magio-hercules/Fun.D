package com.fund.iam.ui.main.channel;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.R;
import com.fund.iam.data.model.ChannelUser;
import com.fund.iam.data.model.User;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChannelUserListAdapter extends RecyclerView.Adapter<ChannelUserListAdapter.ViewHolder> {

    private List<User> usersModel = new ArrayList<>();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);

        return new ChannelUserListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        switch (usersModel.get(position).getJobList()) {
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

        holder.tv_userName.setText(usersModel.get(position).getUserName());
        holder.tv_email.setText(usersModel.get(position).getEmail());

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
        return usersModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tv_email;
        public final TextView tv_userName;
        public final TextView tv_jobList;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            tv_email = itemView.findViewById(R.id.tv_email);
            tv_userName = itemView.findViewById(R.id.tv_userName);
            tv_jobList = itemView.findViewById(R.id.tv_jobList);

        }
    }

    public void setModel_User(List<User> userModel) {
        this.usersModel = userModel;
    }
}
