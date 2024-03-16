package com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.R;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderdetailAdapter extends RecyclerView.Adapter<OrderdetailAdapter.viewholder> {
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private Context context;

    public OrderdetailAdapter(ArrayList<OrderDetails> orderDetailsArrayList, Context context) {
        this.orderDetailsArrayList = orderDetailsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderdetailAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_food_item,parent,false);
        return new OrderdetailAdapter.viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderdetailAdapter.viewholder holder, int position) {
        OrderDetails orderDetails = orderDetailsArrayList.get(position);
        DecimalFormat dec = new DecimalFormat("#,###");

        Picasso.get().load(orderDetails.getProduct().getImage()).into(holder.foodItemImage);
        holder.foodItemName.setText(orderDetails.getProduct().getName());
        holder.foodItemTitle.setText(orderDetails.getProduct().getMota());
        holder.foodItemPrice.setText(dec.format(orderDetails.getPrice()) + "đ");
        holder.foodItemQuantity.setText("x " + orderDetails.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderDetailsArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private ImageView foodItemImage;
        private MaterialTextView foodItemName,foodItemTitle,foodItemPrice;
        private TextView foodItemQuantity;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            foodItemImage =  itemView.findViewById(R.id.foodItemImage);
            foodItemName =  itemView.findViewById(R.id.foodItemName);
            foodItemTitle =  itemView.findViewById(R.id.foodItemTitle);
            foodItemPrice =  itemView.findViewById(R.id.foodItemPrice);
            foodItemQuantity =  itemView.findViewById(R.id.foodItemQuantity);
        }
    }
}
