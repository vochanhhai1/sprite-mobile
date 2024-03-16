package com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.ActivityUser.Fragment.Cart.OrderDetail;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.wiewholder> {
    private Context context;
    private ArrayList<Order> orderArrayList;

    public OrderAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderAdapter.wiewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_item,parent,false);
        return new OrderAdapter.wiewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.wiewholder holder, int position) {
        Order order = orderArrayList.get(position);
        DecimalFormat dec = new DecimalFormat("#,###");


        Picasso.get().load(order.getOrderDetails().get(0).getProduct().getImage()).into(holder.orderItemImage);
        holder.orderTotalPrice.setText(dec.format(order.getTotalPrice())+" đ");
        holder.orderItemName.setText(order.getOrderDetails().get(0).getProduct().getName());
        holder.orderQuantity.setText(order.getOrderDetails().size()+" "+ context.getResources().getString(R.string.item));
        holder.orderItemPrice.setText(dec.format(order.getOrderDetails().get(0).getProduct().getPrice()) + "đ");
        holder.orderItemQuantity.setText("x" + order.getOrderDetails().get(0).getQuantity());
        holder.orderItemStatus.setText(order.getStatus());

        holder.orderlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetail.class);
                intent.putExtra("orderid", order.getId());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class wiewholder extends RecyclerView.ViewHolder {
        private ImageView orderItemImage;
        private TextView orderItemName,orderItemQuantity,orderItemPrice;
        private MaterialTextView orderQuantity,orderTotalPrice;
        private MaterialButton orderItemStatus;
        private CardView orderlayout;
        public wiewholder(@NonNull View itemView) {
            super(itemView);
            orderItemImage = itemView.findViewById(R.id.orderItemImage);
            orderItemName = itemView.findViewById(R.id.orderItemName);
            orderItemQuantity = itemView.findViewById(R.id.orderItemQuantity);
            orderItemPrice = itemView.findViewById(R.id.orderItemPrice);
            orderQuantity = itemView.findViewById(R.id.orderQuantity);
            orderTotalPrice = itemView.findViewById(R.id.orderTotalPrice);
            orderItemStatus = itemView.findViewById(R.id.orderItemStatus);
            orderlayout = itemView.findViewById(R.id.orderlayout);
        }
    }
}
