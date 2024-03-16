package com.example.appqlsv.Admin.Fragment.QuanLyNhanVien;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.appqlsv.Admin.Fragment.QuanLyNhanVien.FragmentAdapterPaper.QLNV;
import com.example.appqlsv.R;
import com.google.android.material.tabs.TabLayout;

public class QuanLyNhanVienFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tablayerDetailPhong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ql_nhanvien, container, false);

        andxaid(view);
        QLNV qlnv= new QLNV(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,getContext());
        viewPager.setAdapter(qlnv);
        tablayerDetailPhong.setupWithViewPager(viewPager);
        return view;
    }

    private void andxaid(View view) {
        viewPager= view.findViewById(R.id.viewPager);
        tablayerDetailPhong= view.findViewById(R.id.tablayerDetailPhong);
    }
}
