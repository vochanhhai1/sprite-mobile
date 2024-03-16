package com.example.appqlsv.ActivityUser.Fragment.Cart;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter.OrderAdapter;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Myorder extends Fragment {
    private ArrayList<Order> listOrder;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private OrderAdapter orderAdapter;
    private UserSessionManager userSessionManager;
    private int userId;
    private String urlorder;
    private RecyclerView rv_order;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order_user, container, false);
        anhxaid(view);
        listOrder = new ArrayList<>();
        orderDetailsArrayList = new ArrayList<>();
        userSessionManager = new UserSessionManager(getActivity());
        userId = userSessionManager.getUserId();
        urlorder = "https://spriteee.000webhostapp.com/getMyorder.php?buyer=" + userId;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rv_order.setLayoutManager(gridLayoutManager);

        orderAdapter = new OrderAdapter(getActivity(), listOrder);
        rv_order.setAdapter(orderAdapter);
        Loaddata();
        eventbutton();
        return view;
    }

    private void eventbutton() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        });
    }

    private void anhxaid(View view) {
        rv_order = view.findViewById(R.id.rv_order);
        progressBar = view.findViewById(R.id.bar);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
    }

//    private void Loaddata() {
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlorder, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        listOrder.clear(); // Clear existing data before adding new data
//                        orderDetailsArrayList.clear();
//                        try {
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject object = response.getJSONObject(i);
//
//                                JSONObject orderObject = object.getJSONObject("order");
//                                Order order = new Order(
//                                        orderObject.getInt("idorder"),
//                                        orderObject.getInt("totalPrice"),
//                                        orderObject.getString("notes"),
//                                        orderObject.getInt("isReviewed")==1,
//                                        orderObject.getString("status"),
//                                        orderObject.getString("createAt"),
//                                        orderObject.getString("updateAt"),
//                                        orderObject.getInt("buyer")
//                                );
//
//                                JSONObject productObject = object.getJSONObject("products");
//                                String urlimage = "https://spriteee.000webhostapp.com/" + productObject.getString("image");
//                                // Parsing product data
//                                Product product = new Product(
//                                        productObject.getInt("id"),
//                                        productObject.getString("name"),
//                                        productObject.getInt("price"),
//                                        urlimage,
//                                        productObject.getString("mota"),
//                                        productObject.getString("createAt"),
//                                        productObject.getString("deleteAt"),
//                                        productObject.getInt("id_danhmuc")
//                                );
//
//                                // Parsing order details data
//                                OrderDetails orderDetails = new OrderDetails(
//                                        object.getInt("idorderdetail"),
//                                        order,
//                                        object.getInt("productId"),
//                                        object.getInt("price"),
//                                        object.getInt("quantity"),
//                                        product,
//                                        object.getInt("totalQuantity")
//                                );
//                                // Adding parsed order details to the list
//                                orderDetails.setOrderId(order.getId());
//                                orderDetailsArrayList.add(orderDetails);
//                                order.setOrderDetails(orderDetailsArrayList);
//                                listOrder.add(order);
//                            }
//                            // Notify adapter that data has changed
//                            orderAdapter.notifyDataSetChanged();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getActivity(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.d("VolleyError", "Error: " + e);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("VolleyError", "Error: " + error.toString());
//            }
//        });
//        requestQueue.add(jsonArrayRequest);
//    }

    private void Loaddata() {
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
                            }
                            sortOrder();
                            orderAdapter.notifyDataSetChanged();
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
        Loaddata();
    }

    private void sortOrder() {
        ArrayList<Order> listArrived = new ArrayList<>();
        ArrayList<Order> listPending = new ArrayList<>();
        ArrayList<Order> listProcessed = new ArrayList<>();
        ArrayList<Order> listCompleted = new ArrayList<>();

        for (Order item : listOrder) {
            switch (item.getStatus()) {
                case "ARRIVED":
                    listArrived.add(item);
                    break;
                case "PENDING":
                    listPending.add(item);
                    break;
                case "PROCESSED":
                    listProcessed.add(item);
                    break;
                case "COMPLETED":
                    if (item.isReviewed()) {
                        listCompleted.add(item);
                    } else {
                        listCompleted.add(0, item);
                    }
                    break;
            }
        }

        ArrayList<Order> tempList = new ArrayList<>();
        tempList.addAll(listArrived);
        tempList.addAll(listPending);
        tempList.addAll(listProcessed);
        tempList.addAll(listCompleted);

        listOrder.clear();
        listOrder.addAll(tempList);
    }

}
