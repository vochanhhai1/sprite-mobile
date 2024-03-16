package com.example.appqlsv.ActivityUser.Fragment.proflle.profilsetting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.viewholder> {
    private ArrayList<OrderDetails> productArrayList;
    private Context context;

    public FavoriteAdapter(ArrayList<OrderDetails> productArrayList, Context context) {
        this.productArrayList = productArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_favorite_item,parent,false);
        return new FavoriteAdapter.viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.viewholder holder, int position) {
        OrderDetails orderDetails = productArrayList.get(position);



        Picasso.get().load(orderDetails.getProduct().getImage()).into(holder.imageFavorite);
        holder.nameFavorite.setText(orderDetails.getProduct().getName());
        holder.description.setText(orderDetails.getProduct().getMota());
        DecimalFormat dec = new DecimalFormat("#,###");
        holder.price.setText(dec.format(orderDetails.getProduct().getPrice()) + "đ");

        holder.buyAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSessionManager userSessionManager = new UserSessionManager(context);
                addToCart(userSessionManager.getUserId(),orderDetails.getProductId());
            }
        });

    }
    private void addToCart(int user ,int productId) {
        String urlAddtocart = "https://spriteee.000webhostapp.com/AddtoCart.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAddtocart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                    Log.d("AAA", "lỖI!\n" + response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "lỖI!\n" + error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("product_id", String.valueOf(productId));
                params.put("user_id", String.valueOf(user));
                params.put("quantity", "1");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private ImageView imageFavorite;
        private TextView nameFavorite,description,price;
        private AppCompatButton buyAgain;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            buyAgain = itemView.findViewById(R.id.buyAgain);
            imageFavorite = itemView.findViewById(R.id.imageFavorite);
            nameFavorite = itemView.findViewById(R.id.nameFavorite);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
        }
    }
}
