package com.example.appqlsv.ActivityUser.Fragment.Home.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Models.Review;
import com.example.appqlsv.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ReviewDetailsFoodAdapter extends RecyclerView.Adapter<ReviewDetailsFoodAdapter.viewholder> {
    private final Context context;
    private final ArrayList<Review> reviewArrayList;

    public ReviewDetailsFoodAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public ReviewDetailsFoodAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review_details_food_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewDetailsFoodAdapter.viewholder holder, int position) {
        Review review = reviewArrayList.get(position);

        holder.namePerson.setText(review.getUser().getFullname());
        holder.description.setText(review.getContent());
        holder.rBar.setRating(review.getRating().floatValue());
        Picasso.get().load(review.getUser().getPhoto()).into(holder.imagePerson);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date = inputDateFormat.parse(review.getCreatedAt());
            String formattedDate = outputDateFormat.format(date);
            holder.createAtTxt.setText(formattedDate);
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        private final TextView namePerson;
        private final TextView createAtTxt;
        private final TextView description;
        private final ImageView imagePerson;
        private final RatingBar rBar;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            namePerson = itemView.findViewById(R.id.namePerson);
            createAtTxt = itemView.findViewById(R.id.createAtTxt);
            description = itemView.findViewById(R.id.description);
            rBar = itemView.findViewById(R.id.rBar);
        }
    }
}
