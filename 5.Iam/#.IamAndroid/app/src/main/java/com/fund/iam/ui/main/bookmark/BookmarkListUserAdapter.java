package com.fund.iam.ui.main.bookmark;

import android.content.Context;
import android.graphics.Color;
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

import com.fund.iam.R;
import com.fund.iam.data.bus.LetterBoxBus;
import com.fund.iam.data.model.Job;
import com.fund.iam.data.model.LetterBox;
import com.fund.iam.data.model.User;
import com.fund.iam.ui.letter.LetterActivity;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class BookmarkListUserAdapter extends RecyclerView.Adapter<BookmarkListUserAdapter.ViewHolder> {

    private List<User> usersModel = new ArrayList<>();
    private List<Job> jobsModel = new ArrayList<>();

    private Context mContext;

    @NonNull
    @Override
    public BookmarkListUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_bookmarked_user_list, parent, false);

        return new BookmarkListUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkListUserAdapter.ViewHolder holder, int position) {

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

        holder.tv_name.setText(usersModel.get(position).getNickName());
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

        holder.iv_email.setOnClickListener(new View.OnClickListener() {
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

        public final ImageView imageView;
        public final ImageView iv_email;
        public final TextView tv_name;
        public final TextView tv_email;
        public final TextView tv_jobList;
        public final TextView tv_messageCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            iv_email = itemView.findViewById(R.id.iv_email);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_jobList = itemView.findViewById(R.id.tv_jobList);
            tv_messageCount = itemView.findViewById(R.id.tv_messageCount);
        }
    }

    public void setModel_Users(List<User> usersModel, List<Job> jobsModel) {
        this.usersModel = usersModel;
        this.jobsModel = jobsModel;
        notifyDataSetChanged();
    }
}
