package com.example.appqlsv.Admin.Fragment.QuanLyChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Models.Message;
import com.example.appqlsv.R;
import com.example.appqlsv.util.UserSessionManager;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Message> messageArrayList;
    public static final int ITEM_RECEIVE = 1;
    public static final int ITEM_SEND = 2;
    public ConversationAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }
    public static class SendViewHolder extends RecyclerView.ViewHolder {
        public TextView sendMess;

        public SendViewHolder(View itemView) {
            super(itemView);
            sendMess = itemView.findViewById(R.id.sendItem);
        }
    }

    public static class ReceiveViewHolder extends RecyclerView.ViewHolder {
        public TextView receiveMess;

        public ReceiveViewHolder(View itemView) {
            super(itemView);
            receiveMess = itemView.findViewById(R.id.receiveItem);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == ITEM_RECEIVE) {
            view = inflater.inflate(R.layout.receive_message_item, parent, false);
            return new ReceiveViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.send_message_item, parent, false);
            return new SendViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        if (holder.getClass() == SendViewHolder.class) {
            // do stuff for send holder
            SendViewHolder viewHolder = (SendViewHolder) holder;
            viewHolder.sendMess.setText(message.getText());
        } else {
            // do stuff for receive holder
            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.receiveMess.setText(message.getText());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        UserSessionManager userSessionManager = new UserSessionManager(context);
        if (userSessionManager.getUserId()==message.getSender().getId_khachhang())
        {
            return ITEM_SEND;
        }else
            return ITEM_RECEIVE;
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        private TextView receiveItem,sendItem;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            receiveItem = itemView.findViewById(R.id.receiveItem);
            sendItem = itemView.findViewById(R.id.sendItem);
        }
    }
}
