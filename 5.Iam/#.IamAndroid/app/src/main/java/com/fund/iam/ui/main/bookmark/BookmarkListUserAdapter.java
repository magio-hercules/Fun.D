package com.fund.iam.ui.main.bookmark;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fund.iam.R;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.User;
import com.fund.iam.ui.letter.LetterActivity;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkListUserAdapter extends RecyclerView.Adapter<BookmarkListUserAdapter.ViewHolder> {

    private List<User> usersModel = new ArrayList<>();
    private List<Job> jobsModel = new ArrayList<>();

    private Context mContext;

    @NonNull
    @Override
    public BookmarkListUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);

        return new BookmarkListUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkListUserAdapter.ViewHolder holder, int position) {
        Logger.d("test " + usersModel.get(position).getImageUrl());
        Glide.with(holder.imageView.getContext())
                .load(usersModel.get(position).getImageUrl())
                .placeholder(R.drawable.user_icon)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imageView);

        holder.tv_jobList.setText("");

        for (int i=0; i< jobsModel.size(); i++) {

            if (usersModel.get(position).getJobList() != null) {
                if (Integer.parseInt(usersModel.get(position).getJobList()) == jobsModel.get(i).getId()) {
                    holder.tv_jobList.setText(jobsModel.get(i).getName());
                    holder.iv_jobList.setColorFilter(Color.parseColor(jobsModel.get(i).getColor()), PorterDuff.Mode.SRC_IN);
                }
            }
        }

        holder.tv_userName.setText(usersModel.get(position).getNickName());
        holder.tv_email.setText(usersModel.get(position).getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(),position+"",Toast.LENGTH_SHORT).show();
                Logger.i("클릭"+position);

                User userInfo = usersModel.get(position);
                String email = userInfo.getEmail();
                String type = userInfo.getSnsType();
                Log.d("Search", "email : " + email + ", sns_type : " + type);

                BookmarkFragmentDirections.ActionNavigationBookmarkToNavigationHome action = BookmarkFragmentDirections.actionNavigationBookmarkToNavigationHome();
                action.setArgProfileEmail(email);
                action.setArgProfileType(type);
                Navigation.findNavController(view).navigate(action);
            }
        });

        holder.iv_letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LetterBoxBus.getInstance().sendLetterBox(new LetterBox(usersModel.get(position).getId(),usersModel.get(position).getUserName(),usersModel.get(position).getImageUrl(),usersModel.get(position).getToken()));
                LetterActivity.start(mContext);
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
        notifyDataSetChanged();
    }
}
