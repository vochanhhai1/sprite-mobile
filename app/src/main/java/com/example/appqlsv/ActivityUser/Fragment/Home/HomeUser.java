package com.example.appqlsv.ActivityUser.Fragment.Home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Home.Adapter.PopularFood;
import com.example.appqlsv.ActivityUser.Fragment.Home.Adapter.RecentFood;
import com.example.appqlsv.ActivityUser.Fragment.Home.noti.NotificationUser;
import com.example.appqlsv.ActivityUser.Fragment.MainActivity;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeUser extends Fragment {
    private ArrayList<Product> products;
    private androidx.recyclerview.widget.RecyclerView recentFoodRecyclerView, foodRecyclerView;
    private RecentFood recentFood;
    private PopularFood popularFood;
    private int userId;
    private Button txtviewmoreRecentFood,txtViewmorePopularmenu;
    private ImageButton notifyBtn;
    private final ArrayList<OrderDetails> orderDetailsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        andxaid(view);
        products = new ArrayList<>();

        UserSessionManager userSessionManager = new UserSessionManager(getActivity());
        userId = userSessionManager.getUserId();
        setupRecentFoodRecyclerView();
        setupPopularFoodRecyclerView();
        eventButton();
        GetisSeen(userId);
        return view;
    }
    private void setupRecentFoodRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recentFoodRecyclerView.setLayoutManager(gridLayoutManager);

        recentFood = new RecentFood(products, getActivity());
        recentFoodRecyclerView.setAdapter(recentFood);
        Loaddata();

    }

    private void setupPopularFoodRecyclerView() {
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager1.setOrientation(GridLayoutManager.VERTICAL);
        foodRecyclerView.setLayoutManager(gridLayoutManager1);

        popularFood = new PopularFood(orderDetailsList, getActivity());
        foodRecyclerView.setAdapter(popularFood);
        GetOrdercount();
    }

    private void eventButton() {
        txtviewmoreRecentFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.switchToHomeFragment();
                }
            }
        });
        txtViewmorePopularmenu.setOnClickListener(view -> startActivity(new Intent(getActivity(), PopularMenu.class)));
        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationUser.class);
                startActivity(intent);
            }
        });

    }

    public void GetisSeen(int user)
    {

        String url = "https://spriteee.000webhostapp.com/GetisSeen.php?userId="+user;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0 ; i< response.length();i++)
            {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String isSeenString = jsonObject.getString("isSeen");
                    boolean isSeen = isSeenString.equals("1");
                    if (!isSeen) { // 0 là có thông báo, 1 là không có thông báo
                        notifyBtn.setImageResource(R.drawable.ic_notification_badge);
                    }else
                    {
                        notifyBtn.setImageResource(R.drawable.ic_notification_non);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, error -> {

        });
        requestQueue.add(jsonArrayRequest);
    }

    private void replaceFragment(Fragment fragment)
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null) // Đặt null để không thêm transaction này vào BackStack
                .commit();
    }
    private void Loaddata() {
        String recentfood = "https://spriteee.000webhostapp.com/getSP_user.php?buyer=" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, recentfood, null,
                response -> {
                    try {
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
                        }
                        recentFood.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    }
                }, error -> {
                });
        requestQueue.add(jsonArrayRequest);
    }
    private void GetOrdercount() {
        String popularfood = "https://spriteee.000webhostapp.com/getOrdercount.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, popularfood, null, response -> {
            orderDetailsList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject orderDetailsObject = response.getJSONObject(i);
                    JSONObject productObject = orderDetailsObject.getJSONObject("product");
                    String imageUrl = "https://spriteee.000webhostapp.com/" + productObject.getString("image");
                    Product product = new Product(
                            productObject.getInt("id"),
                            productObject.getString("name"),
                            productObject.getInt("price"),
                            imageUrl,
                            productObject.getInt("orderCount"),
                            productObject.getInt("averageRating"),
                            productObject.getInt("isFavorited")==1
                    );
                    OrderDetails orderDetails = new OrderDetails(
                            orderDetailsObject.getInt("id"),
                            orderDetailsObject.getInt("price"),
                            product
                    );
                    orderDetailsList.add(orderDetails);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", "Error: " + e);
                }

            }
            popularFood.notifyDataSetChanged();
        }, error -> {
            Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("VolleyError", "Error: " + error);
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        GetOrdercount();
        Loaddata();

    }
    private void andxaid(View view) {
        notifyBtn= view.findViewById(R.id.notifyBtn);
        recentFoodRecyclerView = view.findViewById(R.id.recentFoodRecyclerView);
        foodRecyclerView = view.findViewById(R.id.foodRecyclerView);
        txtviewmoreRecentFood= view.findViewById(R.id.txtviewmoreRecentFood);
        txtViewmorePopularmenu= view.findViewById(R.id.txtViewmorePopularmenu);

    }

}
