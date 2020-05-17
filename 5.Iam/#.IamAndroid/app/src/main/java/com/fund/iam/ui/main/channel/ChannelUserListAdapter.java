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
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.User;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChannelUserListAdapter extends RecyclerView.Adapter<ChannelUserListAdapter.ViewHolder> {

    private List<ChannelUser> usersModel = new ArrayList<>();
    private List<Job> jobsModel = new ArrayList<>();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);

        return new ChannelUserListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_jobList.setText("");
        holder.tv_jobList.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

        for (int i=0; i< jobsModel.size(); i++) {

            if (usersModel.get(position).getJobList() != null) {
                if (Integer.parseInt(usersModel.get(position).getJobList()) == jobsModel.get(i).getId()) {
                    holder.tv_jobList.setText(jobsModel.get(i).getName());
                    holder.tv_jobList.setBackgroundColor(Color.parseColor(jobsModel.get(i).getColor()));
                }
            }
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

    public void setModel_User(List<ChannelUser> userModel, List<Job> jobsModel) {
        this.usersModel = userModel;
        this.jobsModel = jobsModel;
        notifyDataSetChanged();
    }
}
