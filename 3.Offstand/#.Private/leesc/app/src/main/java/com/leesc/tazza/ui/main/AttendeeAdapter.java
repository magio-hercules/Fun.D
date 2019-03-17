package com.leesc.tazza.ui.main;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.leesc.tazza.R;
import com.leesc.tazza.data.model.Attendee;
import com.leesc.tazza.databinding.ItemAttendeeBinding;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.ViewHolder> {

    private List<Attendee> attendees;

    public void setDatas(List<Attendee> attendees) {
        Log.d("lsc","setDatas wifis " + attendees.size());
        AttendeeDiffCallback wifidiffCallback = new AttendeeDiffCallback(this.attendees, attendees);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(wifidiffCallback);
        this.attendees = attendees;
        diffResult.dispatchUpdatesTo(this);
    }

    public AttendeeAdapter() {
        this.attendees = Collections.emptyList();
    }

    @NonNull
    @Override
    public AttendeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAttendeeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_attendee, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeAdapter.ViewHolder holder, int position) {
        holder.bindRepository(attendees.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ItemAttendeeBinding binding;

        ViewHolder(ItemAttendeeBinding binding) {
            super(binding.itemAttendee);
            this.binding = binding;
        }

        void bindRepository(Attendee wifi) {
            if (binding.getAttendee() == null) {
                binding.setAttendee(wifi);
            } else {
                binding.getAttendee();

            }

        }

    }

    class AttendeeDiffCallback extends DiffUtil.Callback {

        private final List<Attendee> oldAttendee, newAttendee;

        AttendeeDiffCallback(List<Attendee> oldAttendee, List<Attendee> newAttendee) {
            this.oldAttendee = oldAttendee;
            this.newAttendee = newAttendee;
        }

        @Override
        public int getOldListSize() {
            return oldAttendee.size();
        }

        @Override
        public int getNewListSize() {
            return newAttendee.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldAttendee.get(oldItemPosition).equals(newAttendee.get(newItemPosition));
        }
    }
}
