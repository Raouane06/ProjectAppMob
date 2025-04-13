package com.example.labb1;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class profile extends Fragment {
    private static final int EDIT_PROFILE_REQUEST = 100;
    private static final String USER_PREFS = "user_prefs";
    private static final String USERNAME_KEY = "username";

    private database dbHelper;
    private String currentUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        try {
            dbHelper = new database(requireActivity());

            SharedPreferences prefs = requireActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
            currentUsername = prefs.getString(USERNAME_KEY, null);


            if (currentUsername == null || currentUsername.isEmpty()) {
                showLoginPrompt();
                return view;
            }

            loadProfileData(view);
            setupButtons(view);

        } catch (Exception e) {
            Log.e("ProfileFragment", "Error in onCreateView", e);
            Toast.makeText(getActivity(), "Error loading profile", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }

        return view;
    }

    private void showLoginPrompt() {
        Toast.makeText(getActivity(), "Please login first", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        }, 1500);
    }

    @SuppressLint("Range")
    private void loadProfileData(View view) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getUserData(currentUsername);
            if (cursor != null && cursor.moveToFirst()) {
                // Bind data to views
                TextView profileName = view.findViewById(R.id.profile_username);
                profileName.setText(cursor.getString(cursor.getColumnIndex(database.USERNAME)));

                TextView profileEmail = view.findViewById(R.id.profile_email);
                profileEmail.setText(cursor.getString(cursor.getColumnIndex(database.EMAIL)));

            } else {
                Toast.makeText(getActivity(), "User  data not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error loading data", e);
            Toast.makeText(getActivity(), "Error loading data", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.edit_profile_button).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("username", currentUsername);
            startActivityForResult(intent, EDIT_PROFILE_REQUEST);
        });

        view.findViewById(R.id.logout_button).setOnClickListener(v -> {
            SharedPreferences.Editor editor = requireActivity()
                    .getSharedPreferences(USER_PREFS, MODE_PRIVATE)
                    .edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroyView();
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Password");

        View dialogView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_change_password, null);

        EditText currentPassword = dialogView.findViewById(R.id.current_password);
        EditText newPassword = dialogView.findViewById(R.id.new_password);
        EditText confirmPassword = dialogView.findViewById(R.id.confirm_password);

        builder.setView(dialogView);
        builder.setPositiveButton("Change", (dialog, which) -> {
            String currentPass = currentPassword.getText().toString();
            String newPass = newPassword.getText().toString();
            String confirmPass = confirmPassword.getText().toString();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(getActivity(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkUserCredentials(currentUsername, currentPass)) {
                boolean updated = dbHelper.updatePassword(currentUsername, newPass);
                if (updated) {
                    Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    @Override
    public void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}
