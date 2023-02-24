package com.example.messenger.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.Model.ItemNav;
import com.example.messenger.R;

import java.util.ArrayList;
import java.util.List;

public class ListNavAdapter extends RecyclerView.Adapter<ListNavAdapter.ViewHoler>{

    List<ItemNav> list;

    public ListNavAdapter (){
        list = new ArrayList<>();
        list.add(new ItemNav(R.drawable.baseline_chat_24, "Đoạn chat", 0));
        list.add(new ItemNav(R.drawable.baseline_chat_bubble_24, "Tin nhắn đang chờ", 0));
    }

    public void setData(int count){
        list.get(0).setIndex(count);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nav_rcv_chat, null);

        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        int img = list.get(position).getLogo();
        holder.imgLogo.setImageResource(img);
        holder.tvTitle.setText(list.get(position).getTitle());

        int index = list.get(position).getIndex();
        if (index > 0){
            holder.cvIndex.setVisibility(View.VISIBLE);
            holder.tvIndex.setText(String.valueOf(index));
        }else {
            holder.cvIndex.setVisibility(View.GONE);
        }

//        holder.cvMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.cvMain.setCardBackgroundColor(Color.GRAY);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHoler extends RecyclerView.ViewHolder {

        TextView tvTitle, tvIndex;
        ImageView imgLogo;

        CardView cvMain, cvIndex;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            cvMain = itemView.findViewById(R.id.item_nav_rcv_chat_cv_main);
            cvIndex = itemView.findViewById(R.id.item_nav_rcv_chat_cv_index);
            tvTitle = itemView.findViewById(R.id.item_nav_rcv_chat_tv_title);
            tvIndex = itemView.findViewById(R.id.item_nav_rcv_chat_tv_index);
            imgLogo = itemView.findViewById(R.id.item_nav_rcv_chat_img_icon);
        }
    }
}
