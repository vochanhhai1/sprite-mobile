package com.example.appqlsv.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.appqlsv.R;

public class SuccessSetPassScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_set_pass_screen);
        AppCompatButton loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(view -> toLoginScreen());
    }
    private void toLoginScreen() {
        Intent it = new Intent(this, Login.class);
        finishAffinity();
        startActivity(it);
        finish();
    }

}