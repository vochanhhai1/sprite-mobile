package com.example.appqlsv.ActivityUser.Fragment.Cart;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter.RatingFoodAdapter;
import com.example.appqlsv.Models.Review;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RateScreen extends AppCompatActivity {

    private RecyclerView rv_rating_food;
    private ProgressBar bar;
    private MaterialButton submitBtn,btnSkip;
    private RatingFoodAdapter ratingFoodAdapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_screen);
        anhxaid();
        //        Intent intent = getIntent();
//        orderId = intent.getIntExtra("orderid", 0);
        UserSessionManager userSessionManager = new UserSessionManager(this);
        userId = userSessionManager.getUserId();

        Intent intent = getIntent();
        String json = intent.getStringExtra("listReviewJson");
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Review>>(){}.getType();
        ArrayList<Review> listReview = gson.fromJson(json, listType);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getApplicationContext(), 1);
        gridLayoutManager1.setOrientation(GridLayoutManager.VERTICAL);
        rv_rating_food.setLayoutManager(gridLayoutManager1);

        ratingFoodAdapter = new RatingFoodAdapter(this, listReview);
        rv_rating_food.setAdapter(ratingFoodAdapter);
        eventButton();
    }
    private void eventButton() {
        btnSkip.setOnClickListener(view -> finish());

        submitBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(RateScreen.this);
            builder.setMessage(getResources().getString(R.string.ReviewOrderMessage));
            builder.setPositiveButton(getResources().getString(R.string.YES), (dialog, which) -> {
                // Lấy danh sách đánh giá từ Adapter
                ArrayList<Review> reviews = ratingFoodAdapter.getReviews();

                for (Review review : reviews) {
                    String content = review.getContent();
                    int rating = review.getRating();
                    uploadReview(review.getOrderDetailId(), userId, content, rating);
                }
                dialog.dismiss();
            });
            builder.setNegativeButton(getResources().getString(R.string.NO), (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.setOnShowListener(dialog -> {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            });
            alert.show();
        });
    }

    public void uploadReview(final int orderDetailId, final int userId,final String content,final int rating) {
        String url = "https://spriteee.000webhostapp.com/InsertReview.php";
        bar.setVisibility(ProgressBar.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        @SuppressLint("RestrictedApi") StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(getApplicationContext(), "SUCCESS create review", Toast.LENGTH_SHORT).show();
                        bar.setVisibility(ProgressBar.GONE);
                        goBack();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + response, Toast.LENGTH_SHORT).show();
                        Log.d("VolleyError", "Error: " + response);
                    }
                }, error -> {
                    Toast.makeText(getApplicationContext(), "JSON parsing error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "JSON parsing error: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("orderDetailId", String.valueOf(orderDetailId));
                params.put("userId", String.valueOf(userId));
                params.put("content", content);
                params.put("rating", String.valueOf(rating));
                return params;
            }
        };
        queue.add(stringRequest);
    }
    private void goBack() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void anhxaid() {
        rv_rating_food = findViewById(R.id.rv_rating_food);
        bar = findViewById(R.id.bar);
        submitBtn = findViewById(R.id.submitBtn);
        btnSkip = findViewById(R.id.btnSkip);
    }
}