package com.example.appqlsv.Admin.Fragment.QuanLyDonHang.orderdetail;

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

public class AdminOrderDetailAdapter extends RecyclerView.Adapter<AdminOrderDetailAdapter.viewholder> {
    private Context context;
    private ArrayList<OrderDetails> orderDetailsArrayList;

    public AdminOrderDetailAdapter(Context context, ArrayList<OrderDetails> orderDetailsArrayList) {
        this.context = context;
        this.orderDetailsArrayList = orderDetailsArrayList;
    }

    @NonNull
    @Override
    public AdminOrderDetailAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_admin_order_detail,parent,false);
        return new AdminOrderDetailAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderDetailAdapter.viewholder holder, int position) {
        OrderDetails orderDetails = orderDetailsArrayList.get(position);


        Picasso.get().load(orderDetails.getProduct().getImage()).into(holder.foodItemImage);

        holder.foodItemName.setText(orderDetails.getProduct().getName());
        holder.foodItemTitle.setText(orderDetails.getProduct().getMota());

        DecimalFormat dec = new DecimalFormat("#,###");
        String formattedPrice = dec.format(orderDetails.getProduct().getPrice()) + "đ";
        holder.foodItemPrice.setText(formattedPrice);
        String quantityString = context.getResources().getString(R.string.Qty) + orderDetails.getQuantity();
        holder.foodItemQuantity.setText(quantityString);

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
            foodItemImage= itemView.findViewById(R.id.foodItemImage);
            foodItemName= itemView.findViewById(R.id.foodItemName);
            foodItemTitle= itemView.findViewById(R.id.foodItemTitle);
            foodItemPrice= itemView.findViewById(R.id.foodItemPrice);
            foodItemQuantity= itemView.findViewById(R.id.foodItemQuantity);
        }
    }
}
