package com.example.appqlsv.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.R;

import java.util.HashMap;
import java.util.Map;

public class SetNewPassScreen extends AppCompatActivity {

    private FrameLayout backBtn;
    private EditText newpass,repass;
    private AppCompatButton finishbtn;
    private String userName;
    private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_pass_screen);
        intview();
        userName = getIntent().getStringExtra("id");
        eventbuton();
    }

    private void intview() {
        backBtn = findViewById(R.id.backBtn);
        newpass = findViewById(R.id.newpass);
        repass = findViewById(R.id.repass);
        finishbtn = findViewById(R.id.finishbtn);
        bar= findViewById(R.id.bar);
    }

    private void eventbuton() {
        backBtn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        });
        finishbtn.setOnClickListener(view -> toSuccessScreen());
    }
    private void toSuccessScreen() {
        if (newpass.getText().toString().isEmpty()) {
            newpass.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            newpass.setBackgroundResource(R.drawable.rounded_edittext_normal);
        }

        if (repass.getText().toString().isEmpty()) {
            repass.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else {
            repass.setBackgroundResource(R.drawable.rounded_edittext_normal);
        }

        if (newpass.getText().toString().isEmpty() || repass.getText().toString().isEmpty()) {
            return;
        }

        if (!newpass.getText().toString().equals(repass.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.Paswordnotmatch), Toast.LENGTH_LONG).show();
            return;
        }

        callAPI();
    }

    private void callAPI() {
        String url = "https://spriteee.000webhostapp.com/updateForgotpass.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        bar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, s -> {
            if (s.trim().equals("success")) {
                Intent it = new Intent(getApplicationContext(), SuccessSetPassScreen.class);
                finishAffinity();
                startActivity(it);
                finish();
                bar.setVisibility(View.GONE);
            } else {
                Toast.makeText(SetNewPassScreen.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        }, volleyError -> {
            Toast.makeText(SetNewPassScreen.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            Log.d("AAA", "lỖI!\n" + volleyError.toString());
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("email",userName);
                params.put("password",repass.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
