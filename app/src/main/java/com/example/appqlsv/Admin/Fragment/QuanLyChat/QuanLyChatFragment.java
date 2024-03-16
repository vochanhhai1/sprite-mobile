package com.example.appqlsv.Admin.Fragment.QuanLyChat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appqlsv.Models.Conversation;
import com.example.appqlsv.Models.Message;
import com.example.appqlsv.Models.RoomMember;
import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuanLyChatFragment extends Fragment {
    private RecyclerView messRcV;
    private ProgressBar progressBar;
    private ListConversationAdapter listConversationAdapter;
    private ArrayList<Conversation> conversationArrayList;
    private ArrayList<Users> usersArrayList;
    private ArrayList<Message> messageArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ql_chat, container, false);
        initview(view);
        conversationArrayList = new ArrayList<>();
        usersArrayList = new ArrayList<>();
        messageArrayList = new ArrayList<>();

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager1.setOrientation(GridLayoutManager.VERTICAL);
        messRcV.setLayoutManager(gridLayoutManager1);

        listConversationAdapter = new ListConversationAdapter(conversationArrayList,getActivity());
        messRcV.setAdapter(listConversationAdapter);
        GetListMess();
        eventbuton();
        return view;
    }

    private void eventbuton() {
        listConversationAdapter.onItemClick = conversation -> {
            Intent intent = new Intent(getActivity(), AdminChatScreen.class);
            intent.putExtra("conversationId", conversation.getId());
            startActivityForResult(intent, 1);
        };
    }

    private void GetListMess()
    {
        String url = "https://spriteee.000webhostapp.com/getAdminListmess.php";
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray jsonArray) {
                conversationArrayList.clear();
                messageArrayList.clear();
                usersArrayList.clear();
                for (int i = 0; i< jsonArray.length();i++)
                {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        JSONObject roomMemberObject = jsonObject.getJSONObject("RoomMember");
                        JSONObject customers = roomMemberObject.getJSONObject("customers");
                        String imageUrl = "https://spriteee.000webhostapp.com/" + customers.getString("photoUrl");
                        Users users = new Users(customers.getInt("id_khachhang"),
                                customers.getString("fullname"),
                                imageUrl);
                        usersArrayList.add(users);
                        RoomMember roomMember = new RoomMember(roomMemberObject.getInt("id"),
                                users,
                                roomMemberObject.getInt("roomid"),
                                roomMemberObject.getString("createAt"),
                                roomMemberObject.getString("updateAt"));

                        Conversation conversation = new Conversation(jsonObject.getInt("id"),
                                jsonObject.getString("createAt"),
                                jsonObject.getString("lastMessage"),
                                jsonObject.getInt("isSeen")==0,
                                jsonObject.getString("updateAt"),
                                roomMember);
                        conversationArrayList.add(conversation);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                progressBar.setVisibility(View.GONE);
               listConversationAdapter.notifyDataSetChanged();
            }
        }, volleyError -> {
            Toast.makeText(getActivity(), "JSON parsing error: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "JSON parsing error: " + volleyError.getMessage());
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void initview(View view) {
        messRcV = view.findViewById(R.id.messRcV);
        progressBar = view.findViewById(R.id.progressBar);
    }
}
