package com.example.appqlsv.ActivityUser.Fragment.Home.noti;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.Notification;
import com.example.appqlsv.Models.Order;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationUser extends AppCompatActivity {
    private RecyclerView notifyRCV;
    private ProgressBar ProgressBar;
    private FrameLayout backBtn;
    private ArrayList<Notification> notificationArrayList;
    private NotificationUserAdapter adapter;
    private UserSessionManager userSessionManager;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_user);

        anhxaid();
        notificationArrayList = new ArrayList<>();
        userSessionManager = new UserSessionManager(this);
        userId = userSessionManager.getUserId();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        notifyRCV.setLayoutManager(gridLayoutManager);

        adapter = new NotificationUserAdapter(this, notificationArrayList);
        notifyRCV.setAdapter(adapter);

        GetNoti(userId);
        eventbutton();
    }


    public void GetNoti(int idu)
    {
        String url = "https://spriteee.000webhostapp.com/GetNotification.php?buyer="+idu;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                notificationArrayList.clear();
            for (int i= 0 ; i < response.length(); i++)
            {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    JSONObject orderObject = jsonObject.getJSONObject("order");
                    Order order = new Order(orderObject.getInt("idorder"),
                            orderObject.getInt("totalPrice"),
                            orderObject.getString("notes"),
                            orderObject.getInt("isReviewed")==1,
                            orderObject.getString("status"),
                            orderObject.getString("createAt"),
                            orderObject.getString("updateAt"),
                            orderObject.getInt("buyer"));

                    String isSeenString = jsonObject.getString("isSeen");
                    boolean isSeen = isSeenString.equals("1");
                    Notification notification = new Notification(jsonObject.getInt("id"),
                            isSeen,
                            jsonObject.getString("createAt"),
                            jsonObject.getString("updateAt"),
                            order);

                    notificationArrayList.add(notification);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void eventbutton() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void anhxaid() {
        notifyRCV= findViewById(R.id.notifyRCV);
        ProgressBar= findViewById(R.id.bar);
        backBtn= findViewById(R.id.backBtn);
    }
}