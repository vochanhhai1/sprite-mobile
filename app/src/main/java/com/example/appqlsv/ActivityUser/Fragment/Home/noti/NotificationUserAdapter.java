package com.example.appqlsv.ActivityUser.Fragment.Home.noti;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.ActivityUser.Fragment.Cart.OrderDetail;
import com.example.appqlsv.Models.Notification;
import com.example.appqlsv.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NotificationUserAdapter extends RecyclerView.Adapter<NotificationUserAdapter.viewholder> {
    private final Context context;
    private final ArrayList<Notification> notificationArrayList;

    public NotificationUserAdapter(Context context, ArrayList<Notification> notificationArrayList) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
    }

    @NonNull
    @Override
    public NotificationUserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notify_item,parent,false);
        return new viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotificationUserAdapter.viewholder holder, int position) {
        Notification notification = notificationArrayList.get(position);

        if (notification.getOrder() != null && notification.getOrder().getStatus() != null) {
            switch (notification.getOrder().getStatus()) {
                case "COMPLETED":
                    holder.imageNotify.setImageResource(R.drawable.ic_success);
                    holder.notifyTitle.setText(context.getString(R.string.YourOrder) +
                            notification.getOrder().getId() + " " +
                            context.getString(R.string.UCompletedNoti));
                    break;
                case "PROCESSED":
                    holder.imageNotify.setImageResource(R.drawable.ic_success);
                    holder.notifyTitle.setText(context.getString(R.string.YourOrder) +
                            notification.getOrder().getId() + " " +
                            context.getString(R.string.UProcessedNoti));
                    break;
                case "CANCELLED":
                    holder.imageNotify.setImageResource(R.drawable.ic_cancel);
                    holder.notifyTitle.setText(context.getString(R.string.YourOrder) +
                            notification.getOrder().getId() + " " + context.getString(R.string.UCancelledNoti));
                    break;
                case "PENDING":
                    holder.imageNotify.setImageResource(R.drawable.ic_pending);
                    holder.notifyTitle.setText(context.getString(R.string.YourOrder) +
                            notification.getOrder().getId() + " " + context.getString(R.string.UPendingNoti));
                    break;
                case "ARRIVED":
                    holder.imageNotify.setImageResource(R.drawable.ic_pending);
                    holder.notifyTitle.setText(context.getString(R.string.YourOrder) +
                            notification.getOrder().getId() + " " + context.getString(R.string.UArrivedorder));
                    break;
            }
        }

        if (notification.getCreatedAt() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date createdDate = sdf.parse(notification.getCreatedAt());
                long createdTimeMillis = createdDate.getTime();

                holder.notifyDate.setText(DateUtils.getRelativeTimeSpanString(
                        createdTimeMillis,
                        System.currentTimeMillis(),
                        0L,
                        DateUtils.FORMAT_ABBREV_ALL
                ));
            } catch (ParseException e) {
                e.printStackTrace();
                // Xử lý ngoại lệ nếu có lỗi khi chuyển đổi chuỗi thành đối tượng Date
                holder.notifyDate.setText("");
            }
        } else {
            holder.notifyDate.setText("");
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                openOrderDetail(notification);
            }
        });

        if (notification.getSeen()) {
            holder.isNew.setVisibility(View.GONE);
        } else {
            holder.isNew.setVisibility(View.VISIBLE);
        }

    }
    private void openOrderDetail(Notification notification) {
        Intent intent = new Intent(context, OrderDetail.class);
        if (notification.getOrder() != null && notification.getOrder().getId() != null) {
            intent.putExtra("orderid", notification.getOrder().getId());
            context.startActivity(intent);

            updateisSeen(notification.getOrder().getId());
        }
    }

    private void updateisSeen(int orderid)
    {
        String url = "https://spriteee.000webhostapp.com/updateNotif.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")) {
                    updateNotificationList(orderid);
                } else {
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    Log.d("VolleyError", "Error: " + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "JSON parsing error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("VolleyError", "Error: " + error);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderid", String.valueOf(orderid));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateNotificationList(int orderId) {
        for (Notification notification : notificationArrayList) {
            if (notification.getOrder() != null && notification.getOrder().getId() == orderId) {
                notification.setSeen(true);
                notifyDataSetChanged();
                return;
            }
        }
    }
    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        private final ImageView imageNotify;
        private final TextView notifyTitle;
        private final TextView notifyDate;
        private final CardView isNew,layout;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            isNew = itemView.findViewById(R.id.isNew);
            imageNotify = itemView.findViewById(R.id.imageNotify);
            notifyTitle = itemView.findViewById(R.id.notifyTitle);
            notifyDate = itemView.findViewById(R.id.notifyDate);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
