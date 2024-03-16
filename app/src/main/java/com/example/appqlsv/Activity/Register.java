package com.example.appqlsv.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");
    private TextView register_login;
    private EditText register_user, register_email, register_password, register_confirm;
    private Button register_button;
    private CheckBox RoleCheckBox;
    private int role;
    private ArrayList<Users> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        users= new ArrayList<>();
        anhxa();
        GetData();
        EventButton();
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));

    }

    private void xulydangky() {
        if (RoleCheckBox.isChecked()) {
            role = 1;
        } else {
            role = 2;
        }

        if (!validateEmail() || !validatePassword() || !validateFullname()) {
            return;
        }

        String inputemail = register_email.getText().toString().trim();
        String inputpassword = register_password.getText().toString().trim();
        String inputfullname = register_user.getText().toString().trim();
        String repassword = register_confirm.getText().toString().trim();

        if (inputemail.isEmpty() || inputpassword.isEmpty() || inputfullname.isEmpty() || repassword.isEmpty()) {
            Toast.makeText(Register.this, "Vui lòng nhập đủ thông tin!!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Users user : users) {
            if (inputpassword.equals(repassword)) {
                if (user.getEmail().trim().equals(inputemail)) {
                    Toast.makeText(Register.this, "Email đã được sử dụng!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(Register.this, "Mật khẩu không trùng khớp!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (role == 1) {
            InsertUsers();
            Toast.makeText(Register.this, "Đăng ký thành công với quyền Nhân viên \n Vui lòng chờ trong 1p có Admin duyệt!!", Toast.LENGTH_SHORT).show();
        } else {
            InsertUsers();
            Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        }
    }


    private void GetData() {
        String url = "https://spriteee.000webhostapp.com//demo.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    users.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            Users user = new Users(object.getInt("id_khachhang"),
                                    object.getString("fullname"),
                                    object.getString("email"),
                                    object.getString("password"),
                                    object.getString("sodienthoai"),
                                    object.getString("diachi"),
                                    object.getInt("is_admin"),
                                    object.getInt("status")==1);
                            users.add(user);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, error -> Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }
    private boolean validateFullname() {
        String fullnameInput = register_user.getText().toString().trim();

        if (fullnameInput.isEmpty()) {
            register_user.setError(getString(R.string.tr_ng_kh_ng_th_tr_ng));
            return false;
        } else if (fullnameInput.length() > 15) {
            register_user.setError(getString(R.string.t_n_ng_i_d_ng_qu_d_i));
            return false;
        }else if (fullnameInput.matches(".*\\d+.*")) {
            register_user.setError(getString(R.string.t_n_ng_i_d_ng_kh_ng_th_ch_a_s));
            return false;
        } else {
            register_user.setError(null);
            return true;
        }
    }

    public  boolean validateEmail()
    {
        String validateemail = register_email.getText().toString().trim();

        if (validateemail.isEmpty())
        {
            register_email.setError(getString(R.string.tr_ng_kh_ng_th_tr_ng));
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(validateemail).matches()) {
            register_email.setError(getString(R.string.h_y_nh_p_ng_email_c_a_b_n));
            return false;
        } else
        {
            register_email.setError(null);
            return true;
        }

    }

    private boolean validatePassword() {
        String passwordInput = register_password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            register_password.setError(getString(R.string.tr_ng_kh_ng_th_tr_ng));
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            register_password.setError(getString(R.string.m_t_kh_u_qu_y_u));
            return false;
        } else {
            register_password.setError(null);
            return true;
        }
    }
    private void EventButton() {
        register_login.setOnClickListener(view -> startActivity(new Intent(Register.this, Login.class)));

        register_button.setOnClickListener(view -> xulydangky());
    }

    private void InsertUsers() {
        String urlInsert = "https://spriteee.000webhostapp.com/register.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInsert, response -> {
            if (response.trim().equals("success")) {
                startActivity(new Intent(Register.this, Login.class));
            } else {
                Toast.makeText(Register.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(Register.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            Log.d("AAA", "lỖI!\n" + error.toString());
        }
        ) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String username = register_email.getText().toString().trim();
                String password = register_password.getText().toString().trim();
                String full_name = register_user.getText().toString().trim();
                if (RoleCheckBox.isChecked()) {
                    role = 1;
                } else {
                    role = 2;
                }
                params.put("email", username);
                params.put("password", password);
                params.put("fullname", full_name);
                params.put("is_admin", String.valueOf(role));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void anhxa() {
        register_login = findViewById(R.id.register_login);
        register_user = findViewById(R.id.register_user);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_confirm = findViewById(R.id.register_confirm);
        register_button = findViewById(R.id.register_button);
        RoleCheckBox = findViewById(R.id.RoleCheckBox);
    }
}