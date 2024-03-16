package com.example.appqlsv.Admin.Fragment.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Admin.Fragment.QuanLyDonHang.orderdetail.AdminOrderDetail;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ArrivedOrderAdapter extends RecyclerView.Adapter<ArrivedOrderAdapter.viewholder> {
    private Context context;
    private ArrayList<Order> orderArrayList;

    public ArrivedOrderAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public ArrivedOrderAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recent_food_item,parent,false);
        return new ArrivedOrderAdapter.viewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ArrivedOrderAdapter.viewholder holder, int position) {
        Order order = orderArrayList.get(position);

        Picasso.get().load(order.getOrderDetails().get(0).getProduct().getImage()).into(holder.imageRecentFood);
        holder.nameRecentFood.setText(order.getBuyer().getFullname());

        DecimalFormat dec = new DecimalFormat("#,###");
        holder.priceRecentFood.setText(dec.format(order.getTotalPrice()) + "đ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDate = LocalDateTime.parse(order.getCreatedAt(), formatter);
        String formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
        holder.DesTxt.setText(formattedDate);

        holder.recentfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public class viewholder extends RecyclerView.ViewHolder {

        private TextView nameRecentFood,DesTxt,priceRecentFood;
        private ImageView imageRecentFood;
        private CardView recentfood;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            nameRecentFood= itemView.findViewById(R.id.nameRecentFood);
            DesTxt= itemView.findViewById(R.id.DesTxt);
            priceRecentFood= itemView.findViewById(R.id.priceRecentFood);
            imageRecentFood= itemView.findViewById(R.id.imageRecentFood);
            recentfood= itemView.findViewById(R.id.recentfood);
        }
    }
}
