package com.fund.iam.ui.main.search;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fund.iam.R;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.Location;
import com.fund.iam.data.model.User;
import com.fund.iam.ui.letter.LetterActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchListUserAdapter extends RecyclerView.Adapter<SearchListUserAdapter.ViewHolder> {

    private List<User> usersModel = new ArrayList<>();
    private List<Job> jobsModel = new ArrayList<>();
    private List<Location> locationsModel = new ArrayList<>();

    private List<User> usersModel_filter = new ArrayList<>();
    private List<User> usersModel_filter_result = new ArrayList<>();
    public List<Integer> user_filters = new ArrayList<>();
    private List<User> usersModel_filterKeyword = new ArrayList<>();

    String KeyWord;
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);

        return new SearchListUserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Logger.d("onBindViewHolder position " + position + ", " + usersModel.get(position).getImageUrl());
        Glide.with(mContext)
                .load(usersModel.get(position).getImageUrl())
                .placeholder(R.drawable.user_icon)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);

        holder.tv_jobList.setText("");
        holder.iv_jobList.setVisibility(View.INVISIBLE);

        for (int i = 0; i < jobsModel.size(); i++) {

            if (usersModel_filterKeyword.get(position).getJobList() != null) {
                if (Integer.parseInt(usersModel_filterKeyword.get(position).getJobList()) == jobsModel.get(i).getId()) {
                    holder.tv_jobList.setText(jobsModel.get(i).getName());
                    holder.iv_jobList.setColorFilter(Color.parseColor(jobsModel.get(i).getColor()), PorterDuff.Mode.SRC_IN);
                    holder.iv_jobList.setVisibility(View.VISIBLE);
                }
            }
        }

        holder.tv_userName.setText(usersModel_filterKeyword.get(position).getUserName());
        holder.tv_email.setText(usersModel_filterKeyword.get(position).getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("클릭"+position);

                User userInfo = usersModel_filterKeyword.get(position);
                String email = userInfo.getEmail();
                String type = userInfo.getSnsType();
                Log.d("Search", "email : " + email + ", sns_type : " + type);

                SearchFragmentDirections.ActionNavigationHome action = SearchFragmentDirections.actionNavigationHome();
                action.setArgProfileEmail(email);
                action.setArgProfileType(type);
                Navigation.findNavController(v).navigate(action);
            }
        });

        holder.iv_letter.setOnClickListener(v -> {
//            LetterBoxBus.getInstance().sendLetterBox(new LetterBox(usersModel_filterKeyword.get(position).getId(), usersModel_filterKeyword.get(position).getUserName(), usersModel_filterKeyword.get(position).getImageUrl(), usersModel_filterKeyword.get(position).getToken()));
            LetterBoxBus.getInstance().sendLetterBox(new LetterBox(usersModel_filterKeyword.get(position)));
            LetterActivity.start(mContext);
        });

    }

    @Override
    public int getItemCount() {
        return usersModel_filterKeyword.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final CircleImageView imageView;
        public final TextView tv_email;
        public final TextView tv_userName;
        public final TextView tv_jobList;
        public final ImageView iv_jobList;
        public final ImageView iv_letter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_user);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_userName = itemView.findViewById(R.id.tv_userName);
            tv_jobList = itemView.findViewById(R.id.tv_jobList);
            iv_jobList = itemView.findViewById(R.id.iv_jobList);
            iv_letter = itemView.findViewById(R.id.imageView5);
        }
    }

    public void setModel_Users(List<User> usersModel, List<Job> jobsModel) {
        this.usersModel = usersModel;
        this.jobsModel = jobsModel;
        this.usersModel_filter.addAll(usersModel);
        notifyDataSetChanged();
    }

    public void setUserFilter(List<Location> locationsModel, List<Integer> user_filters) {
        this.locationsModel = locationsModel;
        this.user_filters = user_filters;



//        Toast.makeText(mContext,"성공",Toast.LENGTH_SHORT).show();
        usersModel_filter.removeAll(usersModel_filter);
        usersModel_filter_result.removeAll(usersModel_filter_result);

        String location;
        String job;
        int gender;
        int age;

        for (int i = 0; i < this.usersModel.size(); i++) {
            if (user_filters.get(0) != 0) {
                location = this.usersModel.get(i).getLocationList();
                if (location == null || Integer.parseInt(location) != user_filters.get(0)) {
                    Log.d("test" , "location : " + location + ", user_filters.get(0) : " + user_filters.get(0));
                    continue;
                }
            }
            if (user_filters.get(1) != 0) {
                job = usersModel.get(i).getJobList();
                if (job == null || Integer.parseInt(job) != user_filters.get(1)) {
                    Log.d("test" , "job : " + job + ", user_filters.get(1) : " + user_filters.get(1));
                    continue;
                }
            }
            if (user_filters.get(2) != 0) {
                gender = usersModel.get(i).getGender();
                if (gender == -1 || gender != user_filters.get(2) - 1) {
                    Log.d("test" , "gender : " + gender + ", user_filters.get(2) : " + user_filters.get(2));
                    continue;
                }
            }
            if (user_filters.get(3) != 0) {
                age = usersModel.get(i).getAge();
                if (age == -1 || age != user_filters.get(3) - 1) {
                    Log.d("test" , "age : " + age + ", user_filters.get(3) : " + user_filters.get(3));
                    continue;
                }
            }

            usersModel_filter.add(usersModel.get(i));
            usersModel_filter_result.add(usersModel.get(i));
        }


    }




    public void setKeyWord(String keyWord) {
        this.KeyWord = keyWord;
        usersModel_filterKeyword.removeAll(usersModel_filterKeyword);
        for (int i=0; i<usersModel_filter.size(); i++) {
            if(usersModel_filter.get(i).getUserName().toUpperCase().contains(keyWord.toUpperCase())) {
                usersModel_filterKeyword.add(usersModel_filter.get(i));
            }
        }
        notifyDataSetChanged();
    }
}
