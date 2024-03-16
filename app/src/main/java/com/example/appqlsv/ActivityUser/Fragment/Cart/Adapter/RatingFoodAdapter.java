package com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Models.Review;
import com.example.appqlsv.R;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RatingFoodAdapter extends RecyclerView.Adapter<RatingFoodAdapter.viewholer> {
    private final Context context;
    private final ArrayList<Review> reviewArrayList;

    public RatingFoodAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public RatingFoodAdapter.viewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rating_food_item,parent,false);
        return new viewholer(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RatingFoodAdapter.viewholer holder, int position) {
        Review review = reviewArrayList.get(position);

        DecimalFormat dec = new DecimalFormat("#,###");
        Picasso.get().load(review.getOrderDetail().getProduct().getImage()).into(holder.ratingFoodImage);
        holder.ratingFoodName.setText(review.getOrderDetail().getProduct().getName());
        holder.ratingFoodTitle.setText(review.getOrderDetail().getProduct().getMota());
        holder.ratingFoodPrice.setText(dec.format(review.getOrderDetail().getProduct().getPrice())+" đ");
        holder.ratingFoodQuantity.setText("x " + review.getOrderDetail().getQuantity());
        holder.ratingFoodBar.setStepSize(1f);
        holder.ratingFoodBar.setRating(5f);
        holder.ratingFoodBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> review.setRating((int) rating));
        holder.ratingFoodReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                review.setContent(holder.ratingFoodReview.getText().toString());
            }
        });

    }
    public ArrayList<Review> getReviews() {
        return reviewArrayList;
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public static class viewholer extends RecyclerView.ViewHolder {
        private final ImageView ratingFoodImage;
        private final MaterialTextView ratingFoodName;
        private final MaterialTextView ratingFoodTitle;
        private final MaterialTextView ratingFoodPrice;
        private final TextView ratingFoodQuantity;
        private final RatingBar ratingFoodBar;
        private final EditText ratingFoodReview;
        public viewholer(@NonNull View itemView) {
            super(itemView);
            ratingFoodImage = itemView.findViewById(R.id.ratingFoodImage);
            ratingFoodName = itemView.findViewById(R.id.ratingFoodName);
            ratingFoodTitle = itemView.findViewById(R.id.ratingFoodTitle);
            ratingFoodPrice = itemView.findViewById(R.id.ratingFoodPrice);
            ratingFoodQuantity = itemView.findViewById(R.id.ratingFoodQuantity);
            ratingFoodBar = itemView.findViewById(R.id.ratingFoodBar);
            ratingFoodReview = itemView.findViewById(R.id.ratingFoodReview);
        }
    }
}
