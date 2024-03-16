package com.example.appqlsv.ActivityUser.Fragment.Cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter.AdapterPaper;
import com.example.appqlsv.R;
import com.google.android.material.tabs.TabLayout;

public class CartUser extends Fragment {
    private ViewPager viewPagerCart;
    private TabLayout tablayerCart;
    private RecyclerView rv_cart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_cart, container, false);
        anhxaid(view);
        AdapterPaper adapterPaper= new AdapterPaper(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,getContext());
        viewPagerCart.setAdapter(adapterPaper);
        tablayerCart.setupWithViewPager(viewPagerCart);
        return view;
    }


    private void anhxaid(View view) {
        viewPagerCart = view.findViewById(R.id.viewPagerCart);
        tablayerCart = view.findViewById(R.id.tablayerCart);
        rv_cart = view.findViewById(R.id.rv_cart);
    }
}
