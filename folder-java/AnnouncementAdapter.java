package com.example.labb1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private List<AnnouncementItem> announcements;

    public AnnouncementAdapter(List<AnnouncementItem> announcements) {
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_announcement, parent, false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        AnnouncementItem item = announcements.get(position);
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.date.setText(item.getDate());
        holder.sender.setText("From: " + item.getSender());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, date, sender;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.announcement_title);
            content = itemView.findViewById(R.id.announcement_content);
            date = itemView.findViewById(R.id.announcement_date);
            sender = itemView.findViewById(R.id.announcement_sender);
        }
    }
}
