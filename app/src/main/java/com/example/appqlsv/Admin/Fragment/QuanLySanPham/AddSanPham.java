package com.example.appqlsv.Admin.Fragment.QuanLySanPham;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddSanPham extends AppCompatActivity {

    private FrameLayout backBtn;
    private EditText productName, productPrice, productDes;
    private ImageButton productImage;
    private Button finishbtn;
    private Bitmap bitmap;
    private String base64Image;
    private TextView productType;
    private int selectedType;

    private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_san_pham);
        anhxaid();
        EventButton();
        initSpinner();
    }
    private void initSpinner() {
        productType.setOnClickListener(v -> {
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(AddSanPham.this);
            builder.setTitle(getResources().getString(R.string.Filtertheorders));

            // add a list
            String[] status = new String[]{
                    getResources().getString(R.string.FOOD),
                    getResources().getString(R.string.DRINK)
            };
            builder.setItems(status, (dialog, which) -> {
                switch (which) {
                    case 0:
                        selectedType = 1;
                        productType.setText(getResources().getString(R.string.FOOD));
                        break;
                    case 1:
                        selectedType = 2;
                        productType.setText(getResources().getString(R.string.DRINK));
                        break;
                }
            });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }


    private void EventButton() {
        backBtn.setOnClickListener(view -> finish());
        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            productImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        productImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });
        finishbtn.setOnClickListener(view -> {
            ByteArrayOutputStream byteArrayOutputStream;
            byteArrayOutputStream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
            } else
                Toast.makeText(getApplicationContext(), "Vui lòng chọn ảnh!!", Toast.LENGTH_SHORT).show();

            String tensp = productName.getText().toString().trim();
            String gia = productPrice.getText().toString().trim();
            String mota = productDes.getText().toString().trim();
            if (tensp.isEmpty() | gia.isEmpty() | mota.isEmpty()) {
                Toast.makeText(AddSanPham.this, "Vui lòng nhập đủ thông tin!!", Toast.LENGTH_SHORT).show();
            } else {

                ThemSanPham();
            }
        });
    }

    private void ThemSanPham() {

        String urlinsert = "https://spriteee.000webhostapp.com/insertQLSP.php";
        bar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlinsert, response -> {
            if (response.trim().equals("success")) {
                Toast.makeText(AddSanPham.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                finish();
                bar.setVisibility(View.GONE);
            } else {
                Toast.makeText(AddSanPham.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        },
                error -> {
                    Toast.makeText(AddSanPham.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                    Log.d("AAA", "lỖI!\n" + error.toString());
                }
        ) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", productName.getText().toString().trim());
                params.put("price", productPrice.getText().toString().trim());
                params.put("image", base64Image);
                params.put("mota", productDes.getText().toString().trim());
                params.put("id_danhmuc", String.valueOf(selectedType));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void anhxaid() {
        backBtn = findViewById(R.id.backBtn);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDes = findViewById(R.id.productDes);
        productImage = findViewById(R.id.productImage);
        finishbtn = findViewById(R.id.finishbtn);
        productType = findViewById(R.id.productType);
        bar = findViewById(R.id.bar);
    }
}