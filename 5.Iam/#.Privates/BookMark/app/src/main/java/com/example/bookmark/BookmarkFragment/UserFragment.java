package com.example.bookmark.BookmarkFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmark.Adapter.BookmarkUserRecyclerAdapter;
import com.example.bookmark.R;
import com.example.bookmark.model.BookmarkUserItem;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<BookmarkUserItem>bookmarkUserItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = rootView.findViewById(R.id.rv_bookmarkusers);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        bookmarkUserItems.clear();

        bookmarkUserItems.add(new BookmarkUserItem("", "홍순동", "개발자", "hong1_gmail@gmail.com", 0));
        bookmarkUserItems.add(new BookmarkUserItem("", "홍곽동", "기획자", "hong2_gmail@gmail.com", 0));
        bookmarkUserItems.add(new BookmarkUserItem("", "홍열동", "디자이너", "hong3_gmail@gmail.com", 0));
        bookmarkUserItems.add(new BookmarkUserItem("", "홍연동", "개발자", "hong4_gmail@gmail.com", 0));

        BookmarkUserRecyclerAdapter bookmarkUserRecyclerAdapter = new BookmarkUserRecyclerAdapter(bookmarkUserItems);

        recyclerView.setAdapter(bookmarkUserRecyclerAdapter);
        return rootView;
    }
}
