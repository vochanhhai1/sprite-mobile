package com.example.appqlsv.ActivityUser.Fragment.Home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Home.Adapter.AllFoodAdapter;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchSceen extends AppCompatActivity {
    private ArrayList<OrderDetails> listSearch;
    private AllFoodAdapter foodAdapter;
    private String searchString;
    private FrameLayout buttonBack;
    private TextView headerTitle;
    private RecyclerView SearchFoodRecyclerView;
    private ProgressBar progressBar;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sceen);

        anhxaid();

        listSearch= new ArrayList<>();
        searchString = getIntent().getStringExtra("searchstring");
        String headertitle = getResources().getString(R.string.Searchresultfor) + searchString;
        headerTitle.setText(headertitle);
        SearchFoodRecyclerView.setLayoutManager(new GridLayoutManager(this , 2));
        foodAdapter = new AllFoodAdapter(getApplicationContext(),listSearch);
        SearchFoodRecyclerView.setAdapter(foodAdapter);
        GetOrdercount(searchString);
        foodAdapter.notifyDataSetChanged();
        eventbutton();
    }

    private void eventbutton() {
        buttonBack.setOnClickListener(view -> finish());
    }

    private void anhxaid() {
        buttonBack= findViewById(R.id.buttonBack);
        headerTitle = findViewById(R.id.headerTitle);
        SearchFoodRecyclerView = findViewById(R.id.SearchFoodRecyclerView);
        progressBar= findViewById(R.id.progressBar);
    }

    private void GetOrdercount(String search) {
        String url = "https://spriteee.000webhostapp.com/getSearch.php?query="+search;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                listSearch.clear();

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
                        listSearch.add(orderDetails);
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("VolleyError", "Error: " + e);
                    }
                }
                foodAdapter.notifyDataSetChanged();
            }
        }, error -> {
            Toast.makeText(getApplication(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("VolleyError", "Error: " + error);
        });
        requestQueue.add(jsonArrayRequest);
    }
}