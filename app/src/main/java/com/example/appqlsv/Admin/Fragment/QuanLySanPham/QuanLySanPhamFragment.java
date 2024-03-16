package com.example.appqlsv.Admin.Fragment.QuanLySanPham;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Home.FilterScreen;
import com.example.appqlsv.Models.Filter;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.Models.Product;
import com.example.appqlsv.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuanLySanPhamFragment extends Fragment {
    private RecyclerView menuRCV;
    private AppCompatButton createNewBtn;
    private ImageButton filterBtn;
    private ProgressBar bar;
    private QLSPAdapter qlspAdapter;
    private ArrayList<Product> productArrayList;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private List<Float> range;
    private  Filter filter = new Filter();
    private ArrayList<Filter> filterArrayList;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qlsp, container, false);
        andxaid(view);

        productArrayList = new ArrayList<>();
        orderDetailsArrayList = new ArrayList<>();
        filterArrayList = new ArrayList<>();
        range = new ArrayList<>();
        List<Float> range = new ArrayList<>();
        range.add(0f);
        range.add(50000000f);
        filter.setRange(range);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        menuRCV.setLayoutManager(gridLayoutManager);

        qlspAdapter = new QLSPAdapter(getActivity(), orderDetailsArrayList);
        menuRCV.setAdapter(qlspAdapter);
        getProductAdmin();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String filterData = data.getStringExtra("filterdata");

                            Gson gson = new Gson();
                            Type listType = new TypeToken<Filter>(){}.getType();
                            filter = gson.fromJson(filterData, listType);
                            createAddFoodRecyclerView(filter);
                            List<Float> filteredRange = filter.getRange();

                            GetTypesearch(filter.getType(),filter.getSort(),filteredRange.get(0));
                            //GetOrdersort(filter.getSort());
                            if(filter.getType()==0)
                            {
                                getProductAdmin();
                            }
                        }
                    }
                });

        EventButton();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void createAddFoodRecyclerView(Filter filter) {
        if (filter != null) {
            if (filter.isWayUp()) {
                if (!filter.getSort().startsWith("-")) {
                    filter.setSort("-" + filter.getSort());
                }
            } else {
                if (filter.getSort().startsWith("-")) {
                    filter.setSort(filter.getSort().substring(1));
                }
            }
        }
        menuRCV.setLayoutManager(new LinearLayoutManager(getActivity()));
        menuRCV.setHasFixedSize(true);

        bar.setVisibility(View.VISIBLE);

        qlspAdapter = new QLSPAdapter(getActivity(),orderDetailsArrayList);
        menuRCV.setAdapter(qlspAdapter);

        menuRCV.setLayoutManager(new GridLayoutManager(getActivity() , 1));
        qlspAdapter.notifyDataSetChanged();
        bar.setVisibility(View.GONE);
    }
    private void EventButton() {
        createNewBtn.setOnClickListener(view -> startActivity(new Intent(getActivity(),AddSanPham.class)));

        filterBtn.setOnClickListener(view -> {

            Gson gson = new Gson();

            String json = gson.toJson(filter); // listReview là một ArrayList<Review>
            Intent intent = new Intent(getActivity(), FilterScreen.class);
            intent.putExtra("filterdata", json);
            activityResultLauncher.launch(intent);


        });
    }
    public void GetTypesearch(int type, String price,float price1)
    {
        String url ="https://spriteee.000webhostapp.com/getSearchproduct.php?query="+type+"&sort="+price+"&price="+price1;
        bar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            orderDetailsArrayList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject orderDetailsObject = response.getJSONObject(i);
                    JSONObject productObject = orderDetailsObject.getJSONObject("product");
                    String imageUrl = "https://spriteee.000webhostapp.com/" + productObject.getString("image");
                    Product product = new Product(
                            productObject.getInt("id"),
                            productObject.getString("name"),
                            productObject.getInt("price"),
                            imageUrl,
                            productObject.getInt("id_danhmuc"),
                            productObject.getInt("orderCount"),
                            productObject.getInt("averageRating"),
                            productObject.getInt("isFavorited") == 1
                    );
                    OrderDetails orderDetails = new OrderDetails(
                            product
                    );
                    orderDetailsArrayList.add(orderDetails);
                    filter.setType(orderDetails.getProduct().getId_danhmuc());
                    filterArrayList.add(filter);
                    bar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", "Error: " + e);
                }
            }

            menuRCV.setLayoutManager(new LinearLayoutManager(getActivity()));
            menuRCV.setHasFixedSize(true);

            qlspAdapter = new QLSPAdapter(getActivity(),orderDetailsArrayList);
            menuRCV.setAdapter(qlspAdapter);

            menuRCV.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            qlspAdapter.notifyDataSetChanged();
        }, error -> Log.e(TAG, "Error: " + error.getMessage()));
        requestQueue.add(jsonArrayRequest);
    }
    private void getProductAdmin()
    {
        String recentfood = "https://spriteee.000webhostapp.com/getSP_admin.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, recentfood, null, response -> {
            orderDetailsArrayList.clear();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject orderDetailsObject = response.getJSONObject(i);
                    JSONObject productObject = orderDetailsObject.getJSONObject("product");
                    String imageUrl = "https://spriteee.000webhostapp.com/" + productObject.getString("image");
                    Product product = new Product(
                            productObject.getInt("id"),
                            productObject.getString("name"),
                            productObject.getInt("price"),
                            imageUrl,
                            productObject.getInt("id_danhmuc"),
                            productObject.getInt("orderCount"),
                            productObject.getInt("averageRating"),
                            productObject.getInt("isFavorited") == 1
                    );
                    productArrayList.add(product);
                    OrderDetails orderDetails = new OrderDetails(
                            product
                    );
                    orderDetailsArrayList.add(orderDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", "Error: " + e);
                }

            }

            qlspAdapter.notifyDataSetChanged();
        }, error -> {
            Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("VolleyError", "Error: " + error);
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProductAdmin();
    }

    private void andxaid(View view) {
        menuRCV = view.findViewById(R.id.menuRCV);
        createNewBtn= view.findViewById(R.id.createNewBtn);
        bar = view.findViewById(R.id.bar);
        filterBtn = view.findViewById(R.id.filterBtn);

    }
}
