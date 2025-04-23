package com.example.labb1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class Schedule extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule, container, false);

        // Handle back button
        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Setup week tabs
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
        for (String day : days) {
            tabLayout.addTab(tabLayout.newTab().setText(day));
        }

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ScheduleItem> scheduleItems = getSampleSchedule(tabLayout.getSelectedTabPosition());
        ScheduleAdapter adapter = new ScheduleAdapter(scheduleItems);
        recyclerView.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateScheduleForDay(tab.getPosition());
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void updateScheduleForDay(int dayIndex) {
        if (getView() == null) return;

        RecyclerView recyclerView = getView().findViewById(R.id.scheduleRecyclerView);
        List<ScheduleItem> scheduleItems = getSampleSchedule(dayIndex);
        ScheduleAdapter adapter = new ScheduleAdapter(scheduleItems);
        recyclerView.setAdapter(adapter);
    }

    private List<ScheduleItem> getSampleSchedule(int dayIndex) {
        List<ScheduleItem> items = new ArrayList<>();

        // Sample data - in a real app, this would come from a database
        switch (dayIndex) {
            case 0: // Sunday
                items.add(new ScheduleItem("Cours DSS", "08:00", "09:30", "Amphi j", "Prof. Meadi"));
                items.add(new ScheduleItem("TP DSS", "09:40", "11:10", "SM3", "Prof. Meadi"));
                items.add(new ScheduleItem("TD SecInf", "13:10", "14:40", "S2-A", "Prof. Sahraoui"));
                items.add(new ScheduleItem("Cours AppMob", "14:50", "16:20", "Amphi 1 UFC", "Prof. Zouai"));
                break;
            case 1: // Monday
                items.add(new ScheduleItem("Cours RS", "09:40", "11:10", "Amphi 4 UFC", "Prof. Telli"));
                items.add(new ScheduleItem("TP AppMob", "11:20", "12:50", "B4", "Prof. Dakhia"));
                break;
            case 2: // Monday
                items.add(new ScheduleItem("TP IA", "08:00", "09:30", "SM8", "Prof. Mokhtari"));
                items.add(new ScheduleItem("Cours IA", "09:40", "11:10", "Amphi j", "Prof. Mokhtari"));
                items.add(new ScheduleItem("Cours SecInf", "13:10", "14:40", "Amphi j", "Prof. Sahraoui"));
                break;
            default:
                items.add(new ScheduleItem("No classes", "", "", "", ""));
        }

        return items;
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