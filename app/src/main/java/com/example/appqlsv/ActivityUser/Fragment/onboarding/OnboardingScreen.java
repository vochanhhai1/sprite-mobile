package com.example.appqlsv.ActivityUser.Fragment.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.appqlsv.Activity.Login;
import com.example.appqlsv.R;

public class OnboardingScreen extends AppCompatActivity {

    private TextView backBtn,btnSkip;
    private FragmentContainerView wrapperB;
    private RadioGroup radioGroup;
    private RadioButton radio0,radio1,radio2;
    private RelativeLayout bottom;
    private AppCompatButton nextBtn,loginBtn;
    private int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding_screen);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        intview();
        setCurrentFragment1();
        eventbutton();
    }

    private void eventbutton() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backFunc();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFunc();
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLoginScreen();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLoginScreen();
            }
        });
    }
    private void nextFunc() {
        position++;
        if (position != 0)
            backBtn.setVisibility(View.VISIBLE);
        if (position == 2) {
            nextBtn.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
        setCurrentFragment1();
        updateIndicator();
    }

    private void backFunc() {
        position--;
        if (position == 0)
            backBtn.setVisibility(View.GONE);
        if (position != 2) {
            nextBtn.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        setCurrentFragment2();
        updateIndicator();
    }
    private void updateIndicator() {
        radio0.setBackgroundResource(R.drawable.radio_button_uncheck);
        radio1.setBackgroundResource(R.drawable.radio_button_uncheck);
        radio2.setBackgroundResource(R.drawable.radio_button_uncheck);

        switch (position) {
            case 0:
                radio0.setBackgroundResource(R.drawable.radio_button_check);
                break;
            case 1:
                radio1.setBackgroundResource(R.drawable.radio_button_check);
                break;
            case 2:
                radio2.setBackgroundResource(R.drawable.radio_button_check);
                break;
        }
    }

    private void setCurrentFragment1() {
        Fragment fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                fragment = new Boarding_1();
                break;
            case 1:
                fragment = new Boarding_2();
                break;
            case 2:
                fragment = new Boarding_3();
                break;
            default:
                fragment = new Fragment();
                break;
        }

        transaction.setCustomAnimations(
                R.anim.enter_ltr,
                R.anim.exit_ltr,
                R.anim.enter_rtl,
                R.anim.exit_rtl
        );

        transaction.addToBackStack(null);
        transaction.replace(R.id.wrapperB, fragment);
        transaction.commit();
    }
    private void setCurrentFragment2() {
        Fragment fragment = new Fragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (position) {
            case 0:
                fragment = new Boarding_1();
                break;
            case 1:
                fragment = new Boarding_2();
                break;
            case 2:
                fragment = new Boarding_3();
                break;
        }

        transaction.setCustomAnimations(
                R.anim.enter_ltr,
                R.anim.exit_ltr,
                R.anim.enter_rtl,
                R.anim.exit_rtl
        );

        transaction.addToBackStack(null);
        transaction.replace(R.id.wrapperB, fragment);
        transaction.commit();
    }
    private void toLoginScreen() {
        Intent i = new Intent(this, Login.class);
        finishAffinity();
        startActivity(i);
        finish();
    }

    private void intview() {
        backBtn = findViewById(R.id.backBtn);
        btnSkip = findViewById(R.id.btnSkip);
        wrapperB = findViewById(R.id.wrapperB);
        radioGroup = findViewById(R.id.radioGroup);
        radio0 = findViewById(R.id.radio0);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        bottom = findViewById(R.id.bottom);
        nextBtn = findViewById(R.id.nextBtn);
        loginBtn = findViewById(R.id.loginBtn);
    }
}