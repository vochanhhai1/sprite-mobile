package com.example.appqlsv.ActivityUser.Fragment.proflle.profilsetting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BioScreen extends AppCompatActivity {
    private FrameLayout backBtn;
    private EditText Studentid, Address, phonenum;
    private ImageButton circleavatar;
    private AppCompatButton finishbtn;
    private Bitmap bitmap;
    private String base64Image;
    private int id;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bio_screen);
        UserSessionManager userSessionManager = new UserSessionManager(this);
        id = userSessionManager.getUserId();
        intview();
        pickimager();
        eventbutton();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, bạn có thể sử dụng máy ảnh ở đây
            } else {
                // Người dùng từ chối cấp quyền, bạn cần thông báo hoặc xử lý sao cho phù hợp
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void eventbutton() {
        backBtn.setOnClickListener(view -> finish());
        finishbtn.setOnClickListener(view -> confirmRegister());
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void pickimager() {
        ActivityResultLauncher<Intent> activityResultLauncherForGallery =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                circleavatar.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

        ActivityResultLauncher<Intent> activityResultLauncherForCamera =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            circleavatar.setImageBitmap(imageBitmap);
                        }
                    }
                });

        circleavatar.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BioScreen.this);
            builder.setTitle("Choose option")
                    .setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialogInterface, i) -> {
                        switch (i) {
                            case 0:
                                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    // Nếu chưa được cấp, yêu cầu quyền từ người dùng
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{Manifest.permission.CAMERA},
                                            CAMERA_PERMISSION_REQUEST_CODE);
                                } else {
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                        activityResultLauncherForCamera.launch(takePictureIntent);
                                    }
                                }

                                break;
                            case 1:
                                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                activityResultLauncherForGallery.launch(pickPhotoIntent);
                                break;
                        }
                    });
            builder.show();
        });

    }

    private void confirmRegister() {
        // IS VALID DATA
        if (Studentid.getText().toString().isEmpty()) {
            Studentid.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            Studentid.setBackgroundResource(R.drawable.rounded_edittext_normal);
        }

        if (Address.getText().toString().isEmpty()) {
            Address.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            Address.setBackgroundResource(R.drawable.rounded_edittext_normal);
        }

        if (phonenum.getText().toString().isEmpty()) {
            phonenum.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            phonenum.setBackgroundResource(R.drawable.rounded_edittext_normal);
        }

        if (Studentid.getText().toString().isEmpty() ||
                Address.getText().toString().isEmpty() ||
                phonenum.getText().toString().isEmpty()) {
            // return statement in Java
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream;
        byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        } else
            Toast.makeText(getApplicationContext(), "Vui lòng chọn ảnh!!", Toast.LENGTH_SHORT).show();

        String url = "https://spriteee.000webhostapp.com/updateBioprofile.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.trim().equals("success")) {
                Toast.makeText(BioScreen.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(BioScreen.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        },
                error -> {
                    Toast.makeText(BioScreen.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                    Log.d("AAA", "lỖI!\n" + error.toString());
                }
        ) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_khachhang", String.valueOf(id));
                params.put("studentid", Studentid.getText().toString());
                params.put("sodienthoai", phonenum.getText().toString());
                params.put("diachi", Address.getText().toString());
                params.put("photoUrl", base64Image);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    private void intview() {
        backBtn = findViewById(R.id.backBtn);
        Studentid = findViewById(R.id.Studentid);
        Address = findViewById(R.id.Address);
        phonenum = findViewById(R.id.phonenum);
        circleavatar = findViewById(R.id.circleavatar);
        finishbtn = findViewById(R.id.finishbtn);
    }
}