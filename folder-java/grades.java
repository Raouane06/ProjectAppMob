package com.example.labb1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
        
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_grades) {
                return true;
            }
            else if (itemId == R.id.nav_calculator) {// switch to calcuate
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new calculate())
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            else if (itemId == R.id.nav_profile) { // switch to profile
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new profile())
                        .addToBackStack(null)
                        .commit();
                return true;
            }

            return false;
        });

        return view;
    }
}
