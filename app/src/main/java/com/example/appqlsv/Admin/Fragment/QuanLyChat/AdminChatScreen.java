package com.example.appqlsv.Admin.Fragment.QuanLyChat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.Message;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminChatScreen extends AppCompatActivity {
    private FrameLayout imageButton;
    private ConstraintLayout constraintLayout3,constraintLayout2,bottomField,constraintLayout;
    private CardView cardView2,cardView;
    private ImageView messImage;
    private TextView messName,messStatus,txtSending;
    private RecyclerView conversationRCV;
    private EditText inputText;
    private ImageButton sendBtn,callAdmin;
    private ProgressBar progressBar;
    private ConversationAdapter conversationAdapter;
    private ArrayList<Message> messageArrayList;
    private ArrayList<Users> usersArrayList;
    private int user;
    private int conversationId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat_screen);
        initview();
        messageArrayList= new ArrayList<>();
        usersArrayList = new ArrayList<>();
        UserSessionManager userSessionManager= new UserSessionManager(this);
        user = userSessionManager.getUserId();
        conversationId = getIntent().getIntExtra("conversationId", 0);
        //Toast.makeText(getApplicationContext(), " " + conversationId, Toast.LENGTH_SHORT).show();
        messStatus.setText("Online");
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getApplicationContext(), 1);
        gridLayoutManager1.setOrientation(GridLayoutManager.VERTICAL);
        conversationRCV.setLayoutManager(gridLayoutManager1);

        conversationAdapter = new ConversationAdapter(getApplicationContext(),messageArrayList);
        conversationRCV.setAdapter(conversationAdapter);
        GetMessUser();
        eventbuton();

    }


    private void eventbuton() {
        callAdmin.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 1234567890));
            startActivity(intent);
        });
        imageButton.setOnClickListener(view -> finish());
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thiết cho việc triển khai này
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần thiết cho việc triển khai này
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.toString().isEmpty()) {
                    sendBtn.setVisibility(View.GONE);
                } else {
                    sendBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        sendBtn.setOnClickListener(view -> InsetMessUser());
    }

    private void GetMessUser()
    {
        String url = "https://spriteee.000webhostapp.com/getAdminMess.php?id="+conversationId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray jsonArray) {
                messageArrayList.clear();
                for (int i = 0; i< jsonArray.length();i++)
                {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject customers = jsonObject.getJSONObject("customer");
                        String imageUrl = "https://spriteee.000webhostapp.com/" + customers.getString("photoUrl");
                        Users users = new Users(customers.getInt("id_khachhang"),
                                customers.getString("fullname"),
                                customers.getString("email"),
                                customers.getInt("is_admin"),
                                imageUrl,
                                customers.getString("sodienthoai")
                        );
                        usersArrayList.add(users);
                        Message message = new Message(jsonObject.getInt("id"),
                                jsonObject.getString("text"),
                                jsonObject.getString("createAt"),
                                jsonObject.getInt("roomid"),
                                users);
                        messageArrayList.add(message);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                for (Message message : messageArrayList)
                {
                    if (message.getSender().getIs_admin()!=0)
                    {
                        messName.setText(message.getSender().getFullname());
                        Picasso.get().load(message.getSender().getPhoto()).into(messImage);
                    }
                }
                conversationAdapter.notifyDataSetChanged();
            }
        }, volleyError -> {
            Toast.makeText(getApplicationContext(), "JSON parsing error: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "JSON parsing error: " + volleyError.getMessage());
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void InsetMessUser()
    {
        String url = "https://spriteee.000webhostapp.com/InsertAdminMess.php";
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String s) {
                if (s.trim().equals("success")) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    conversationAdapter.notifyDataSetChanged();
                    runOnUiThread(() -> {
                        conversationAdapter.notifyDataSetChanged();
                        inputText.setText("");

                        GetMessUser();
                        conversationAdapter.notifyItemInserted(messageArrayList.size() - 1);

                        // Cuộn RecyclerView xuống vị trí của tin nhắn mới
                        conversationRCV.smoothScrollToPosition(messageArrayList.size() - 1);
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        }, volleyError -> {
            Toast.makeText(getApplicationContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            Log.d("AAA", "lỖI!\n" + volleyError.toString());
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("text",inputText.getText().toString().trim());
                params.put("senderid", String.valueOf(user));
                params.put("roomid", String.valueOf(conversationId));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            conversationRCV.smoothScrollToPosition(conversationAdapter.getItemCount() - 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetMessUser();
    }

    private void initview() {
        callAdmin= findViewById(R.id.callAdmin);
        progressBar = findViewById(R.id.progressBar);
        sendBtn = findViewById(R.id.sendBtn);
        inputText = findViewById(R.id.inputText);
        conversationRCV = findViewById(R.id.conversationRCV);
        messName = findViewById(R.id.messName);
        messStatus = findViewById(R.id.messStatus);
        txtSending = findViewById(R.id.txtSending);
        messImage = findViewById(R.id.messImage);
        cardView2 = findViewById(R.id.cardView2);
        cardView = findViewById(R.id.cardView);
        constraintLayout3 = findViewById(R.id.constraintLayout3);
        constraintLayout2 = findViewById(R.id.constraintLayout2);
        bottomField = findViewById(R.id.bottomField);
        constraintLayout = findViewById(R.id.constraintLayout);
        imageButton = findViewById(R.id.imageButton);
    }

//    public void callUserFunc() {
//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + partner.getPhoneNumber()));
//        startActivity(intent);
//    }

}