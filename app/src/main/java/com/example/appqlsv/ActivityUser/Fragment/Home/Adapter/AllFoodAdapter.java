package com.example.appqlsv.ActivityUser.Fragment.Home.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.ActivityUser.Fragment.Home.infoFood;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AllFoodAdapter extends RecyclerView.Adapter<AllFoodAdapter.viewholder> {
    private Context context;
    private ArrayList<OrderDetails> productArrayList;
    @SuppressLint("NotifyDataSetChanged")
    public AllFoodAdapter(Context context, ArrayList<OrderDetails> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public AllFoodAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_all_recent_food_item,parent,false);
        return new AllFoodAdapter.viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AllFoodAdapter.viewholder holder, int position) {
        OrderDetails orderDetails = productArrayList.get(position);
        DecimalFormat dec = new DecimalFormat("#,###");

        Picasso.get().load(orderDetails.getProduct().getImage()).into(holder.imageFood);
        holder.price.setText(dec.format(orderDetails.getProduct().getPrice()) + "đ");
        holder.numberProduct.setText(context.getResources().getString(R.string.Sold) + " " + orderDetails.getProduct().getOrderCount());
        holder.nameFood.setText(orderDetails.getProduct().getName());
        holder.rBar.setRating((float) orderDetails.getProduct().getAverageRating());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetdataToinfoFood(orderDetails);
            }
        });
    }
    private void GetdataToinfoFood(OrderDetails orderDetails)
    {
        Intent intent =new Intent(context, infoFood.class);
        intent.putExtra("OrderDetails", orderDetails);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private ImageView imageFood;
        private TextView nameFood,numberProduct,price;
        private RatingBar rBar;

        private CardView layout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.imageFood);
            nameFood = itemView.findViewById(R.id.nameFood);
            numberProduct = itemView.findViewById(R.id.numberProduct);
            price = itemView.findViewById(R.id.price);
            rBar = itemView.findViewById(R.id.rBar);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
