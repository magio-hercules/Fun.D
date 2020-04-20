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
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Location;
import com.fund.iam.data.model.User;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class SearchListUserAdapter extends RecyclerView.Adapter<SearchListUserAdapter.ViewHolder> {

    private List<User> usersModel = new ArrayList<>();
    private List<Job> jobsModel = new ArrayList<>();
    private List<Location> locationsModel = new ArrayList<>();

    private List<User> usersModel_filter = new ArrayList<>();
    public List<String> user_filters = new ArrayList<>();
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

        holder.tv_jobList.setText("");
        holder.tv_jobList.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

        for (int i=0; i< jobsModel.size(); i++) {

            if (usersModel_filterKeyword.get(position).getJobList() != null) {
                if (Integer.parseInt(usersModel_filterKeyword.get(position).getJobList()) == jobsModel.get(i).getId()) {
                    holder.tv_jobList.setText(jobsModel.get(i).getName());
                    holder.tv_jobList.setBackgroundColor(Color.parseColor(jobsModel.get(i).getColor()));
                }
            }
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

    public void setModel_Users(List<User> usersModel, List<Job> jobsModel) {
        this.usersModel = usersModel;
        this.jobsModel = jobsModel;
        this.usersModel_filter = usersModel;
        notifyDataSetChanged();
    }

    public void setUserFilter(List<Location> locationsModel, List<String> user_filters) {
        this.locationsModel = locationsModel;
        this.user_filters = user_filters;
        usersModel_filter.removeAll(usersModel_filter);
        for (int i=0; i<usersModel.size(); i++) {
            if(usersModel.get(i).getLocationList() == Integer.parseInt(user_filters.get(0))-1) {
                usersModel_filter.add(usersModel.get(i));
            }

        }

    }

    public void setKeyWord(String keyWord) {
        this.KeyWord = keyWord;
        usersModel_filterKeyword.removeAll(usersModel_filterKeyword);
        for (int i=0; i<usersModel.size(); i++) {
            if(usersModel.get(i).getUserName().toUpperCase().contains(keyWord.toUpperCase())) {
                usersModel_filterKeyword.add(usersModel.get(i));
            }
        }
        notifyDataSetChanged();
    }
}
