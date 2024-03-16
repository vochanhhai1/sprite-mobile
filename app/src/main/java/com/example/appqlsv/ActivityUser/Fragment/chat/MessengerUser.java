package com.example.appqlsv.ActivityUser.Fragment.chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.MainActivity;
import com.example.appqlsv.Models.Message;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessengerUser extends Fragment {
    private FrameLayout imageButton;
    private ConstraintLayout constraintLayout3,constraintLayout2,bottomField;
    private CardView cardView2,cardView;
    private ImageView messImage;
    private TextView messName,messStatus,txtSending;
    private RecyclerView conversationRCV;
    private EditText inputText;
    private ImageButton sendBtn,callMess;
    private ProgressBar progressBar;
    private ConversationAdapter conversationAdapter;
    private ArrayList<Message> messageArrayList;
    private ArrayList<Users> usersArrayList;
    private int users;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_messenger, container, false);
        intview(view);
        messageArrayList= new ArrayList<>();
        usersArrayList= new ArrayList<>();
        UserSessionManager userSessionManager = new UserSessionManager(getActivity());
        users = userSessionManager.getUserId();
        messName.setText("Admin");
        messStatus.setText("Online");
        messImage.setImageURI(null);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.hideSmoothBottomBar();
        }

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager1.setOrientation(GridLayoutManager.VERTICAL);
        conversationRCV.setLayoutManager(gridLayoutManager1);

        conversationAdapter = new ConversationAdapter(getActivity(),messageArrayList);
        conversationRCV.setAdapter(conversationAdapter);
        GetMessUser();
        eventbutton();


        return view;
    }
    private void eventbutton() {
        callMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Message message : messageArrayList)
                {
                    if (message.getSender().getIs_admin()==0)
                    {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + message.getSender().getSodienthoai()));
                        startActivity(intent);
                    }
                }

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for your implementation
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
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsetMessUser();
            }
        });
    }

    private void GetMessUser()
    {
        String url = "https://spriteee.000webhostapp.com/getUserMessage.php?memberid="+users;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                conversationAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "JSON parsing error: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "JSON parsing error: " + volleyError.getMessage());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void InsetMessUser() {
        String url = "https://spriteee.000webhostapp.com/InsertUsermessage.php";
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String s) {
                if (s.trim().equals("success")) {
                    Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    if (messageArrayList != null && !messageArrayList.isEmpty()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Cập nhật giao diện và thêm tin nhắn mới vào RecyclerView
                                conversationAdapter.notifyDataSetChanged();
                                inputText.setText("");
                                GetMessUser();
                                // Đảm bảo vị trí cuộn là một vị trí hợp lệ
                                int targetPosition = messageArrayList.size() - 1;
                                if (targetPosition >= 0) {
                                    conversationAdapter.notifyItemInserted(targetPosition);
                                    // Cuộn RecyclerView xuống vị trí của tin nhắn mới
                                    conversationRCV.smoothScrollToPosition(targetPosition);
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "lỖI!\n" + volleyError.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("text", inputText.getText().toString().trim());
                params.put("senderid", String.valueOf(users));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        GetMessUser();
    }
    private void intview(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        sendBtn = view.findViewById(R.id.sendBtn);
        inputText = view.findViewById(R.id.inputText);
        conversationRCV = view.findViewById(R.id.conversationRCV);
        messName = view.findViewById(R.id.messName);
        messStatus = view.findViewById(R.id.messStatus);
        txtSending = view.findViewById(R.id.txtSending);
        messImage = view.findViewById(R.id.messImage);
        cardView2 = view.findViewById(R.id.cardView2);
        cardView = view.findViewById(R.id.cardView);
        constraintLayout3 = view.findViewById(R.id.constraintLayout3);
        constraintLayout2 = view.findViewById(R.id.constraintLayout2);
        bottomField = view.findViewById(R.id.bottomField);
        imageButton = view.findViewById(R.id.imageButton);
        callMess = view.findViewById(R.id.callMess);
    }
}
