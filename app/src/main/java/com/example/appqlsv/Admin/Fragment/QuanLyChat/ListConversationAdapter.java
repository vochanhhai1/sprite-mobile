package com.example.appqlsv.Admin.Fragment.QuanLyChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Models.Conversation;
import com.example.appqlsv.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListConversationAdapter extends RecyclerView.Adapter<ListConversationAdapter.ViewHolder> {
    private ArrayList<Conversation> conversationArrayList;
    private Context context;
    public interface ConversationClickListener {
        void onItemClick(Conversation conversation);
    }
    public ConversationClickListener onItemClick;
    public ListConversationAdapter(ArrayList<Conversation> conversationArrayList, Context context) {
        this.conversationArrayList = conversationArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListConversationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_message_item,parent,false);
        return new ListConversationAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListConversationAdapter.ViewHolder holder, int position) {
        Conversation conversation = conversationArrayList.get(position);
        Picasso.get().load(conversation.getRoomMembers().getUsers().getPhoto()).into(holder.messImage);
        holder.messName.setText(conversation.getRoomMembers().getUsers().getFullname());
        holder.messLast.setText(conversation.getLastMessage());
        if (conversation.getCreatedAt() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date createdDate = sdf.parse(conversation.getCreatedAt());
                long createdTimeMillis = createdDate.getTime();

                holder.messTime.setText(DateUtils.getRelativeTimeSpanString(
                        createdTimeMillis,
                        System.currentTimeMillis(),
                        0L,
                        DateUtils.FORMAT_ABBREV_ALL
                ));
            } catch (ParseException e) {
                e.printStackTrace();
                holder.messTime.setText("");
            }
        } else {
            holder.messTime.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(conversation);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView messImage;
        private TextView messName,messLast,messTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            messImage = itemView.findViewById(R.id.messImage);
            messName = itemView.findViewById(R.id.messName);
            messLast = itemView.findViewById(R.id.messLast);
            messTime = itemView.findViewById(R.id.messTime);
        }
    }
}
