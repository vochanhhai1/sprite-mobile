package com.example.appqlsv.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Admin.HomeAdmin;
import com.example.appqlsv.ActivityUser.Fragment.MainActivity;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //it nhat 1 so
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +         //it nhat 1 ky tu viet hoa
                    "(?=.*[a-zA-Z])" +
                    "(?=.*[@#$%^&+=])" +    //it nhat 1 ky tu dac biet
                    "(?=\\S+$)" +           //khong khoang trang
                    ".{6,}" +               //toi thieu 6 ky thu
                    "$");
    private EditText login_user, login_pass;
    private TextView text_register,forgotpass;
    private ArrayList<Users> users;
    private CheckBox rememberMeCheckBox;
    private UserSessionManager userSessionManager;
    private boolean isBackPressedOnce = false;

    private AppCompatButton loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anhxaid();
        users = new ArrayList<>();
        GetData();
        Rememberpass();
        EventButton();

        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
    }



    public  boolean validateEmail()
    {
        String validateemail = login_user.getText().toString().trim();

        if (validateemail.isEmpty())
        {
            login_user.setError(getString(R.string.tr_ng_kh_ng_th_tr_ng));
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(validateemail).matches()) {
            login_user.setError(getString(R.string.h_y_nh_p_ng_email_c_a_b_n));
            return false;
        } else
        {
            login_user.setError(null);
            return true;
        }

    }
    private boolean validatePassword() {
        String passwordInput = login_pass.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            login_pass.setError(getString(R.string.tr_ng_kh_ng_th_tr_ng));
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            login_pass.setError(getString(R.string.m_t_kh_u_qu_y_u));
            return false;
        } else {
            login_pass.setError(null);
            return true;
        }
    }
    private void Rememberpass() {
        userSessionManager = new UserSessionManager(this);

        if (userSessionManager.getRememberMe()) {
            String savedUsername = userSessionManager.getUsername();
            String savedPassword = userSessionManager.getPassword();
            login_user.setText(savedUsername);
            login_pass.setText(savedPassword);
            rememberMeCheckBox.setChecked(true);
        }

        rememberMeCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // Lưu thông tin đăng nhập vào SharedPreferences
                String username = login_user.getText().toString();
                String password = login_pass.getText().toString();
                userSessionManager.saveCredentials(username, password);
                userSessionManager.saveRememberMe(true);
            } else {
                // Xóa thông tin đăng nhập từ SharedPreferences
                userSessionManager.clearCredentials();
                userSessionManager.saveRememberMe(false);
            }
        });
    }

    private void EventButton() {
        loginBtn.setOnClickListener(view -> {
            if (!validateEmail()| !validatePassword()) {
                return;
            }
            String inputemail =  login_user.getText().toString();
            String inputpassword = login_pass.getText().toString();
            if (inputemail.isEmpty() | inputpassword.isEmpty()) {
                Toast.makeText(Login.this, "Vui lòng nhập đủ thông tin!!", Toast.LENGTH_SHORT).show();
            } else {
                XuLyLogin(inputemail,inputpassword);
            }
        });

        text_register.setOnClickListener(view -> {

            Intent intent = new Intent(Login.this, Register.class);
            intent.putExtra("userList", users);
            startActivity(intent);

        });
        forgotpass.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),ForgotPassScreen.class)));
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
// 0 là admin
// 1 là nhân viên
// 2 là khách hàng


    private void XuLyLogin(String username, String password) {
        for (Users user : users) {
            if (user.getEmail().equals(username) && user.getPassword().equals(password)) {
                int userId = user.getId_khachhang();
                userSessionManager.saveUserId(userId);
                userSessionManager.savePhoneNumber(user.getSodienthoai());
                if (user.getIs_admin() == 0 && user.getStatus()) {
                    Toast.makeText(getApplicationContext(), "Admin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, HomeAdmin.class);
                    intent.putExtra("Users",user);
                    startActivity(intent);
                } else if (user.getIs_admin() == 1) {
                    if (!user.getStatus())
                    {
                        Toast.makeText(getApplicationContext(), "Tài khoản nhân viên chưa được kích hoạt!!", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Đăng nhập với quyền NHÂN VIÊN!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this,MainActivity.class));
                    }
                }
                else if (user.getIs_admin() == 2)
                {
                    userSessionManager.saveFullname(user.getFullname());
                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,MainActivity.class));
                }
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show();
    }


    private void anhxaid() {
        login_user = findViewById(R.id.login_user);
        login_pass = findViewById(R.id.login_pass);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        text_register = findViewById(R.id.text_register);
        loginBtn = findViewById(R.id.loginBtn);
        forgotpass = findViewById(R.id.forgotpass);
    }

    public void onBackPressed() {
        if (isBackPressedOnce) {
            super.onBackPressed();
            return;
        }
        isBackPressedOnce = true;
        Toast.makeText(this, "Nhấn back một lần nữa để thoát", Toast.LENGTH_SHORT).show();
        new android.os.Handler().postDelayed(
                () -> isBackPressedOnce = false,
                2000 // Thời gian chờ (milliseconds)
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetData();
    }
}