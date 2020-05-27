package com.fund.iam.ui.main.search;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.R;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.Location;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SearchListUserFilterAdapter extends RecyclerView.Adapter<SearchListUserFilterAdapter.ViewHoder> {
    private List<Location> locationsModel = new ArrayList<>();
    private List<Job> jobsModel = new ArrayList<>();
    private String[] spinner_str_user_gender = null;
    private String[] spinner_str_user_age = null;

    // 필터값 순서: 지역, 직종, 성별, 나이대
    public List<Integer> user_filters = new ArrayList<>();
    public List<Integer> user_filters_view = new ArrayList<>();

    Integer ItemCount=0;

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_filter_view,parent,false);

        return new SearchListUserFilterAdapter.ViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

        for (int i = ItemCount; i < user_filters.size() ;i++) {
            if(user_filters.get(i) != 0) {
                switch (ItemCount) {
                    // 지역
                    case 0:
                        holder.cv_filtername.setCardBackgroundColor(Color.parseColor("#FF7070"));
                        holder.tv_filtername.setText(locationsModel.get(user_filters_view.get(position)-1).getName());
                        ItemCount++;
                        break;
                    // 직종
                    case 1:
                        holder.cv_filtername.setCardBackgroundColor(Color.parseColor("#4EF34B"));
                        holder.tv_filtername.setText(jobsModel.get(user_filters_view.get(position)-1).getName());
                        ItemCount++;
                        break;
                    // 성별
                    case 2:
                        holder.cv_filtername.setCardBackgroundColor(Color.parseColor("#B6D7FF"));
                        holder.tv_filtername.setText(spinner_str_user_gender[user_filters_view.get(position)]);
                        ItemCount++;
                        break;
                    // 나이대
                    case 3:
                        holder.cv_filtername.setCardBackgroundColor(Color.parseColor("#FFE2B6"));
                        holder.tv_filtername.setText(spinner_str_user_age[user_filters_view.get(position)]);
                        ItemCount++;
                        break;
                    default:
                }
                break;

            } else {
                ItemCount++;
            }
        }
    }

    @Override
    public int getItemCount() {
        return user_filters_view.size();
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {

        public final TextView tv_filtername;
        public final MaterialCardView cv_filtername;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);

            tv_filtername = itemView.findViewById(R.id.tv_filtername);
            cv_filtername = itemView.findViewById(R.id.cv_filtername);
        }
    }

    public void setUserFilter(List<Location> locationsModel, List<Job> jobsModel, String[] spinner_str_user_gender, String[] spinner_str_user_age, List<Integer> user_filters) {
        this.locationsModel = locationsModel;
        this.jobsModel = jobsModel;
        this.spinner_str_user_gender = spinner_str_user_gender;
        this.spinner_str_user_age = spinner_str_user_age;
        this.user_filters = user_filters;

        user_filters_view.removeAll(user_filters_view);
        for(int i = 0 ; i<user_filters.size(); i++ ) {
            if(user_filters.get(i) != 0) {
                user_filters_view.add(user_filters.get(i));
            }
        }
        ItemCount = 0;
        notifyDataSetChanged();
    }
}
