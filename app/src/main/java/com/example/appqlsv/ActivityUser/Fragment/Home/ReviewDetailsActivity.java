package com.example.appqlsv.ActivityUser.Fragment.Home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Home.Adapter.ReviewDetailsFoodAdapter;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Review;
import com.example.appqlsv.Models.ReviewStatistic;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewDetailsActivity extends AppCompatActivity {
    private FrameLayout buttonBack;
    private TextView avgRating,numReview;
    private RatingBar rBar;
    private ProgressBar prg1,prg2,prg3,prg4,prg5,progress_bar;
    private RecyclerView rcvReviewDetails;
    private LinearLayout View;
    private ArrayList<Review> reviewArrayList;
    private ArrayList<ReviewStatistic> reviewStatisticArrayList;
    private int id;
    private ReviewDetailsFoodAdapter reviewDetailsFoodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        initview();
        id = getIntent().getIntExtra("id", 0);
        //Toast.makeText(getApplicationContext(), "" +id, Toast.LENGTH_SHORT).show();
        View.setVisibility(View.GONE);

        reviewArrayList = new ArrayList<>();
        reviewStatisticArrayList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ReviewDetailsActivity.this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rcvReviewDetails.setLayoutManager(gridLayoutManager);

        reviewDetailsFoodAdapter = new ReviewDetailsFoodAdapter(ReviewDetailsActivity.this, reviewArrayList);
        rcvReviewDetails.setAdapter(reviewDetailsFoodAdapter);
        Loaddata();


        eventbutton();
    }

    private void eventbutton() {
        buttonBack.setOnClickListener(view -> finish());
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
                        reviewStatisticArrayList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);

                                JSONObject orderObject = object.getJSONObject("order_detail");
                                OrderDetails orderDetails = new OrderDetails(
                                        orderObject.getInt("id"),
                                        orderObject.getInt("product_id"),
                                        orderObject.getInt("price"),
                                        orderObject.getInt("quantity")

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
                                reviewStatisticArrayList.add(new ReviewStatistic(review.getOrderDetail().getQuantity(),review.getRating()));
                                progress_bar.setVisibility(View.GONE);

                            }
                            View.setVisibility(View.VISIBLE);
                            //tính biểu đồ
                            updateStatistics();
                            reviewDetailsFoodAdapter.notifyDataSetChanged();
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
    private void updateStatistics()
    {
        int sumReview = 0;
        int sumNumberStar = 0;
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        int num4 = 0;
        int num5 = 0;
        for (ReviewStatistic reviewStatistic : reviewStatisticArrayList) {
            sumReview += reviewStatistic.getQuantity();
            sumNumberStar += reviewStatistic.getRating() * reviewStatistic.getQuantity();
            switch (reviewStatistic.getRating()) {
                case 1:
                    num1 += reviewStatistic.getRating() * reviewStatistic.getQuantity();
                    break;
                case 2:
                    num2 += reviewStatistic.getRating() * reviewStatistic.getQuantity();
                    break;
                case 3:
                    num3 += reviewStatistic.getRating() * reviewStatistic.getQuantity();
                    break;
                case 4:
                    num4 += reviewStatistic.getRating() * reviewStatistic.getQuantity();
                    break;
                case 5:
                    num5 += reviewStatistic.getRating() * reviewStatistic.getQuantity();
                    break;
            }
        }
        numReview.setText(String.valueOf(sumReview));

        if (sumReview != 0) {
            double avg = (double) sumNumberStar / sumReview;
            double roundedAvg = Math.round(avg * 100.0) / 100.0;

            avgRating.setText(String.format("%.1f", roundedAvg));
            rBar.setRating((float) roundedAvg);
            prg1.setProgress(num1 * 100 / sumNumberStar);
            prg2.setProgress(num2 * 100 / sumNumberStar);
            prg3.setProgress(num3 * 100 / sumNumberStar);
            prg4.setProgress(num4 * 100 / sumNumberStar);
            prg5.setProgress(num5 * 100 / sumNumberStar);
        } else {
            // Nếu sumReview bằng 0, hiển thị giá trị mặc định hoặc không thực hiện gì cả
            // Ví dụ:
            avgRating.setText("N/A");
            rBar.setRating(0);
            prg1.setProgress(0);
            prg2.setProgress(0);
            prg3.setProgress(0);
            prg4.setProgress(0);
            prg5.setProgress(0);
        }

        View.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);


    }
    private void initview() {
        buttonBack = findViewById(R.id.buttonBack);
        avgRating = findViewById(R.id.avgRating);
        numReview = findViewById(R.id.numReview);
        rBar = findViewById(R.id.rBar);
        prg1 = findViewById(R.id.prg1);
        prg2 = findViewById(R.id.prg2);
        prg3 = findViewById(R.id.prg3);
        prg4 = findViewById(R.id.prg4);
        prg5 = findViewById(R.id.prg5);
        progress_bar = findViewById(R.id.progress_bar);
        rcvReviewDetails = findViewById(R.id.rcvReviewDetails);
        View= findViewById(R.id.View);
    }
}