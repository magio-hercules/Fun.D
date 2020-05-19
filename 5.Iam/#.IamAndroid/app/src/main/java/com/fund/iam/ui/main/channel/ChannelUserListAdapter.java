package com.fund.iam.ui.main.channel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fund.iam.R;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.model.ChannelUser;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.User;
import com.fund.iam.ui.letter.LetterActivity;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChannelUserListAdapter extends RecyclerView.Adapter<ChannelUserListAdapter.ViewHolder> {

    private List<ChannelUser> usersModel = new ArrayList<>();
    private List<Job> jobsModel = new ArrayList<>();
    private Context mContext;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);

        return new ChannelUserListAdapter.ViewHolder(v);
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

        for (int i=0; i< jobsModel.size(); i++) {

            if (usersModel.get(position).getJobList() != null) {
                if (Integer.parseInt(usersModel.get(position).getJobList()) == jobsModel.get(i).getId()) {
                    holder.tv_jobList.setText(jobsModel.get(i).getName());
                    holder.iv_jobList.setColorFilter(Color.parseColor(jobsModel.get(i).getColor()), PorterDuff.Mode.SRC_IN);
                    holder.iv_jobList.setVisibility(View.VISIBLE);
                }
            }
        }

        holder.iv_letter.setOnClickListener(v -> {
            LetterBoxBus.getInstance().sendLetterBox(new LetterBox(usersModel.get(position).getId(),usersModel.get(position).getUserName(),usersModel.get(position).getImageUrl(),usersModel.get(position).getToken()));
            LetterActivity.start(mContext);
        });


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

        public final CircleImageView imageView;
        public final TextView tv_email;
        public final TextView tv_userName;
        public final TextView tv_jobList;
        public final ImageView iv_jobList;
        public final ImageView iv_letter;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_user);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_userName = itemView.findViewById(R.id.tv_userName);
            tv_jobList = itemView.findViewById(R.id.tv_jobList);
            iv_jobList = itemView.findViewById(R.id.iv_jobList);
            iv_letter = itemView.findViewById(R.id.imageView5);

        }
    }

    public void setModel_User(List<ChannelUser> userModel, List<Job> jobsModel) {
        this.usersModel = userModel;
        this.jobsModel = jobsModel;
        notifyDataSetChanged();
    }
}
