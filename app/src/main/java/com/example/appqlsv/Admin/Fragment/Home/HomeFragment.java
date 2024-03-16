package com.example.appqlsv.Admin.Fragment.Home;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Home.noti.NotificationUser;
import com.example.appqlsv.Admin.Fragment.QuanLyDonHang.allorder.QuanLyDonHangFragment;
import com.example.appqlsv.Admin.Fragment.QuanLySanPham.QuanLySanPhamFragment;
import com.example.appqlsv.Admin.HomeAdmin;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ImageButton notifyBtn;
    private Button viewMoreOrder,viewMoreMenu;
    private RecyclerView arrivedOrderRCV,myMenuRCV;
    private ImageView logoDone;
    private ProgressBar bar;
    private ArrayList<Order> listOrder;
    private ArrivedOrderAdapter arrivedOrderAdapter;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private MyMenuAdapter myMenuAdapter;
    private ArrayList<Product> productArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        andxaid(view);
        listOrder= new ArrayList<>();
        orderDetailsArrayList= new ArrayList<>();
        productArrayList= new ArrayList<>();
        getArrivedOrder();
        getMyMenu();
        getUnseenNotify();
        eventbuton();
        return view;
    }

    private void eventbuton() {
        viewMoreOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new QuanLyDonHangFragment());
                NavigationView navigationView = ((HomeAdmin) getActivity()).getNavigationView();

                navigationView.setCheckedItem(R.id.nav_don);

            }
        });
        viewMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new QuanLySanPhamFragment());
                NavigationView navigationView = ((HomeAdmin) getActivity()).getNavigationView();

                navigationView.setCheckedItem(R.id.nav_sanpham);
            }
        });
        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificationUser.class));
            }
        });
    }

    public void getUnseenNotify()
    {
        String url = "https://spriteee.000webhostapp.com/getAdminisSeen.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            ArrayList<Integer> listNotification = new ArrayList<Integer>();
            for (int i = 0 ; i< response.length();i++)
            {
                listNotification.add(i);
                if (listNotification.size() > 0) {
                    notifyBtn.setImageResource(R.drawable.ic_notification_badge);
                } else {
                    notifyBtn.setImageResource(R.drawable.ic_notification_non);
                }
            }
        }, error -> {

        });
        requestQueue.add(jsonArrayRequest);
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout1, fragment);
        fragmentTransaction.commit();
    }
    private void getArrivedOrder() {
        String urlorder = "https://spriteee.000webhostapp.com/getAdminArrivedOrder.php";
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
                                if (listOrder != null && listOrder.size() == 0) {
                                    logoDone.setVisibility(View.VISIBLE);
                                }

                                bar.setVisibility(View.GONE);
                            }
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                            arrivedOrderRCV.setLayoutManager(gridLayoutManager);

                            arrivedOrderAdapter = new ArrivedOrderAdapter(getActivity(),listOrder);
                            arrivedOrderRCV.setAdapter(arrivedOrderAdapter);
                            arrivedOrderAdapter.notifyDataSetChanged();
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

    private void getMyMenu() {
        String popularfood = "https://spriteee.000webhostapp.com/getOrdercount.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, popularfood, null, response -> {
            orderDetailsArrayList.clear();
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
                    orderDetailsArrayList.add(orderDetails);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", "Error: " + e);
                }

            }
            GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
            gridLayoutManager1.setOrientation(GridLayoutManager.VERTICAL);
            myMenuRCV.setLayoutManager(gridLayoutManager1);

            myMenuAdapter = new MyMenuAdapter(getActivity(), orderDetailsArrayList);
            myMenuRCV.setAdapter(myMenuAdapter);
            myMenuAdapter.notifyDataSetChanged();
        }, error -> {
            Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("VolleyError", "Error: " + error);
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyMenu();
        getArrivedOrder();
        getUnseenNotify();
    }
    private void andxaid(View view) {
        notifyBtn = view.findViewById(R.id.notifyBtn);
        viewMoreOrder = view.findViewById(R.id.viewMoreOrder);
        viewMoreMenu = view.findViewById(R.id.viewMoreMenu);
        arrivedOrderRCV = view.findViewById(R.id.arrivedOrderRCV);
        myMenuRCV = view.findViewById(R.id.myMenuRCV);
        logoDone = view.findViewById(R.id.logoDone);
        bar = view.findViewById(R.id.bar);
    }
}
