package com.example.bookmark.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmark.R;
import com.example.bookmark.model.BookmarkUserItem;

import java.util.ArrayList;

public class BookmarkUserRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<BookmarkUserItem> bookmarkUserItems;

    public BookmarkUserRecyclerAdapter(ArrayList<BookmarkUserItem> bookmarkUserItems) {
        this.bookmarkUserItems = bookmarkUserItems;
    }

    public class BookmarkUserViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_userProfile;
        TextView tv_userName;
        TextView tv_userEmail;
        TextView tv_userJob;
        ImageView iv_userSendedMessage;

        public BookmarkUserViewHolder(View view) {
            super(view);

            iv_userProfile = view.findViewById(R.id.iv_userProfile);
            tv_userName = view.findViewById(R.id.tv_userName);
            tv_userEmail = view.findViewById(R.id.tv_userEmail);
            tv_userJob = view.findViewById(R.id.tv_userJob);
            iv_userSendedMessage = view.findViewById(R.id.iv_userSendedMessage);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_user_recyclerview_item, parent, false);
        return new BookmarkUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookmarkUserViewHolder bookmarkUserViewHolder = (BookmarkUserViewHolder) holder;

        bookmarkUserViewHolder.iv_userProfile.setImageResource(R.drawable.image1);
        bookmarkUserViewHolder.tv_userName.setText(bookmarkUserItems.get(position).getUserName());
        bookmarkUserViewHolder.tv_userEmail.setText(bookmarkUserItems.get(position).getUserEmail());
        bookmarkUserViewHolder.tv_userJob.setText(bookmarkUserItems.get(position).getUserJob());
    }

    @Override
    public int getItemCount() {
        return bookmarkUserItems.size();
    }
}
