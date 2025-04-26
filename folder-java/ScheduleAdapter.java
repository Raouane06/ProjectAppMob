package com.example.labb1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<ScheduleItem> scheduleItems;

    public ScheduleAdapter(List<ScheduleItem> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        ScheduleItem item = scheduleItems.get(position);

        holder.courseName.setText(item.getCourseName());
        holder.timeText.setText(item.getStartTime() + " - " + item.getEndTime());
        holder.roomText.setText(item.getRoom());
        holder.professorText.setText(item.getProfessor());
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, timeText, roomText, professorText;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            timeText = itemView.findViewById(R.id.timeText);
            roomText = itemView.findViewById(R.id.roomText);
            professorText = itemView.findViewById(R.id.professorText);
        }
    }
}
