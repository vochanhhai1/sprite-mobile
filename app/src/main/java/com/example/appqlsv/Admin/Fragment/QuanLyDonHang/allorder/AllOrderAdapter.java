package com.example.appqlsv.Admin.Fragment.QuanLyDonHang.allorder;

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

import com.example.appqlsv.Admin.Fragment.QuanLyDonHang.orderdetail.AdminOrderDetail;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.viewholder> {
    private ArrayList<Order> orderArrayList;
    private Context context;

    public AllOrderAdapter(ArrayList<Order> orderArrayList, Context context) {
        this.orderArrayList = orderArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllOrderAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_item,parent,false);
        return new AllOrderAdapter.viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AllOrderAdapter.viewholder holder, int position) {
        Order order = orderArrayList.get(position);

        holder.orderItemName.setText(order.getBuyer() != null ? order.getBuyer().getFullname() : "");
        holder.orderItemQuantity.setText(order.getNotes());
        Picasso.get().load(order.getOrderDetails().get(0).getProduct().getImage()).into(holder.orderItemImage);
        LocalDateTime parsedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            parsedDate = LocalDateTime.parse(order.getCreatedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
        }
        holder.orderItemPrice.setText(formattedDate);


        if ("ARRIVED".equals(order.getStatus())) {
            holder.orderItemStatus.setText(R.string.ARRIVED);
            holder.orderItemStatus.setBackgroundResource(R.drawable.light_red_gradient);
        } else if ("PENDING".equals(order.getStatus())) {
            holder.orderItemStatus.setText(R.string.PENDING);
            holder.orderItemStatus.setBackgroundResource(R.drawable.light_yellow_gradient);
        } else if ("PROCESSED".equals(order.getStatus()) || "COMPLETED".equals(order.getStatus())) {
            holder.orderItemStatus.setText(R.string.PROCESSED);
            holder.orderItemStatus.setBackgroundResource(R.drawable.light_green_gradient);
        } else if ("CANCELLED".equals(order.getStatus())) {
            holder.orderItemStatus.setText(R.string.CANCELLED);
            holder.orderItemStatus.setBackgroundResource(R.drawable.light_gray_gradient);
        }

// Số lượng đặt hàng
        if (order.getOrderDetails() != null) {
            holder.orderQuantity.setText(order.getOrderDetails().size() + " " +
                    context.getResources().getString(R.string.item));
        } else {
            holder.orderQuantity.setText("0 " +
                    context.getResources().getString(R.string.item));
        }

// Giá trị đơn hàng
        DecimalFormat dec = new DecimalFormat("#,###");
        holder.orderTotalPrice.setText(dec.format(order.getTotalPrice()) + "đ");

        holder.orderlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), AdminOrderDetail.class);
                intent.putExtra("orderid", order.getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        private final ImageView orderItemImage;
        private TextView orderItemName,orderItemQuantity,orderItemPrice;
        private final MaterialButton orderItemStatus;
        private MaterialTextView orderQuantity,orderTotalPrice;
        private CardView orderlayout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            orderTotalPrice = itemView.findViewById(R.id.orderTotalPrice);
            orderQuantity = itemView.findViewById(R.id.orderQuantity);
            orderItemStatus = itemView.findViewById(R.id.orderItemStatus);
            orderItemQuantity = itemView.findViewById(R.id.orderItemQuantity);
            orderItemName = itemView.findViewById(R.id.orderItemName);
            orderItemPrice = itemView.findViewById(R.id.orderItemPrice);
            orderItemImage = itemView.findViewById(R.id.orderItemImage);
            orderlayout = itemView.findViewById(R.id.orderlayout);
        }
    }
}
