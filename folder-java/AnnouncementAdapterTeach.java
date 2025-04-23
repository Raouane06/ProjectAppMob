package com.example.labb1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AnnouncementAdapterTeach extends RecyclerView.Adapter<AnnouncementAdapterTeach.AnnouncementViewHolder> {
    private final ArrayList<Announcement> announcements;

    public AnnouncementAdapterTeach(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_announcement_teacher, parent, false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.tvTitle.setText(announcement.getTitle());
        holder.tvContent.setText(announcement.getContent());
        holder.tvDate.setText(announcement.getDate());
        holder.tvAuthor.setText(announcement.getAuthor());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate, tvAuthor;

        AnnouncementViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvAnnouncement);
            tvContent = itemView.findViewById(R.id.tvAnnouncementContent);
            tvDate = itemView.findViewById(R.id.tvAnnouncementDate);
            tvAuthor = itemView.findViewById(R.id.tvAnnouncementAuthor);
        }
    }
}
