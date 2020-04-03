package com.fund.iam.ui.main.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.R;

public class SearchListUserFilterAdapter extends RecyclerView.Adapter<SearchListUserFilterAdapter.ViewHoder> {

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_filter_view,parent,false);

        return new SearchListUserFilterAdapter.ViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {

        public final TextView tv_filtername;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);

            tv_filtername = itemView.findViewById(R.id.tv_filtername);
        }
    }
}
