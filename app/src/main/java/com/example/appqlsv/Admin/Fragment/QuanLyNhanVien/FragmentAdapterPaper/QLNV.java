package com.example.appqlsv.Admin.Fragment.QuanLyNhanVien.FragmentAdapterPaper;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Admin.Fragment.QuanLyNhanVien.QuanLyNhanVien_Enable;
import com.example.appqlsv.Admin.Fragment.QuanLyNhanVien.QuanLyNhanVien_Unenable;

import org.json.JSONException;
import org.json.JSONObject;

public class QLNV extends FragmentPagerAdapter {
    private int count_enable;
    private int count_unenable;
    private Context context;
    private final String url1 = "https://spriteee.000webhostapp.com/countNV_enable.php";
    private final String url2 = "https://spriteee.000webhostapp.com/countNV_unenable.php";
    public QLNV(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        getCount_enable(url1);
        getCount_unenable(url2);
        switch (position)
        {
            case 0:
                return new QuanLyNhanVien_Enable();
            case 1:
                return new QuanLyNhanVien_Unenable();
            default:
                return new QuanLyNhanVien_Enable();
        }
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
                title ="Đã kích hoạt \n" + count_enable;
                break;
            case 1:
                title ="Chưa kích hoạt \n" + count_unenable;
                break;

        }
        return title;
    }

    private void getCount_enable(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        count_enable = jsonObject.getInt("count");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    notifyDataSetChanged();
                }, error -> Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show());
        requestQueue.add(jsonArrayRequest);
    }

    private void getCount_unenable(String url)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url2, null,
                response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        count_unenable = jsonObject.getInt("count1");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    notifyDataSetChanged();
                }, error -> Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show());
        requestQueue.add(jsonArrayRequest);
    }
}
