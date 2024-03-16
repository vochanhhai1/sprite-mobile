package com.example.appqlsv.Admin.Fragment.QuanLyNhanVien;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuanLyNhanVien_Unenable extends Fragment {
    private ArrayList<Users> users;
    private AdapterQLNV adapterQLNV;
    private RecyclerView rcfQLND_unenable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ql_nhanvien_unenable, container, false);
        anhxa(view);

        users = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rcfQLND_unenable.setLayoutManager(gridLayoutManager);

        adapterQLNV = new AdapterQLNV(getActivity(), users);
        rcfQLND_unenable.setAdapter(adapterQLNV);
        GetDataQLND();
        registerForContextMenu(rcfQLND_unenable);

        return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                UpdateNhanvien();
                adapterQLNV.notifyDataSetChanged();
                return true;
            case 2:
                // Handle option 2
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void UpdateNhanvien()
    {
        String url = "https://spriteee.000webhostapp.com/updateAdminstatusQLNV.php";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.trim().equals("success")) {
                Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                GetDataQLND();
            } else {
                Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(getActivity(),"Xảy ra lỗi",Toast.LENGTH_SHORT).show();
            Log.d("AAA","lỖI!\n"+error.toString());
        }
        ) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                for (Users users1 : users)
                {
                    params.put("id_khachhang", String.valueOf(users1.getId_khachhang()));
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void GetDataQLND() {
        String url = "https://spriteee.000webhostapp.com/getNV_unenable.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    users.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            Users user = new Users(object.getInt("id_khachhang"),
                                    object.getString("fullname"),
                                    object.getString("email"),
                                    object.getString("password"),
                                    object.getString("sodienthoai"),
                                    object.getString("diachi"),
                                    object.getInt("is_admin"),
                                    object.getInt("status")==1);
                            users.add(user);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    adapterQLNV.notifyDataSetChanged();
                }, error -> Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        GetDataQLND();
    }

    private void anhxa(View view) {
        rcfQLND_unenable = view.findViewById(R.id.rcfQLND_unenable);
    }
}

