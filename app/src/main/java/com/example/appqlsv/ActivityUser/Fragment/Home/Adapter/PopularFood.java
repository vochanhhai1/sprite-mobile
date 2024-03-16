package com.example.appqlsv.ActivityUser.Fragment.Home.Adapter;

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

import com.example.appqlsv.ActivityUser.Fragment.Home.infoFood;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PopularFood extends RecyclerView.Adapter<PopularFood.viewholder> {
    private ArrayList<OrderDetails> productArrayList;
    private Context context;


    public PopularFood(ArrayList<OrderDetails> productArrayList, Context context) {
        this.productArrayList = productArrayList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PopularFood.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_food_item,parent,false);
        return new PopularFood.viewholder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PopularFood.viewholder holder, int position) {
        //Product product = productArrayList.get(position);
        OrderDetails orderDetails = productArrayList.get(position);
        DecimalFormat dec = new DecimalFormat("#,###");

        Picasso.get().load(orderDetails.getProduct().getImage()).into(holder.imageFood);
        holder.nameFood.setText(orderDetails.getProduct().getName());

        holder.iconButton.setBackgroundResource(R.drawable.ic_add);
        holder.price.setText(dec.format(orderDetails.getProduct().getPrice()) + "đ");
        holder.numberProduct.setText(context.getResources().getString(R.string.Sold) + " " + orderDetails.getProduct().getOrderCount());
        holder.cardView3.setOnClickListener(new View.OnClickListener() {
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
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private ImageView imageFood , iconButton;
        private TextView nameFood,price,numberProduct;
        private androidx.cardview.widget.CardView cardView3;
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
