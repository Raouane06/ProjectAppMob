package com.example.labb1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Announcements extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.announcements, container, false);

        // Handle back button
        view.findViewById(R.id.btnBackToMain).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.announcements_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<AnnouncementItem> announcements = getSampleAnnouncements();
        AnnouncementAdapter adapter = new AnnouncementAdapter(announcements);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<AnnouncementItem> getSampleAnnouncements() {
        List<AnnouncementItem> announcements = new ArrayList<>();
        announcements.add(new AnnouncementItem(
                "Exam Schedule Update",
                "The final exam schedule has been updated. Please check the new dates.",
                "2023-06-15",
                "Department Head"
        ));
        announcements.add(new AnnouncementItem(
                "Library Closure",
                "The library will be closed this weekend for maintenance.",
                "2023-06-10",
                "University Administration"
        ));
        return announcements;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisibility(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisibility(true);
        }
    }
}