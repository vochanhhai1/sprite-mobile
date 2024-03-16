package com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.appqlsv.ActivityUser.Fragment.Cart.Mycart;
import com.example.appqlsv.ActivityUser.Fragment.Cart.Myorder;
import com.example.appqlsv.R;

public class AdapterPaper extends FragmentPagerAdapter {
    private final Context context;
    public AdapterPaper(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new Myorder();
        }
        return new Mycart();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title ="";

        switch (position)
        {
            case 0:
                title =context.getResources().getString(R.string.mycart);
                break;
            case 1:
                title =context.getResources().getString(R.string.myorder);
                break;

        }
        return title;
    }
}
