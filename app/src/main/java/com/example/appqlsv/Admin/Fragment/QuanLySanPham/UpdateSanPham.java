package com.example.appqlsv.Admin.Fragment.QuanLySanPham;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.OrderDetails;
import com.example.appqlsv.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateSanPham extends AppCompatActivity {
    private MaterialCardView btnDelete;
    private EditText upproductName,upproductPrice,upproductDes;
    private ImageButton upproductImage;
    private AppCompatButton upfinishbtn;
    private FrameLayout upbackBtn;
    private TextView upproductType;
    private Bitmap bitmap;
    private String base64Image;
    private int id, price;
    private String name, des;
    private String currentImage;
    private ProgressBar bar;
    private final List<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private final String updateurl = "https://spriteee.000webhostapp.com/updateQLSP.php";
    private final String urlDelete = "https://spriteee.000webhostapp.com/deleteQLSP.php";
    private final String url = "https://spriteee.000webhostapp.com/getSP_admin.php";
    private int idkhachhang,idkhach;
    private String tendanhmuc;
    private int selectedType;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_san_pham);
        anhxaid();

        Intent intent = getIntent();
        OrderDetails orderDetails = (OrderDetails) intent.getSerializableExtra("OrderDetails");
        DecimalFormat dcf = new DecimalFormat("###,###,###");
        name = orderDetails.getProduct().getName();
        price = orderDetails.getProduct().getPrice();
        des = orderDetails.getProduct().getMota();

        id = orderDetails.getProduct().getId();
        idkhach = orderDetails.getProduct().getId_danhmuc();
        upproductName.setText(name);
        upproductPrice.setText(dcf.format(price)+ " đ");
        upproductDes.setText(des);
        currentImage = orderDetails.getProduct().getImage();
        Picasso.get().load(currentImage).into(upproductImage);
        if (idkhach ==1)
        {
            upproductType.setText(getResources().getString(R.string.FOOD));
        }else
        {
            upproductType.setText(getResources().getString(R.string.DRINK));
        }
        initSpinner();
        EventButton();
    }
    private void initSpinner() {
        upproductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateSanPham.this);
                builder.setTitle(getResources().getString(R.string.Filtertheorders));

                // add a list
                String[] status = new String[]{
                        getResources().getString(R.string.FOOD),
                        getResources().getString(R.string.DRINK)
                };
                builder.setItems(status, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                selectedType = 1;
                                upproductType.setText(getResources().getString(R.string.FOOD));
                                break;
                            case 1:
                                selectedType = 2;
                                upproductType.setText(getResources().getString(R.string.DRINK));
                                break;
                        }
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private void EventButton() {
        //delete
        btnDelete.setOnClickListener(view -> {
            xacnhanxoa(id);
        });


        upbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            upproductImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        upproductImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });
        upfinishbtn.setOnClickListener(view -> {
            ByteArrayOutputStream byteArrayOutputStream;
            byteArrayOutputStream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

            } else
                Toast.makeText(getApplicationContext(), "Vui lòng chọn ảnh!!", Toast.LENGTH_SHORT).show();
            String tensp = upproductName.getText().toString().trim();
            String giasp = upproductPrice.getText().toString().trim();
            String motasp = upproductDes.getText().toString().trim();
            if (tensp.isEmpty() | giasp.isEmpty() | motasp.isEmpty()) {
                Toast.makeText(UpdateSanPham.this, "Vui lòng nhập đủ thông tin!!", Toast.LENGTH_SHORT).show();
            } else {
                UpdateSanPham(updateurl);
            }

        });

    }
// lỗi phải nhập đủ thông số mới được success
    @SuppressLint("NotConstructor")
    private void UpdateSanPham(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        bar.setVisibility(ProgressBar.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if(response.trim().equals("success"))
            {
                Toast.makeText(UpdateSanPham.this,"Update thành công",Toast.LENGTH_SHORT).show();
                bar.setVisibility(ProgressBar.GONE);
                finish();
            }else
            {
                Toast.makeText(UpdateSanPham.this,"Lỗi",Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(UpdateSanPham.this,"Xảy ra lỗi",Toast.LENGTH_SHORT).show();
            Log.d("AAA","lỖI!\n"+error.toString());
        }
        ){
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                String productName = upproductName.getText().toString().trim();
                params.put("name", name);
                if (!productName.isEmpty()) {
                    params.put("name", productName);
                }
                String productPrice = upproductPrice.getText().toString().trim();
                params.put("price", String.valueOf(price));
                if (!productPrice.isEmpty()) {
                    params.put("price", productPrice);
                }
                params.put("image", currentImage);
                if (base64Image != null && !base64Image.isEmpty()) {
                    params.put("image", base64Image); // Cập nhật hình ảnh mới nếu có
                }

                String productDescription = upproductDes.getText().toString().trim();
                params.put("mota", des);
                if (!productDescription.isEmpty()) {
                    params.put("mota", productDescription); // Cập nhật mô tả sản phẩm mới nếu có
                }
                params.put("id_danhmuc", String.valueOf(selectedType));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void DeleteQLSP(int idsp)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        bar.setVisibility(ProgressBar.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success"))
                {
                    Toast.makeText(UpdateSanPham.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                    bar.setVisibility(ProgressBar.GONE);
                }else
                {
                    Toast.makeText(UpdateSanPham.this,"Lỗi",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateSanPham.this,"Xảy ra lỗi",Toast.LENGTH_SHORT).show();
                Log.d("AAA","lỖI!\n"+error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void xacnhanxoa(int idsp)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cảnh báo");
        builder.setIcon(R.drawable.baseline_error_24);

        builder.setMessage("Bạn có muốn xóa sản phẩm này không?");
        builder.setPositiveButton("Có chứ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteQLSP(idsp);
               finish();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();
    }
    private void anhxaid() {
        btnDelete = findViewById(R.id.btnDelete);
        upproductName = findViewById(R.id.upproductName);
        upproductPrice = findViewById(R.id.upproductPrice);
        upproductDes = findViewById(R.id.upproductDes);
        upproductImage = findViewById(R.id.upproductImage);
        upfinishbtn = findViewById(R.id.upfinishbtn);
        upbackBtn = findViewById(R.id.upbackBtn);
        upproductType = findViewById(R.id.upproductType);
        bar = findViewById(R.id.bar);
    }
}