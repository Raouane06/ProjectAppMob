package com.example.labb1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Announcements extends Fragment {
    private AnnouncementDBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.announcements, container, false);

        try {
            // Initialize database
            dbHelper = new AnnouncementDBHelper(requireActivity());

            // Setup back button
            view.findViewById(R.id.btnBackToMain).setOnClickListener(v -> {
                requireActivity().onBackPressed();
            });

            // Setup RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.announcements_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Get data from database
            List<Announcement> dbAnnouncements = dbHelper.getAllAnnouncements();
            List<AnnouncementItem> items = new ArrayList<>();

            for (Announcement announcement : dbAnnouncements) {
                items.add(new AnnouncementItem(
                        announcement.getTitle(),
                        announcement.getContent(),
                        announcement.getDate(),
                        announcement.getAuthor()
                ));
            }

            // Set adapter
            AnnouncementAdapter adapter = new AnnouncementAdapter(items);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("Announcements", "Loading error", e);
            Toast.makeText(getActivity(), "Failed to load announcements: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroyView();
    }
}
