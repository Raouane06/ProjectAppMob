package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private final ArrayList<String> announcements;

    public AnnouncementAdapter(ArrayList<String> announcements) {
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
        holder.textView.setText(announcements.get(position));
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        AnnouncementViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvAnnouncement);
        }
    }
}
