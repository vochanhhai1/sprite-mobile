package com.example.appqlsv.ActivityUser.Fragment.Cart;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
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
import com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter.OrderdetailAdapter;
import com.example.appqlsv.ActivityUser.Fragment.Home.noti.NotificationUserAdapter;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.Models.Review;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetail extends AppCompatActivity {

    private FrameLayout orderDetailBack, orderDetailBtnDelete;
    private ImageView pendingBtn;
    private OrderdetailAdapter orderdetailAdapter;
    private ArrayList<OrderDetails> orderDetails;
    private ArrayList<Product> products;
    private ArrayList<Order> orderArrayList;
    private RecyclerView rv_food;
    private String urlGetDetail;
    private MaterialButton cartButton;
    private int orderId,userId;
    private ProgressBar progress_bar;
    private MaterialTextView totalPriceOrder;
    private Guideline guideline1,guideline2,guideline3;
    private MaterialCardView orderDetailCard;
    private LinearLayout linear1,linear2;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private NotificationUserAdapter notificationUserAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        anhxaid();
        orderDetails = new ArrayList<>();
        products = new ArrayList<>();
        orderArrayList = new ArrayList<>();

        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderid", 0);

        UserSessionManager userSessionManager = new UserSessionManager(this);
        userId= userSessionManager.getUserId();

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getApplicationContext(), 1);
        gridLayoutManager1.setOrientation(GridLayoutManager.VERTICAL);
        rv_food.setLayoutManager(gridLayoutManager1);

        orderdetailAdapter = new OrderdetailAdapter(orderDetails, getApplicationContext());
        rv_food.setAdapter(orderdetailAdapter);
        Loaddata();
        eventButton();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    hideReviewBtn();
                }
            }
        });

    }

    private void Loaddata() {
        urlGetDetail = "https://spriteee.000webhostapp.com/getOrderDetail.php?orderId=" + orderId+"&buyer="+userId;
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
                                DecimalFormat dec = new DecimalFormat("#,###");
                                totalPriceOrder.setText(dec.format(order.getTotalPrice()) + "đ");

                                if (!order.getStatus().equals("PENDING")) {
                                    pendingBtn.setVisibility(View.GONE);
                                }

                                if (order.getStatus().equals("PROCESSED") || order.getStatus().equals("COMPLETED")) {
                                    orderDetailBtnDelete.setVisibility(View.GONE);
                                }

                                if (!order.isReviewed() || !order.getStatus().equals("COMPLETED")) {
                                    hideReviewBtn();
                                }
                            }
                            orderdetailAdapter.notifyDataSetChanged();
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

    private void hideReviewBtn() {
        // Lấy thông số cần thiết của RecyclerView
        ConstraintLayout.LayoutParams recyclerViewParams = (ConstraintLayout.LayoutParams) rv_food.getLayoutParams();
        recyclerViewParams.bottomToTop = guideline3.getId();

        // Lấy thông số cần thiết của orderDetailCard
        ConstraintLayout.LayoutParams orderDetailLayoutParams = (ConstraintLayout.LayoutParams) orderDetailCard.getLayoutParams();
        orderDetailLayoutParams.topToBottom = guideline3.getId();

        // Thiết lập margin cho linear1 và linear2
        ConstraintLayout.LayoutParams linear1Params = (ConstraintLayout.LayoutParams) linear1.getLayoutParams();
        ConstraintLayout.LayoutParams linear2Params = (ConstraintLayout.LayoutParams) linear2.getLayoutParams();
        linear1Params.setMargins(50, 80, 0, 0);
        linear2Params.setMargins(0, 80, 50, 0);

        // Ẩn button cartButton
        cartButton.setVisibility(View.GONE);
    }

    private void eventButton() {
        orderDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
                builder.setTitle(getResources().getString(R.string.PENDING));
                String message = getResources().getString(R.string.YourOrder) + orderId + " " + getResources().getString(R.string.UPendingNoti);
                builder.setMessage(message);
                builder.setPositiveButton(getResources().getString(R.string.YES), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update process
                        updateOrderStatus("PROCESSED", orderId);
                        pendingBtn.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Updateordersuccessfully), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.NO), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel order
                        updateOrderStatus("CANCELLED", orderId);
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                    }
                });
                alert.show();
            }
        });
        orderDetailBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
                builder.setTitle(getResources().getString(R.string.Warning));
                builder.setMessage(getResources().getString(R.string.DeleteOrderMessage));
                builder.setPositiveButton(getResources().getString(R.string.YES), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update process
                        updateOrderStatus("CANCELLED",orderId);
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.NO), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                final AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                    }
                });
                alert.show();
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateOrderisReview(0,orderId);

                ArrayList<Review> listReview = new ArrayList<>();
                for (OrderDetails orderDetails1 : orderDetails) {
                    Review review = new Review();
                    review.setOrderDetail(orderDetails1);
                    review.setOrderDetailId(orderDetails1.getId());
                    review.setRating(5);
                    listReview.add(review);
                }


// Tạo một đối tượng Gson
                Gson gson = new Gson();

// Chuyển đối tượng thành chuỗi JSON
                String json = gson.toJson(listReview); // listReview là một ArrayList<Review>

// Tạo Intent và đặt chuỗi JSON vào Intent dưới dạng Extra
                Intent intent = new Intent(OrderDetail.this, RateScreen.class);
                intent.putExtra("listReviewJson", json);
                intent.putExtra("orderid", orderId);

// Khởi chạy Intent
                activityResultLauncher.launch(intent);

            }
        });
    }
    private void updateOrderisReview(int isReviewed,int orderId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://spriteee.000webhostapp.com/updateisReviewed.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            Toast.makeText(getApplicationContext(), "Order isReview updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error updating order isReview: " + response, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error updating order isReview: " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error updating order isReview: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error updating order isReview: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("isReviewed", String.valueOf(isReviewed));
                params.put("idorder", String.valueOf(orderId));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateOrderStatus(String status, int orderId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        progress_bar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://spriteee.000webhostapp.com/updateStatus.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            Toast.makeText(getApplicationContext(), "Order status updated successfully", Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error updating order status: " + response, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error updating order status: " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error updating order status: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error updating order status: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
                params.put("idorder", String.valueOf(orderId));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void anhxaid() {
        orderDetailBack = findViewById(R.id.orderDetailBack);
        orderDetailBtnDelete = findViewById(R.id.orderDetailBtnDelete);
        pendingBtn = findViewById(R.id.pendingBtn);
        rv_food = findViewById(R.id.rv_food);
        cartButton = findViewById(R.id.cartButton);
        totalPriceOrder = findViewById(R.id.totalPriceOrder);
        progress_bar = findViewById(R.id.bar);
        guideline1= findViewById(R.id.guideline1);
        guideline2= findViewById(R.id.guideline2);
        guideline3= findViewById(R.id.guideline3);
        orderDetailCard= findViewById(R.id.orderDetailCard);
        linear1= findViewById(R.id.linear1);
        linear2= findViewById(R.id.linear2);
    }
}