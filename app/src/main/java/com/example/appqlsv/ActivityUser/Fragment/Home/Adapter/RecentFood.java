package com.example.appqlsv.ActivityUser.Fragment.Home.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Models.Product;
import com.example.appqlsv.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecentFood extends RecyclerView.Adapter<RecentFood.viewholder> {
    private ArrayList<Product> products;
    private Context context;

    public RecentFood(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public RecentFood.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recent_food_item,parent,false);
        return new RecentFood.viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecentFood.viewholder holder, int position) {
        Product product = products.get(position);
        DecimalFormat dcf = new DecimalFormat("###,###,###");
        holder.nameRecentFood.setText(product.getName());
        holder.priceRecentFood.setText(dcf.format(product.getPrice())+ " đ");
        holder.DesTxt.setText(product.getMota());
        Picasso.get().load(product.getImage()).into(holder.imageRecentFood);

    }


    @Override
    public int getItemCount() {
        return products.size();
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
