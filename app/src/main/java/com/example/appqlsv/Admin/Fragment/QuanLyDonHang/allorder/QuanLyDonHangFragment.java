package com.example.appqlsv.Admin.Fragment.QuanLyDonHang.allorder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuanLyDonHangFragment extends Fragment {
    private TextView filterBtn;
    private RecyclerView orderRCV;
    private ProgressBar bar;
    private String currentFilter = "";
    private int userid;
    private ArrayList<Order> listOrder;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private AllOrderAdapter allOrderAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qldh, container, false);
        intview(view);
        UserSessionManager userSessionManager = new UserSessionManager(getActivity());
        userid = userSessionManager.getUserId();
        listOrder = new ArrayList<>();
        orderDetailsArrayList = new ArrayList<>();
        currentFilter = "";
        eventbutton();
        getOrder();
        return view;
    }


    private void eventbutton() {
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo đối tượng AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.Filtertheorders);

                // Tạo danh sách các tùy chọn trạng thái
                final String[] status = {
                        v.getResources().getString(R.string.ALL),
                        v.getResources().getString(R.string.ARRIVED),
                        v.getResources().getString(R.string.PENDING),
                        v.getResources().getString(R.string.PROCESSED),
                        v.getResources().getString(R.string.COMPLETED),
                        v.getResources().getString(R.string.CANCELLED)
                };

                // Thêm danh sách vào AlertDialog
                builder.setItems(status, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                getOrder();
                                currentFilter = "";
                                filterBtn.setText(v.getResources().getString(R.string.ALL));
                                break;
                            case 1:
                                getOrderByStatus("ARRIVED");
                                currentFilter = "ARRIVED";
                                filterBtn.setText(v.getResources().getString(R.string.ARRIVED));
                                break;
                            case 2:
                                getOrderByStatus("PENDING");
                                currentFilter = "PENDING";
                                filterBtn.setText(v.getResources().getString(R.string.PENDING));
                                break;
                            case 3:
                                getOrderByStatus("PROCESSED");
                                currentFilter = "PROCESSED";
                                filterBtn.setText(v.getResources().getString(R.string.PROCESSED));
                                break;
                            case 4:
                                getOrderByStatus("COMPLETED");
                                currentFilter = "COMPLETED";
                                filterBtn.setText(v.getResources().getString(R.string.COMPLETED));
                                break;
                            case 5:
                                getOrderByStatus("CANCELLED");
                                currentFilter = "CANCELLED";
                                filterBtn.setText(v.getResources().getString(R.string.CANCELLED));
                                break;
                        }
                    }
                });

                // Tạo và hiển thị AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void getOrderByStatus(String status) {
        String urlorder = "https://spriteee.000webhostapp.com/getAdminStatusOrder.php?status="+status;
        bar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlorder, null,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            listOrder.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                JSONObject orderObject = jsonObject.getJSONObject("order");
                                Order order = new Order(
                                        orderObject.getInt("idorder"),
                                        orderObject.getInt("totalPrice"),
                                        orderObject.getString("notes"),
                                        orderObject.getInt("isReviewed") == 1,
                                        orderObject.getString("status"),
                                        orderObject.getString("createAt"),
                                        orderObject.getString("updateAt"),
                                        orderObject.getInt("buyer")
                                );
                                JSONObject customerJson = jsonObject.getJSONObject("customer");
                                Users users = new Users(customerJson.getInt("id_khachhang"),
                                        customerJson.getString("fullname"));

                                order.setBuyer(users);
                                JSONArray orderDetailsArray = jsonObject.getJSONArray("orderDetails");
                                ArrayList<OrderDetails> orderDetailsList = new ArrayList<>();
                                for (int j = 0; j < orderDetailsArray.length(); j++) {
                                    JSONObject orderDetailsObject = orderDetailsArray.getJSONObject(j);
                                    JSONObject productObject = orderDetailsObject.getJSONObject("product");
                                    String imageUrl = "https://spriteee.000webhostapp.com/" + productObject.getString("image");
                                    Product product = new Product(
                                            productObject.getInt("id"),
                                            productObject.getString("name"),
                                            productObject.getInt("price"),
                                            imageUrl,
                                            productObject.getString("mota"),
                                            productObject.getString("createAt"),
                                            productObject.getString("deleteAt"),
                                            productObject.getInt("id_danhmuc"),
                                            productObject.getInt("isFavorited")==1
                                    );
                                    OrderDetails orderDetails = new OrderDetails(
                                            orderDetailsObject.getInt("idorderdetail"),
                                            orderDetailsObject.getInt("productId"),
                                            orderDetailsObject.getInt("price"),
                                            orderDetailsObject.getInt("quantity"),
                                            product
                                    );
                                    orderDetailsList.add(orderDetails);
                                    orderDetails.setOrderId(order.getId());
                                }
                                order.setOrderDetails(orderDetailsArrayList);
                                order.setOrderDetails(orderDetailsList);

                                listOrder.add(order);
                                bar.setVisibility(View.GONE);
                            }

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                            orderRCV.setLayoutManager(gridLayoutManager);

                            allOrderAdapter = new AllOrderAdapter(listOrder, getActivity());
                            orderRCV.setAdapter(allOrderAdapter);
                            allOrderAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getOrder() {
        String urlorder = "https://spriteee.000webhostapp.com/getAdminOrder.php";
        bar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlorder, null,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            listOrder.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                JSONObject orderObject = jsonObject.getJSONObject("order");
                                Order order = new Order(
                                        orderObject.getInt("idorder"),
                                        orderObject.getInt("totalPrice"),
                                        orderObject.getString("notes"),
                                        orderObject.getInt("isReviewed") == 1,
                                        orderObject.getString("status"),
                                        orderObject.getString("createAt"),
                                        orderObject.getString("updateAt"),
                                        orderObject.getInt("buyer")
                                );
                                JSONObject customerJson = jsonObject.getJSONObject("customer");
                                Users users = new Users(customerJson.getInt("id_khachhang"),
                                        customerJson.getString("fullname"));

                                order.setBuyer(users);
                                JSONArray orderDetailsArray = jsonObject.getJSONArray("orderDetails");
                                ArrayList<OrderDetails> orderDetailsList = new ArrayList<>();
                                for (int j = 0; j < orderDetailsArray.length(); j++) {
                                    JSONObject orderDetailsObject = orderDetailsArray.getJSONObject(j);
                                    JSONObject productObject = orderDetailsObject.getJSONObject("product");
                                    String imageUrl = "https://spriteee.000webhostapp.com/" + productObject.getString("image");
                                    Product product = new Product(
                                            productObject.getInt("id"),
                                            productObject.getString("name"),
                                            productObject.getInt("price"),
                                            imageUrl,
                                            productObject.getString("mota"),
                                            productObject.getString("createAt"),
                                            productObject.getString("deleteAt"),
                                            productObject.getInt("id_danhmuc"),
                                            productObject.getInt("isFavorited")==1
                                    );
                                    OrderDetails orderDetails = new OrderDetails(
                                            orderDetailsObject.getInt("idorderdetail"),
                                            orderDetailsObject.getInt("productId"),
                                            orderDetailsObject.getInt("price"),
                                            orderDetailsObject.getInt("quantity"),
                                            product
                                    );
                                    orderDetailsList.add(orderDetails);
                                    orderDetails.setOrderId(order.getId());
                                }
                                order.setOrderDetails(orderDetailsArrayList);
                                order.setOrderDetails(orderDetailsList);

                                listOrder.add(order);
                                bar.setVisibility(View.GONE);
                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                            orderRCV.setLayoutManager(gridLayoutManager);

                            allOrderAdapter = new AllOrderAdapter(listOrder, getActivity());
                            orderRCV.setAdapter(allOrderAdapter);
                            allOrderAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentFilter.isEmpty()) {
            getOrder();
        } else {
            getOrderByStatus(currentFilter);
        }
    }


    private void intview(View view) {
        filterBtn= view.findViewById(R.id.filterBtn);
        orderRCV= view.findViewById(R.id.orderRCV);
        bar= view.findViewById(R.id.bar);
    }
}
