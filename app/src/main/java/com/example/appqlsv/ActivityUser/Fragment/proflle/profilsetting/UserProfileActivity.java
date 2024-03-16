package com.example.appqlsv.ActivityUser.Fragment.proflle.profilsetting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    private ImageView userImage;
    private Toolbar toolbar;
    private TextView nameProfile,emailProfile;
    private RecyclerView favoriteRecycleview;
    private UserSessionManager userSessionManager;
    private Button editprofile;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private FavoriteAdapter favoriteAdapter;
    private int id;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        iniView();

        orderDetailsArrayList= new ArrayList<>();


        Users users = getUsersInfo();
        if (users != null) {
            emailProfile.setText(users.getEmail());
            nameProfile.setText(users.getFullname());
            id = users.getId_khachhang();
        } else {
            userSessionManager = new UserSessionManager(this);
            emailProfile.setText(userSessionManager.getUsername());
            nameProfile.setText(userSessionManager.getFullname());
            id = userSessionManager.getUserId();
        }

        initToolBar();
        setupRecentFoodRecyclerView();
        eventbuton();
        GetUrlphoto();
        getWindow().setStatusBarColor(Color.parseColor("#000000"));
    }
    private Users getUsersInfo() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Users")) {
            return (Users) intent.getSerializableExtra("Users");
        }
        return null;
    }
    private void GetUrlphoto() {
        String url = "https://spriteee.000webhostapp.com/getUser.php?id=" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, jsonArray -> {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    imageUrl = "https://spriteee.000webhostapp.com/" + jsonObject.getString("photoUrl");
                    Picasso.get().load(imageUrl).into(userImage);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, volleyError -> {
            volleyError.printStackTrace();
            Toast.makeText(getApplicationContext(), "Lỗi kết nối: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void eventbuton() {
        toolbar.setNavigationOnClickListener(view -> finish());
        editprofile.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),BioScreen.class)));
    }
    private void initToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
    private void setupRecentFoodRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        favoriteRecycleview.setLayoutManager(gridLayoutManager);

        favoriteAdapter = new FavoriteAdapter(orderDetailsArrayList, this);
        favoriteRecycleview.setAdapter(favoriteAdapter);
        Loaddata();
    }
    private void Loaddata() {
        String popularfood = "https://spriteee.000webhostapp.com/getFavorite.php";
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
                    Toast.makeText(this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", "Error: " + e);
                }

            }
            favoriteAdapter.notifyDataSetChanged();
        }, error -> {
            Toast.makeText(this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("VolleyError", "Error: " + error);
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void iniView() {
        userImage = findViewById(R.id.userImage);
        editprofile = findViewById(R.id.editprofile);
        toolbar = findViewById(R.id.toolbar);
        nameProfile = findViewById(R.id.nameProfile);
        emailProfile = findViewById(R.id.emailProfile);
        favoriteRecycleview = findViewById(R.id.favoriteRecycleview);
    }
}