package com.example.doctorsearchapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doctorsearchapp.LoginActivity;
import com.example.doctorsearchapp.MainActivity;
import com.example.doctorsearchapp.R;
import com.example.doctorsearchapp.models.Users;
import com.parse.ParseFile;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvUserId;
    private Users currUser;
    private Button logoutBtn;
    private Button btnAddDoctor;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvUserId = view.findViewById(R.id.tvDateJoined);
        logoutBtn = view.findViewById(R.id.btnLogout);
        btnAddDoctor = view.findViewById(R.id.btnAddDoctor);

        currUser = new Users(ParseUser.getCurrentUser());
        ParseFile profileImage = currUser.getProfilePic();

        tvUsername.setText(currUser.getUsername());
        tvUserId.setText(currUser.getProfileCreation());

        if (profileImage != null)
        {
            Glide.with(getContext()).load(profileImage.getUrl()).into(ivProfileImage);
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new BusinessFragment() ).commit();
            }
        });
    }

    private void signOut() {
        ParseUser.logOut();
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
    }
}