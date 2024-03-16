package com.example.appqlsv.ActivityUser.Fragment.proflle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appqlsv.Activity.Login;
import com.example.appqlsv.ActivityUser.Fragment.proflle.generasetting.GeneralSetting;
import com.example.appqlsv.ActivityUser.Fragment.proflle.profilsetting.UserProfileActivity;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;
import com.google.android.material.card.MaterialCardView;

public class ProfileUser extends Fragment {
    private MaterialCardView logout,profileSettings,generalSettings;
    private UserSessionManager userSessionManager;
    private TextView userName,userRole;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        intview(view);
        userSessionManager = new UserSessionManager(getActivity());
        userName.setText(userSessionManager.getFullname());
        userRole.setText("Khách hàng");
        eventbutton();
        return view;
    }

    private void eventbutton() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSessionManager = new UserSessionManager(getActivity());
                userSessionManager.clearCredentials();
                if (getActivity() != null) {
                    getActivity().finishAffinity();
                }
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });
        profileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserProfileActivity.class));
            }
        });
        generalSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GeneralSetting.class));
            }
        });
    }

    private void intview(View view) {
        logout = view.findViewById(R.id.logout);
        profileSettings = view.findViewById(R.id.profileSettings);
        userName = view.findViewById(R.id.userName);
        generalSettings = view.findViewById(R.id.generalSettings);
        userRole = view.findViewById(R.id.userRole);
    }
}
