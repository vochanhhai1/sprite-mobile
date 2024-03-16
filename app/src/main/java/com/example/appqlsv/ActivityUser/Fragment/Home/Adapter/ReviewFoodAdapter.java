package com.example.appqlsv.ActivityUser.Fragment.Home.Adapter;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Models.Review;
import com.example.appqlsv.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ReviewFoodAdapter extends RecyclerView.Adapter<ReviewFoodAdapter.viewholder> {
    private Context context;
    private ArrayList<Review> reviewArrayList;

    public ReviewFoodAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public ReviewFoodAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review_food_item,parent,false);
        return new ReviewFoodAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewFoodAdapter.viewholder holder, int position) {
        Review review =reviewArrayList.get(position);

        holder.namePerson.setText(review.getUser().getFullname());
        Picasso.get().load(review.getUser().getPhoto()).into(holder.avatarPerson);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date = inputDateFormat.parse(review.getCreatedAt());
            String formattedDate = outputDateFormat.format(date);
            holder.date.setText(formattedDate);
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //holder.date.setText(review.getCreatedAt());

        holder.evaluate.setText(review.getContent());
        holder.numberStar.setText(String.valueOf(review.getRating()));
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private ImageView avatarPerson;
        private TextView namePerson,date,evaluate,numberStar;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            avatarPerson = itemView.findViewById(R.id.avatarPerson);
            namePerson = itemView.findViewById(R.id.namePerson);
            date = itemView.findViewById(R.id.date);
            evaluate = itemView.findViewById(R.id.evaluate);
            numberStar = itemView.findViewById(R.id.numberStar);
        }
    }
}
