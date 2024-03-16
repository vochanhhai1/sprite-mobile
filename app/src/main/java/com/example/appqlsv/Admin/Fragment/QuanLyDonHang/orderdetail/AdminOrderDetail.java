package com.example.appqlsv.Admin.Fragment.QuanLyDonHang.orderdetail;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminOrderDetail extends AppCompatActivity {

    private FrameLayout orderDetailBack;
    private MaterialTextView textOrderDetail,note,subTotal,surchargeTxt,priceTotal;
    private MaterialButton updateBtn;
    private TextView spinner;
    private EditText surcharge;
    private RecyclerView orderDetailRCV;
    private MaterialCardView materialCardView2;
    private LinearLayoutCompat orderDetailCard;
    private int orderID;
    private ArrayList<OrderDetails> orderDetails;
    private ArrayList<Product> products;
    private ArrayList<Order> orderArrayList;
    private MutableLiveData<String> selectedStatus = new MutableLiveData<>();

    private AdminOrderDetailAdapter adminOrderDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);

        intview();
        orderDetails = new ArrayList<>();
        products = new ArrayList<>();
        orderArrayList = new ArrayList<>();

        Intent intent = getIntent();
        orderID = intent.getIntExtra("orderid", 0);
        getOrderdetail();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderStatus(selectedStatus.getValue());
            }
        });
        eventbutton();

        selectedStatus.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String it) {
                for (Order order: orderArrayList)
                {
                    if ("PENDING".equals(it)) {
                        surcharge.setVisibility(View.VISIBLE);
                        surchargeTxt.setVisibility(View.VISIBLE);
                    } else {
                        surcharge.setVisibility(View.GONE);
                        surchargeTxt.setVisibility(View.GONE);
                        priceTotal.setText(String.valueOf(order.getTotalPrice()));
                    }
                }

            }
        });
        surcharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // không cần xử lý trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // không cần xử lý khi văn bản đang thay đổi
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                for (Order order: orderArrayList)
                {
                    if (!TextUtils.isEmpty(s)) {
                        DecimalFormat dec = new DecimalFormat("#,###");
                        priceTotal.setText(dec.format(order.getTotalPrice() + Integer.parseInt(s.toString())) + "đ");
                    }
                }

            }
        });


    }

    private void updateOrderStatus(String status)
    {
        if (selectedStatus.getValue().equals("PENDING") && surcharge.getText().toString().isEmpty()) {
            Toast.makeText(
                    this,
                    getResources().getString(R.string.Surchargecannotbeempty),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String url = "https://spriteee.000webhostapp.com/updateOrderStatus.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.trim().equals("success")) {
                    Toast.makeText(
                            getApplicationContext(),
                            getResources().getString(R.string.Updateordersuccessfully),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            AdminOrderDetail.this,
                            s,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.ErrorNetwork),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("idorder", String.valueOf(orderID));

                if (selectedStatus.getValue().equals("PENDING")) {
                    params.put("status",status);
                    params.put("surcharge",surcharge.getText().toString());
                } else {
                    params.put("status",status);
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getOrderdetail() {
        String urlGetDetail = "https://spriteee.000webhostapp.com/getAdminOrderDetail.php?orderId=" + orderID;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetDetail, null,
                new Response.Listener<JSONArray>() {
                    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            orderArrayList.clear();
                            orderDetails.clear();
                            products.clear();
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

                                JSONArray orderDetailsArray = jsonObject.getJSONArray("orderDetails");
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
                                    products.add(product);
                                    OrderDetails orderDetails1 = new OrderDetails(
                                            orderDetailsObject.getInt("idorderdetail"),
                                            orderDetailsObject.getInt("productId"),
                                            orderDetailsObject.getInt("price"),
                                            orderDetailsObject.getInt("quantity"),
                                            product
                                    );
                                    orderDetails.add(orderDetails1);
                                }

                                order.setOrderDetails(orderDetails);
                                order.setOrderDetails(orderDetails);
                                orderArrayList.add(order);

                                String[] status = { getResources().getString(R.string.PENDING),
                                        getResources().getString(R.string.PROCESSED),
                                        getResources().getString(R.string.COMPLETED),
                                        getResources().getString(R.string.CANCELLED) };
                                    switch (order.getStatus()) {
                                        case "PENDING":
                                            spinner.setText(getResources().getString(R.string.PENDING));
                                            break;
                                        case "PROCESSED":
                                            spinner.setText(getResources().getString(R.string.PROCESSED));
                                            break;
                                        case "COMPLETED":
                                            spinner.setText(getResources().getString(R.string.COMPLETED));
                                            break;
                                        case "CANCELLED":
                                            spinner.setText(getResources().getString(R.string.CANCELLED));
                                            break;
                                    }

                                spinner.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminOrderDetail.this);
                                        builder.setTitle(getResources().getString(R.string.Filtertheorders));
                                        builder.setItems(status, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case 0:
                                                        selectedStatus.setValue("PENDING");
                                                        spinner.setText(getResources().getString(R.string.PENDING));
                                                        break;
                                                    case 1:
                                                        selectedStatus.setValue("PROCESSED");
                                                        spinner.setText(getResources().getString(R.string.PROCESSED));
                                                        break;
                                                    case 2:
                                                        selectedStatus.setValue("COMPLETED");
                                                        spinner.setText(getResources().getString(R.string.COMPLETED));
                                                        break;
                                                    case 3:
                                                        selectedStatus.setValue("CANCELLED");
                                                        spinner.setText(getResources().getString(R.string.CANCELLED));
                                                        break;
                                                }
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                });

                                DecimalFormat dec = new DecimalFormat("#,###");
                                subTotal.setText(dec.format(order.getTotalPrice()) + "đ");
                                priceTotal.setText(dec.format(order.getTotalPrice()) + "đ");
                                note.setText(" " + order.getNotes());
                                note.setContentDescription(order.getNotes());

                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(AdminOrderDetail.this, 1);
                            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                            orderDetailRCV.setLayoutManager(gridLayoutManager);

                            adminOrderDetailAdapter = new AdminOrderDetailAdapter(AdminOrderDetail.this, orderDetails);
                            orderDetailRCV.setAdapter(adminOrderDetailAdapter);
                            adminOrderDetailAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("VolleyError", "Error: " + error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void eventbutton() {
        orderDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void intview() {
        orderDetailBack = findViewById(R.id.orderDetailBack);
        textOrderDetail = findViewById(R.id.textOrderDetail);
        note = findViewById(R.id.note);
        subTotal = findViewById(R.id.subTotal);
        surchargeTxt = findViewById(R.id.surchargeTxt);
        priceTotal = findViewById(R.id.priceTotal);
        updateBtn = findViewById(R.id.updateBtn);
        spinner = findViewById(R.id.spinner);
        surcharge = findViewById(R.id.surcharge);
        orderDetailRCV = findViewById(R.id.orderDetailRCV);
        materialCardView2 = findViewById(R.id.materialCardView2);
        orderDetailCard = findViewById(R.id.orderDetailCard);
    }
}