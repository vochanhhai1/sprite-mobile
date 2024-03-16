package com.example.appqlsv.ActivityUser.Fragment.Cart;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Cart.Adapter.CartAdapter;
import com.example.appqlsv.Models.Cart;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Mycart extends Fragment {
    private RecyclerView rv_cart;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartArrayList;
    private ProgressBar bar;
    private String urlCart;
    private MaterialTextView totalPrice;
    private MaterialButton cartButton;
    private EditText cartNote;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart_user, container, false);

        anhxaid(view);
        cartArrayList = new ArrayList<>();
        UserSessionManager userSessionManager = new UserSessionManager(requireActivity());
        userId = userSessionManager.getUserId();
        urlCart = "https://spriteee.000webhostapp.com/getCartfromUser.php?idu=" + userId;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rv_cart.setLayoutManager(gridLayoutManager);

        cartAdapter = new CartAdapter(cartArrayList, this);
        rv_cart.setAdapter(cartAdapter);
        loadData();
        Detedata();
        eventButton();
        return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    private void eventButton() {
        cartButton.setOnClickListener(view -> {

            if (cartArrayList.isEmpty()) {
                Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage(getResources().getString(R.string.CreateOrderMessage));
            builder.setPositiveButton(getResources().getString(R.string.YES), (dialog, which) -> {
                int totalPrice = calculatePrice(cartArrayList);
                //insert order và orderdetails
                InsertOrder(userId, cartNote.getText().toString(), totalPrice, cartArrayList);

                if (cartArrayList != null) {
                    cartArrayList.clear();
                    cartAdapter = new CartAdapter(cartArrayList, Mycart.this);
                    rv_cart.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_cart.setAdapter(cartAdapter);
                }
                cartNote.getText().clear();
                dialog.dismiss();

            });
            builder.setNegativeButton(getResources().getString(R.string.NO), (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.setOnShowListener(dialog -> {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            });
            alert.show();
        });
    }


    @SuppressLint("SetTextI18n")
    public static void calculatePrice(ArrayList<Cart> cartArrayList, Mycart context) {
        int totalPrice = 0;
        for (Cart cart : cartArrayList) {
            totalPrice += cart.getQuantity() * Integer.parseInt(String.valueOf(cart.getProduct().getPrice()));
        }
        DecimalFormat dec = new DecimalFormat("#,###");
        context.totalPrice.setText(dec.format(totalPrice) + "đ");
    }

    public static int calculatePrice(ArrayList<Cart> cartArrayList) {
        int totalPrice = 0;
        for (Cart cart : cartArrayList) {
            totalPrice += cart.getQuantity() * Integer.parseInt(String.valueOf(cart.getProduct().getPrice()));
        }
        return totalPrice;
    }


    private void Detedata() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Lấy vị trí của mục đã swipe
                int position = viewHolder.getAdapterPosition();
                Cart cart = cartAdapter.getCart(position);
                // Xóa mục khỏi RecyclerView
                cartAdapter.deleteItem(position);
                DeleteCartItem(cart);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
                        .addActionIcon(R.drawable.ic_trash)
                        .addSwipeRightLabel("Delete")
                        .setSwipeRightLabelColor(ContextCompat.getColor(requireActivity(), R.color.white))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rv_cart);
    }

    private void DeleteCartItem(final Cart cart) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://spriteee.000webhostapp.com/DeleteCart.php", response -> {
            if (response.trim().equals("success")) {
                totalPrice.setText("0 đ");
                Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                Log.d("VolleyError", "Error: " + response);
            }
            cartAdapter.notifyDataSetChanged();
        }, error -> {
            Toast.makeText(getActivity(), "JSON parsing error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("VolleyError", "Error: " + error);
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", String.valueOf(cart.getProductid()));
                params.put("quantity", String.valueOf(cart.getQuantity()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadData() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlCart, null,
                response -> {
                    cartArrayList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);
                            JSONObject productObject = object.getJSONObject("product");
                            String urlimage = "https://spriteee.000webhostapp.com/" + productObject.getString("image");
                            Product product = new Product(
                                    productObject.getInt("id"),
                                    productObject.getString("name"),
                                    productObject.getInt("price"),
                                    urlimage,
                                    productObject.getString("mota"),
                                    productObject.getString("createAt"),
                                    productObject.getString("deleteAt"),
                                    productObject.getInt("id_danhmuc"),
                                    productObject.getInt("isFavorited")==1
                            );
                            Cart cart = new Cart(
                                    object.getInt("idct"),
                                    object.getInt("userid"),
                                    object.getInt("productid"),
                                    object.getInt("quantity"),
                                    product
                            );
                            cartArrayList.add(cart);

                        }

                        cartAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show());
        requestQueue.add(jsonArrayRequest);
    }

    private void InsertOrder(int user, String notes, int totalprice, ArrayList<Cart> cartArrayList) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        bar.setVisibility(ProgressBar.VISIBLE);

        JSONArray productsArray = new JSONArray();
        for (Cart cart : cartArrayList) {
            JSONObject productObject = new JSONObject();
            try {
                productObject.put("productid", cart.getProductid());
                productObject.put("quantity", cart.getQuantity());
                productObject.put("price", cart.getQuantity() * cart.getProduct().getPrice());
                productsArray.put(productObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://spriteee.000webhostapp.com/InserttoOrder.php",
                response -> {
                    if (response.trim().equals("success")) {
                        totalPrice.setText("0đ");
                        Toast.makeText(getActivity(), "SUCCESS create order", Toast.LENGTH_SHORT).show();
                        bar.setVisibility(ProgressBar.GONE);
                        clearCart();
                    } else {
                        Toast.makeText(getActivity(), "Error: " + response, Toast.LENGTH_SHORT).show();
                        Log.d("VolleyError", "Error: " + response);
                    }
                }, error -> {
                    Toast.makeText(getActivity(), "JSON parsing error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "JSON parsing error: " + error.getMessage());
                }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("buyer", String.valueOf(user));
                params.put("notes", notes);
                params.put("totalPrice", String.valueOf(totalprice));
                params.put("products", productsArray.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void clearCart() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        String url = "https://spriteee.000webhostapp.com//ResetCart.php";
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        cartArrayList.clear();
                        cartAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Error occurred while clearing cart", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Toast.makeText(getActivity(), "Error occurred while clearing cart: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("VolleyError", "Error: " + error.getMessage());
                }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void anhxaid(View view) {
        rv_cart = view.findViewById(R.id.rv_cart);
        totalPrice = view.findViewById(R.id.totalPrice);
        bar = view.findViewById(R.id.bar);
        cartButton = view.findViewById(R.id.cartButton);
        cartNote = view.findViewById(R.id.cartNote);
    }
}

