package com.example.appqlsv.ActivityUser.Fragment.Home;

import static android.view.View.INVISIBLE;
import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Home.Adapter.ReviewFoodAdapter;
import com.example.appqlsv.ActivityUser.Fragment.MainActivity;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Review;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class infoFood extends AppCompatActivity {
    private TextView description,nameFood;
    private ImageView imageFood;
    private AppCompatButton addFood;
    private ProgressBar progress_bar;
    private UserSessionManager userSessionManager;
    private RecyclerView reviewRecycleview;
    private ImageButton favoriteFood;
    private RatingBar rBar;
    private Toolbar toolbar;
    private Button txtViewDetails;
    private TextView avgRating,numOrder;
    private ArrayList<Review> reviewArrayList;
    private ReviewFoodAdapter reviewFoodAdapter;
    private int id;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_food);
        anhxaid();
        initToolBar();
        OrderDetails orderDetails = getOrderDetailsInfo();
        numOrder.setText(orderDetails.getProduct().getOrderCount()+" Orders");
        nameFood.setText(orderDetails.getProduct().getName());
        description.setText(orderDetails.getProduct().getMota());
        Picasso.get().load(orderDetails.getProduct().getImage()).into(imageFood);
        id = orderDetails.getProduct().getId();

        if (orderDetails.getProduct().isFavorited())
        {
            favoriteFood.setImageResource(R.drawable.ic_love);
        }else
        {
            favoriteFood.setImageResource(R.drawable.ic_love_defaut);
        }

        //hien thi sao
        rBar.setRating((float) orderDetails.getProduct().getAverageRating());
        avgRating.setText(String.valueOf(Math.round(orderDetails.getProduct().getAverageRating() * 100.0) / 100.0));

        userSessionManager = new UserSessionManager(this);

        reviewArrayList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(infoFood.this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        reviewRecycleview.setLayoutManager(gridLayoutManager);

        reviewFoodAdapter = new ReviewFoodAdapter(infoFood.this, reviewArrayList);
        reviewRecycleview.setAdapter(reviewFoodAdapter);
        Loaddata();

        EventButton();
    }



    private void Loaddata() {
        String url = "https://spriteee.000webhostapp.com/getReviewtoInfofood.php?productid=" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                    @Override
                    public void onResponse(JSONArray response) {
                        reviewArrayList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);

                                JSONObject orderObject = object.getJSONObject("order_detail");
                                OrderDetails orderDetails = new OrderDetails(
                                        orderObject.getInt("id"),
                                        orderObject.getInt("product_id"),
                                        orderObject.getInt("quantity"),
                                        orderObject.getInt("price")
                                );

                                JSONObject userObject = object.getJSONObject("user");
                                String imageUrl = "https://spriteee.000webhostapp.com/" + userObject.getString("photoUrl");
                                Users users = new Users(
                                        userObject.getInt("id"),
                                        userObject.getString("fullname"),
                                        userObject.getString("email"),
                                        userObject.getString("phone"),
                                        userObject.getString("address"),
                                        imageUrl
                                );

                                // Parsing order details data
                                Review review = new Review(
                                        object.getInt("review_id"),
                                        orderDetails,
                                        object.getInt("rating"),
                                        object.getString("content"),
                                        object.getString("created_at"),
                                        object.getString("updated_at"),
                                        users
                                );
                                reviewArrayList.add(review);
                                progress_bar.setVisibility(View.GONE);
                            }

                            calculateStar(reviewArrayList);
                            // cập nhat du lieu
                            reviewFoodAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: " + error);
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @SuppressLint("DefaultLocale")
    private void calculateStar(ArrayList<Review> list)
    {
        int sumRating = 0;
        for (Review review1: list) {
            sumRating += review1.getRating();
        }
        int numReviews = list.size();
        float averageRating =  (float) sumRating / numReviews;
        //hien thi sao
        rBar.setRating(averageRating);
        avgRating.setText(String.valueOf(Math.round(averageRating * 100.0) / 100.0));
        avgRating.setText(String.format("%.1f", averageRating));
    }

    private OrderDetails getOrderDetailsInfo() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("OrderDetails")) {
            return (OrderDetails) intent.getSerializableExtra("OrderDetails");
        }
        return null;
    }

    private void EventButton() {
        addFood.setOnClickListener(view -> {
            progress_bar.setVisibility(View.INVISIBLE);
            OrderDetails orderDetails = getOrderDetailsInfo();
            int productId = orderDetails.getProduct().getId();
            // Lấy thông tin user (userId) thông qua userSessionManager
            int userId = userSessionManager.getUserId();

            if (userId != -1) {
                addToCart(userId, productId);
                // Sau khi thêm sản phẩm vào giỏ hàng thành công, chuyển sang fragment CartUser
                Intent intent = new Intent(infoFood.this, MainActivity.class);
                intent.putExtra("fragment", "cart"); // Gửi dữ liệu để xác định fragment cần chuyển đến
                startActivity(intent);
            } else {
                // Xử lý trường hợp không tìm thấy thông tin user
                Toast.makeText(infoFood.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setNavigationOnClickListener(v -> finish());
        txtViewDetails.setOnClickListener(view -> {
            Intent intent = new Intent(infoFood.this, ReviewDetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);

        });
        favoriteFood.setOnClickListener(view -> {
            OrderDetails orderDetails = getOrderDetailsInfo();
            if (orderDetails.getProduct().isFavorited()) {
                favoriteFood.setImageResource(R.drawable.ic_love_defaut);

                updateisFavorite(0, id);

                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.Deletefromfavorite),
                        Toast.LENGTH_SHORT
                ).show();

                orderDetails.getProduct().setFavorited(!orderDetails.getProduct().isFavorited());
            } else {
                favoriteFood.setImageResource(R.drawable.ic_love);
                updateisFavorite(1, id);

                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.Addedtofavorite),
                        Toast.LENGTH_SHORT
                ).show();
                orderDetails.getProduct().setFavorited(!orderDetails.getProduct().isFavorited());
            }

        });
    }
    private void updateisFavorite(int isFavorited,int id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://spriteee.000webhostapp.com/updateisFavorited.php",
                response -> {
                    if (response.trim().equals("success")) {

                    } else {
                        Toast.makeText(getApplicationContext(), "Error isFavorited order isReview: " + response, Toast.LENGTH_SHORT).show();
                        Log.d(MotionEffect.TAG, "Error updating order isFavorited: " + response);
                    }
                }, error -> {
                    Toast.makeText(getApplicationContext(), "Error updating order isFavorited: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(MotionEffect.TAG, "Error updating order isFavorited: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("isFavorited", String.valueOf(isFavorited));
                params.put("id", String.valueOf(id));
                return params;
            }
        };

        requestQueue.add(stringRequest);
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

    private void addToCart(int user , int productId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://spriteee.000webhostapp.com/AddtoCart.php", response -> {
            if (response.trim().equals("success")) {
                addFood.setVisibility(INVISIBLE);
                Toast.makeText(infoFood.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(infoFood.this, "Lỗi", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "lỖI!\n" + response);
            }
        }, error -> {
            Toast.makeText(infoFood.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            Log.d("AAA", "lỖI!\n" + error.toString());
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("product_id", String.valueOf(productId));
                params.put("user_id", String.valueOf(user));
                params.put("quantity", "1");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void anhxaid() {
        description = findViewById(R.id.description);
        nameFood = findViewById(R.id.nameFood);
        imageFood = findViewById(R.id.imageFood);
        addFood = findViewById(R.id.addFood);
        progress_bar = findViewById(R.id.progress_bar);
        reviewRecycleview = findViewById(R.id.reviewRecycleview);
        favoriteFood = findViewById(R.id.favoriteFood);
        rBar = findViewById(R.id.rBar);
        txtViewDetails= findViewById(R.id.txtViewDetails);
        avgRating = findViewById(R.id.avgRating);
        numOrder= findViewById(R.id.numOrder);
        toolbar= findViewById(R.id.toolbar);
    }
}