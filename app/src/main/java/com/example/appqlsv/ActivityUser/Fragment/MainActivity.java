package com.example.appqlsv.ActivityUser.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appqlsv.ActivityUser.Fragment.Cart.CartUser;
import com.example.appqlsv.ActivityUser.Fragment.Home.HomeUser;
import com.example.appqlsv.ActivityUser.Fragment.chat.MessengerUser;
import com.example.appqlsv.ActivityUser.Fragment.proflle.ProfileUser;
import com.example.appqlsv.R;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private SmoothBottomBar smoothBottomBar;

    public void hideSmoothBottomBar() {
        if (smoothBottomBar != null) {
            smoothBottomBar.setVisibility(View.GONE);
        }
    }

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_MESS = 1;
    private static final int FRAGMENT_CART = 2;
    private static final int FRAGMENT_PROFLIE = 3;
    private int mCurrentFragment = FRAGMENT_HOME;


    public void switchToHomeFragment() {
        replaceFragment(new CartUser());
        mCurrentFragment = FRAGMENT_HOME;
        smoothBottomBar.setItemActiveIndex(FRAGMENT_CART);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhxaid();
        replaceFragment(new HomeUser());
        getWindow().setStatusBarColor(Color.parseColor("#53E88B"));
        EventButton();

    }

    private void EventButton() {
        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (i == 0) {
                    if (mCurrentFragment != FRAGMENT_HOME) {
                        replaceFragment(new HomeUser());
                        mCurrentFragment = FRAGMENT_HOME;
                    }
                } else if (i == 1) {
                    if (mCurrentFragment != FRAGMENT_MESS) {
                        replaceFragment(new MessengerUser());
                        mCurrentFragment = FRAGMENT_MESS;
                    }
                }
                if (i == 2) {
                    if (mCurrentFragment != FRAGMENT_CART) {
                        replaceFragment(new CartUser());
                        mCurrentFragment = FRAGMENT_CART;

                    }
                }
                if (i == 3) {
                    if (mCurrentFragment != FRAGMENT_PROFLIE) {
                        replaceFragment(new ProfileUser());
                        mCurrentFragment = FRAGMENT_PROFLIE;
                    }

                }

                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


    private void anhxaid() {
        smoothBottomBar = findViewById(R.id.bottomNavigation);
    }
}