package com.example.appqlsv.ActivityUser.Fragment.Home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Home.Adapter.AllFoodAdapter;
import com.example.appqlsv.ActivityUser.Fragment.Home.noti.NotificationUser;
import com.example.appqlsv.ActivityUser.Fragment.MainActivity;
import com.example.appqlsv.Models.Filter;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopularMenu extends AppCompatActivity {
    private FrameLayout buttonBack;
    private TextInputEditText filterText;
    private ImageButton spinner,notifyBtn;
    private RecyclerView addFoodRecyclerView;
    private AllFoodAdapter foodAdapter;
    private ArrayList<Product> products;
    private int userId;
    private ArrayList<Order> orderArrayList;
    private ArrayList<Filter> filterArrayList;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private ArrayList<OrderDetails> neworderDetailsArrayList;
    private List<Float> range;
    private  Filter filter = new Filter();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ProgressBar progressBar;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_menu);
        initView();

        initVariables();
        UserSessionManager userSessionManager = new UserSessionManager(getApplicationContext());
        userId = userSessionManager.getUserId();

        createAddFoodRecyclerView(filter);
        GetOrdercount();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String filterData = data.getStringExtra("filterdata");

                            Gson gson = new Gson();
                            Type listType = new TypeToken<Filter>(){}.getType();
                            filter = gson.fromJson(filterData, listType);
                            createAddFoodRecyclerView(filter);
                            List<Float> filteredRange = filter.getRange();
                            Toast.makeText(this, "" +filter.getType() , Toast.LENGTH_SHORT).show();
                            GetTypesearch(filter.getType(),filter.getSort(),filteredRange.get(0));
                            //GetOrdersort(filter.getSort());
                            if(filter.getType()==0)
                            {
                                GetOrdercount();
                            }
                        }
                    }
                });
        GetisSeen(userId);
        eventButton();
        getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
    }

    private void initVariables() {
        products= new ArrayList<>();
        orderArrayList= new ArrayList<>();
        filterArrayList = new ArrayList<>();
        orderDetailsArrayList= new ArrayList<>();
        neworderDetailsArrayList = new ArrayList<>();
        range = new ArrayList<>();
        List<Float> range = new ArrayList<>();
        range.add(0f);
        range.add(50000000f);
        filter.setRange(range);
        progressBar = findViewById(R.id.progressBar); // Ánh xạ progressBar từ layout
    }
    @SuppressLint("NotifyDataSetChanged")
    private void createAddFoodRecyclerView(Filter filter) {
        if (filter != null) {
            if (filter.isWayUp()) {
                if (!filter.getSort().startsWith("-")) {
                    filter.setSort("-" + filter.getSort());
                }
            } else {
                if (filter.getSort().startsWith("-")) {
                    filter.setSort(filter.getSort().substring(1));
                }
            }
        }
        addFoodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addFoodRecyclerView.setHasFixedSize(true);

        progressBar.setVisibility(View.VISIBLE);

        foodAdapter = new AllFoodAdapter(getApplicationContext(),orderDetailsArrayList);
        addFoodRecyclerView.setAdapter(foodAdapter);

        addFoodRecyclerView.setLayoutManager(new GridLayoutManager(this , 2));
        foodAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    public void GetisSeen(int user)
    {
        progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar

        String url = "https://spriteee.000webhostapp.com/GetisSeen.php?userId="+user;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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

            progressBar.setVisibility(View.GONE);
        }, error -> {

        });
        requestQueue.add(jsonArrayRequest);
    }

    private void Getorderid(int iduser)
    {
        String url = "https://spriteee.000webhostapp.com/getOrder.php?buyer="+iduser;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            orderArrayList.clear();
           for (int i = 0 ; i< response.length();i++)
           {
               try {
                   JSONObject jsonObject = response.getJSONObject(i);
                    int id = jsonObject.getInt("idorder");
                   InsertNotification(id,iduser);
               } catch (JSONException e) {
                   throw new RuntimeException(e);
               }
           }
        }, error -> {

        });
        requestQueue.add(jsonArrayRequest);
    }
    public void InsertNotification(int orderid, int userid)
    {
        String url = "https://spriteee.000webhostapp.com/InsertNotification.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.trim().equals("success")) {
                Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
//                    progress_bar.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Error From Api: " + response);
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error" + error.getMessage());
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("orderid", String.valueOf(orderid));
                params.put("userId", String.valueOf(userid));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void GetTypesearch(int type, String price,float price1)
    {
        String url ="https://spriteee.000webhostapp.com/getSearchproduct.php?query="+type+"&sort="+price+"&price="+price1;
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
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
                            productObject.getInt("id_danhmuc"),
                            productObject.getInt("orderCount"),
                            productObject.getInt("averageRating"),
                            productObject.getInt("isFavorited") == 1
                    );
                    OrderDetails orderDetails = new OrderDetails(
                            product
                    );
                    orderDetailsArrayList.add(orderDetails);
                    filter.setType(orderDetails.getProduct().getId_danhmuc());
                    filterArrayList.add(filter);
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", "Error: " + e);
                }
            }

            addFoodRecyclerView.setLayoutManager(new LinearLayoutManager(PopularMenu.this));
            addFoodRecyclerView.setHasFixedSize(true);

            foodAdapter = new AllFoodAdapter(getApplicationContext(),orderDetailsArrayList);
            addFoodRecyclerView.setAdapter(foodAdapter);

            addFoodRecyclerView.setLayoutManager(new GridLayoutManager(PopularMenu.this , 2));
            foodAdapter.notifyDataSetChanged();
        }, error -> Log.e(TAG, "Error: " + error.getMessage()));
        requestQueue.add(jsonArrayRequest);
    }

    private void eventButton() {
        buttonBack.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), MainActivity.class))
        );
        spinner.setOnClickListener(view -> {

            Gson gson = new Gson();

            String json = gson.toJson(filter); // listReview là một ArrayList<Review>
            Intent intent = new Intent(PopularMenu.this, FilterScreen.class);
            intent.putExtra("filterdata", json);
            activityResultLauncher.launch(intent);


        });
        filterText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
        notifyBtn.setOnClickListener(view -> {
            Getorderid(userId);
            startActivity(new Intent(PopularMenu.this, NotificationUser.class));
        });
    }

    private void performSearch() {
        Intent intent = new Intent(this, SearchSceen.class);
        intent.putExtra("searchstring", filterText.getText().toString());
        startActivity(intent);
    }

    private void GetOrdercount() {
        String popularfood = "https://spriteee.000webhostapp.com/getProductsort.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                            productObject.getInt("id_danhmuc"),
                            productObject.getInt("orderCount"),
                            productObject.getInt("averageRating"),
                            productObject.getInt("isFavorited") == 1
                    );
                    products.add(product);
                    OrderDetails orderDetails = new OrderDetails(
                            product
                    );
                    orderDetailsArrayList.add(orderDetails);

                    Filter filter = new Filter();
                    filter.setType(orderDetails.getProduct().getId_danhmuc());
                    filterArrayList.add(filter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", "Error: " + e);
                }

            }

            foodAdapter.notifyDataSetChanged();
        }, error -> {
            Toast.makeText(this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("VolleyError", "Error: " + error);
        });
        requestQueue.add(jsonArrayRequest);
    }



    @SuppressLint("InflateParams")
    private void initView() {
        buttonBack = findViewById(R.id.buttonBack);
        filterText = findViewById(R.id.filterText);
        spinner = findViewById(R.id.spinner);
        addFoodRecyclerView = findViewById(R.id.addFoodRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        notifyBtn= findViewById(R.id.notifyBtn);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetisSeen(userId);
    }

}