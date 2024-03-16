package com.example.appqlsv.Admin.Fragment.QuanLySanPham;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QLSPAdapter extends RecyclerView.Adapter<QLSPAdapter.viewholder> {
    private final Context context;
    private final ArrayList<OrderDetails> orderDetailsArrayList;

    @SuppressLint("NotifyDataSetChanged")
    public QLSPAdapter(Context context, ArrayList<OrderDetails> orderDetailsArrayList) {
        this.context = context;
        this.orderDetailsArrayList = orderDetailsArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QLSPAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_food_item,parent,false);
        return new viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull QLSPAdapter.viewholder holder, int position) {
        OrderDetails orderDetails = orderDetailsArrayList.get(position);
        DecimalFormat dec = new DecimalFormat("#,###");
        holder.nameFood.setText(orderDetails.getProduct().getName());
        holder.price.setText(dec.format(orderDetails.getProduct().getPrice())+ " đ");
        holder.numberProduct.setText(context.getResources().getString(R.string.Sold) + " " + orderDetails.getProduct().getOrderCount());
        holder.iconButton.setBackgroundResource(R.drawable.ic_edit);
        holder.iconButton.setScaleX(0.5f);
        holder.iconButton.setScaleY(0.5f);
        Picasso.get().load(orderDetails.getProduct().getImage()).into(holder.imageFood);
        holder.iconButton.setOnClickListener(view -> getDatatoUpdate(orderDetails));
    }

    private void getDatatoUpdate(OrderDetails orderDetails) {
        Intent intent =new Intent(context, UpdateSanPham.class);
        intent.putExtra("OrderDetails", orderDetails);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return orderDetailsArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        private final ImageView imageFood;
        private final ImageView iconButton;
        private final TextView nameFood;
        private final TextView price;
        private final TextView numberProduct;
        private final androidx.cardview.widget.CardView cardView3;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageFood= itemView.findViewById(R.id.imageFood);
            nameFood= itemView.findViewById(R.id.nameFood);
            numberProduct= itemView.findViewById(R.id.numberProduct);
            price= itemView.findViewById(R.id.price);
            cardView3= itemView.findViewById(R.id.cardView3);
            iconButton = itemView.findViewById(R.id.iconButton);
        }
    }
}
