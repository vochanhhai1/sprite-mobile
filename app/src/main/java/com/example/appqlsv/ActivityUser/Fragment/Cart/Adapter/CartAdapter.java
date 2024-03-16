package com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Cart.Mycart;
import com.example.appqlsv.Models.Cart;
import com.example.appqlsv.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    private ArrayList<Cart> carts;
    private Mycart context;
    private String urlQuanlity="https://spriteee.000webhostapp.com/UpdateQuality.php";
    public CartAdapter(ArrayList<Cart> carts, Mycart context) {
        this.carts = carts;
        this.context = context;
        notifyDataSetChanged();
    }
    public Cart getCart(int position) {
        return carts.get(position);
    }
    public void deleteItem(int position) {
        // Xóa mục khỏi danh sách dữ liệu
        carts.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart_item,parent,false);
        return new CartAdapter.viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        Cart cart = carts.get(position);
        DecimalFormat dcf = new DecimalFormat("###,###,###");
        holder.cartItemName.setText(cart.getProduct().getName());
        holder.cartItemOption.setText(cart.getProduct().getMota());
        holder.cartItemQuantity.setText(cart.getQuantity()+"");
        holder.cartItemPrice.setText(dcf.format(cart.getProduct().getPrice())+ " đ");
        Picasso.get().load(cart.getProduct().getImage()).into(holder.cartItemImage);
        Mycart.calculatePrice(carts,context);
        holder.addQuantityCard.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                updateCartItem(urlQuanlity,false, cart);
                Mycart.calculatePrice(carts,context);
                notifyDataSetChanged();
            }
        });
        holder.subtractQuantityCard.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if (cart.getQuantity()>1) {
                    updateCartItem(urlQuanlity, true, cart);
                    Mycart.calculatePrice(carts,context);
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void updateCartItem(String url,boolean isAdd, final Cart cart)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context.getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                if (isAdd) {
                    cart.setQuantity(cart.getQuantity() - 1);
                } else {
                    cart.setQuantity(cart.getQuantity() + 1);
                }
                notifyDataSetChanged();
                Mycart.calculatePrice(carts, context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getActivity(),"Xảy ra lỗi",Toast.LENGTH_SHORT).show();
                Log.d("AAA","lỖI!\n"+error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                if (isAdd)
                    params.put("quantity", String.valueOf(cart.getQuantity()-1));
                else
                    params.put("quantity", String.valueOf(cart.getQuantity()+1));

                params.put("product_id", String.valueOf(cart.getProductid()));
                params.put("user_id", String.valueOf(cart.getUserid()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private MaterialCardView addQuantityCard,subtractQuantityCard;
        private MaterialTextView cartItemName,cartItemOption,cartItemPrice;
        private TextView cartItemQuantity;
        private ImageView cartItemImage;
        private CardView btnDelete;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            addQuantityCard = itemView.findViewById(R.id.addQuantityCard);
            subtractQuantityCard = itemView.findViewById(R.id.subtractQuantityCard);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemOption = itemView.findViewById(R.id.cartItemOption);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            cartItemQuantity = itemView.findViewById(R.id.cartItemQuantity);
            cartItemImage = itemView.findViewById(R.id.cartItemImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
