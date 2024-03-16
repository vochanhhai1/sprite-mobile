package com.example.appqlsv.Admin.Fragment.QuanLyNhanVien;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqlsv.Models.Users;
import com.example.appqlsv.R;

import java.util.ArrayList;

public class AdapterQLNV extends RecyclerView.Adapter<AdapterQLNV.viewholder> {
    private final Context context;
    private final ArrayList<Users> usersArrayList;


    public AdapterQLNV(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public AdapterQLNV.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_qlnd,parent,false);
        return new viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterQLNV.viewholder holder, int position) {
        Users users = usersArrayList.get(position);
        holder.recTen.setText(users.getFullname());
        holder.recEmail.setText(users.getEmail());

        if (users.getIs_admin()==1)
        {
            holder.recTinhtrang.setText("Nhân viên");
        }else if (users.getIs_admin()==2)
        {
            holder.recTinhtrang.setText("Khách hàng");
        }

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final CardView recCard;
        private final TextView recTen;
        private final TextView recEmail;
        private final TextView recTinhtrang;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            recCard = itemView.findViewById(R.id.recCard);
            recTen = itemView.findViewById(R.id.recTen);
            recEmail = itemView.findViewById(R.id.recEmail);
            recTinhtrang = itemView.findViewById(R.id.recTinhtrang);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, 1, Menu.NONE, "Update");
            contextMenu.add(Menu.NONE, 2, Menu.NONE, "Delete");
        }
    }
}
