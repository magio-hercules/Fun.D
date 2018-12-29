package study.easycalendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import study.easycalendar.AppUtil;
import study.easycalendar.DetailActivity;
import study.easycalendar.R;
import study.easycalendar.databinding.ItemScheduleBinding;
import study.easycalendar.model.Schedule;
import study.easycalendar.model.ScheduleViewModel;

import static study.easycalendar.App.getApplicationInstance;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private List<Schedule> schedules;
    private Context context;

    public CalendarAdapter() {
        schedules = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemScheduleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_schedule, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(schedules.get(position));
    }


    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public void setData(List<Schedule> newSchedules) {
        if (schedules != null) {
            ScheduleDiffCallback scheduleDiffCallback = new ScheduleDiffCallback(schedules, newSchedules);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(scheduleDiffCallback);

            schedules.clear();
            schedules.addAll(newSchedules);
            diffResult.dispatchUpdatesTo(this);
        } else {
            // first initialization
            schedules = newSchedules;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ScheduleViewModel.ScheduleNavigator {

        private final ItemScheduleBinding binding;
        private Schedule schedule;


        public ViewHolder(ItemScheduleBinding binding) {
            super(binding.itemSchedule);
            this.binding = binding;
        }

        void bind(final Schedule schedule) {
            if (schedule != null) {
                this.schedule = schedule;
                binding.startDate.setText(AppUtil.getStringByLocalTime(schedule.getStartTime(), "HH:mm:ss"));
                binding.title.setText(schedule.getTitle());
                binding.memo.setText(schedule.getMemo());
            }

            if (binding.getViewModel() == null) {
                ScheduleViewModel scheduleViewModel = new ScheduleViewModel(getApplicationInstance());
                binding.setViewModel(scheduleViewModel);
                scheduleViewModel.setNavigator(this);
                scheduleViewModel.setSchedule(schedule);
            } else {
                binding.getViewModel().setSchedule(schedule);
            }
        }

        @Override
        public void startDetailActivity() {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id", schedule.id);
            context.startActivity(intent);
        }

        @Override
        public void showDeleteDialog() {
            new AlertDialog.Builder(context)
                    .setTitle("삭제")
                    .setMessage("삭제하시겠습니까?")
                    .setPositiveButton("삭제", (dialog, which) -> binding.getViewModel().deleteSchedule())
                    .setNegativeButton("취소", null)
                    .show();
        }

    }

    class ScheduleDiffCallback extends DiffUtil.Callback {

        private final List<Schedule> oldSchedules, newSchedules;

        public ScheduleDiffCallback(List<Schedule> oldSchedules, List<Schedule> newSchedules) {
            this.oldSchedules = oldSchedules;
            this.newSchedules = newSchedules;
        }

        @Override
        public int getOldListSize() {
            return oldSchedules.size();
        }

        @Override
        public int getNewListSize() {
            return newSchedules.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldSchedules.get(oldItemPosition).getId() == newSchedules.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldSchedules.get(oldItemPosition).equals(newSchedules.get(newItemPosition));
        }
    }


}
