package com.example.labb1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class grades extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grades, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.grades_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load modules
        List<Module> modules = new Module().loadModules(getContext());
        gradeAdapter adapter = new gradeAdapter(modules);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.btnBackToMain).setOnClickListener(v -> {
            if (getActivity() != null) {
                // This ensures proper bottom nav visibility restoration
                getActivity().onBackPressed();
            }
        });

        return view;
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
