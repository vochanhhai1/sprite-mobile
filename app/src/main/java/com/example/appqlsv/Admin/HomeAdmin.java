package com.example.appqlsv.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appqlsv.Admin.Fragment.Home.HomeFragment;
import com.example.appqlsv.Admin.Fragment.QuanLyChat.QuanLyChatFragment;
import com.example.appqlsv.Admin.Fragment.QuanLyDonHang.allorder.QuanLyDonHangFragment;
import com.example.appqlsv.Admin.Fragment.QuanLyMatKhau.QuanLyMatKhauFragment;
import com.example.appqlsv.Admin.Fragment.QuanLyNguoiDung.QuanLyNguoiDungFragment;
import com.example.appqlsv.Admin.Fragment.QuanLyNhanVien.QuanLyNhanVienFragment;
import com.example.appqlsv.Admin.Fragment.QuanLySanPham.QuanLySanPhamFragment;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;
import com.google.android.material.navigation.NavigationView;

public class HomeAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_QLSP = 1;
    private static final int FRAGMENT_QLDH = 2;
    private static final int FRAGMENT_QLND = 3;
    private static final int FRAGMENT_QLNV = 4;
    private static final int FRAGMENT_CHAT = 5;
    private static final int FRAGMENT_MATKHAU = 6;

    private int mCurrentFragment = FRAGMENT_HOME;

    private DrawerLayout mDrawerLayout;

    private TextView tvNameAdmin,tvEmail;
    private View header;

    private NavigationView navigationView;
    public NavigationView getNavigationView() {
        return navigationView;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        anhxa();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        Intent intent = getIntent();
        Users users = (Users) intent.getSerializableExtra("Users");
        header = navigationView.getHeaderView(0);
        tvNameAdmin = header.findViewById(R.id.tvNameAdmin);
        tvEmail = header.findViewById(R.id.tvEmail);

        tvNameAdmin.setText(users.getFullname());
        tvEmail.setText(users.getEmail());
        getWindow().setStatusBarColor(Color.parseColor("#FFBB86FC"));
    }

    private void anhxa() {

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            if (mCurrentFragment !=FRAGMENT_HOME)
            {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(R.string.nav_home); //Thiết lập tiêu đề nếu muốn
                replaceFragment(new HomeFragment());
                mCurrentFragment = FRAGMENT_HOME;
            }
        }else if (id== R.id.nav_sanpham)
        {
            if (mCurrentFragment !=FRAGMENT_QLSP)
            {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(R.string.nav_sanpham); //Thiết lập tiêu đề nếu muốn
                replaceFragment(new QuanLySanPhamFragment());
                mCurrentFragment = FRAGMENT_QLSP;
            }
        }else if (id== R.id.nav_don)
        {
            if (mCurrentFragment !=FRAGMENT_QLDH)
            {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(R.string.nav_don); //Thiết lập tiêu đề nếu muốn
                replaceFragment(new QuanLyDonHangFragment());
                mCurrentFragment = FRAGMENT_QLDH;
            }
        }else if (id== R.id.nav_nguoidung)
        {
            if (mCurrentFragment !=FRAGMENT_QLND)
            {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(R.string.nav_nguoidung); //Thiết lập tiêu đề nếu muốn
                replaceFragment(new QuanLyNguoiDungFragment());
                mCurrentFragment = FRAGMENT_QLND;
            }
        }
        else if (id== R.id.nav_nhanvien)
        {
            if (mCurrentFragment !=FRAGMENT_QLNV)
            {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(R.string.nav_nhanvien); //Thiết lập tiêu đề nếu muốn
                replaceFragment(new QuanLyNhanVienFragment());
                mCurrentFragment = FRAGMENT_QLNV;
            }
        }else if (id== R.id.nav_chat)
        {
            if (mCurrentFragment !=FRAGMENT_CHAT)
            {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(R.string.nav_chat); //Thiết lập tiêu đề nếu muốn
                replaceFragment(new QuanLyChatFragment());
                mCurrentFragment = FRAGMENT_CHAT;
            }
        }else if (id== R.id.nav_matkhau)
        {
            if (mCurrentFragment !=FRAGMENT_MATKHAU)
            {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(R.string.nav_matkhau); //Thiết lập tiêu đề nếu muốn
                replaceFragment(new QuanLyMatKhauFragment());
                mCurrentFragment = FRAGMENT_MATKHAU;
            }
        }else if (id== R.id.nav_thoat)
        {
            finish();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }


}