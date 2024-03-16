package com.example.appqlsv.Admin.Fragment.QuanLyNhanVien;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuanLyNhanVien_Enable extends Fragment {

    private RecyclerView rcfQLND_enable;
    private ArrayList<Users> users;
    private AdapterQLNV adapterQLNV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ql_nhanvien_enable, container, false);
        anhxa(view);

        users = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rcfQLND_enable.setLayoutManager(gridLayoutManager);

        adapterQLNV = new AdapterQLNV(getActivity(), users);
        rcfQLND_enable.setAdapter(adapterQLNV);
        GetDataQLND();
        return view;
    }
    private void GetDataQLND() {
        String url = "https://spriteee.000webhostapp.com/getNV_enable.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    users.clear();
                    for (int i = 0; i < response.length(); i++)
                    {
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
                }, error -> Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show());
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        GetDataQLND();
    }

    private void anhxa(View view) {
        rcfQLND_enable = view.findViewById(R.id.rcfQLND_enable);
    }
}
