package com.example.messenger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.ClassTool.ConvertImg;
import com.example.messenger.Model.Messages;
import com.example.messenger.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ME = 1;
    public static final int TYPE_YOU = 2;

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;

    Context context;
    List<Messages> list = new ArrayList<>();
    String idWithUser = "";

    public MessageAdaper (Context context){
        this.context = context;
    }

    public void setData(List<Messages> list, String idWithUser){
        this.list = list;
        this.idWithUser = idWithUser;

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSentBy().equals(idWithUser)){
            return TYPE_YOU;
        }else {
            return TYPE_ME;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_YOU){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_you, null);
            return new YouViewHolder(view);
        }else if (viewType == TYPE_ME){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, null);
            return new MeViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages = list.get(position);
        if (messages == null) return;

        if (holder.getItemViewType() == TYPE_YOU){
            YouViewHolder youHolder = (YouViewHolder) holder;

            if (messages.getTypeText() == TYPE_TEXT){
                youHolder.imgMessage.setVisibility(View.GONE);
                youHolder.tvMessage.setVisibility(View.VISIBLE);
                youHolder.tvMessage.setText(messages.getText());
            }else if (messages.getTypeText() == TYPE_IMAGE){
                if (messages.getText().equalsIgnoreCase("")) return;
                youHolder.tvMessage.setVisibility(View.GONE);
                youHolder.imgMessage.setVisibility(View.VISIBLE);
                youHolder.imgMessage.setImageBitmap(ConvertImg.convertBaseStringToBitmap(messages.getText()));
            }

            youHolder.imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moZoomAnh(messages.getText());
                }
            });
        }

        else if (holder.getItemViewType() == TYPE_ME){
            MeViewHolder meViewHolder = (MeViewHolder) holder;

            if (messages.getTypeText() == TYPE_TEXT){
                meViewHolder.imgMessage.setVisibility(View.GONE);
                meViewHolder.tvMessage.setVisibility(View.VISIBLE);
                meViewHolder.tvMessage.setText(messages.getText());
            }else if (messages.getTypeText() == TYPE_IMAGE){
                if (messages.getText().equalsIgnoreCase("")) return;
                meViewHolder.tvMessage.setVisibility(View.GONE);
                meViewHolder.imgMessage.setVisibility(View.VISIBLE);
                meViewHolder.imgMessage.setImageBitmap(ConvertImg.convertBaseStringToBitmap(messages.getText()));
            }

            meViewHolder.imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moZoomAnh(messages.getText());
                }
            });
        }
    }

    private void moZoomAnh(String strImg) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_zoom_image);
        dialog.show();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView imageView = dialog.findViewById(R.id.dialog_zoom_image);
        imageView.setImageBitmap(ConvertImg.convertBaseStringToBitmap(strImg));
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    private class YouViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAvt, imgMessage;
        TextView tvMessage;
        CardView cardView;

        public YouViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvt = itemView.findViewById(R.id.item_chat_you_img_avata);
            imgMessage = itemView.findViewById(R.id.item_chat_you_img_message);
            tvMessage = itemView.findViewById(R.id.item_chat_you_tv_message);
            cardView = itemView.findViewById(R.id.item_chat_you_cv_avata);
        }
    }

    private class MeViewHolder extends RecyclerView.ViewHolder{

        ImageView imgMessage;
        TextView tvMessage;


        public MeViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMessage = itemView.findViewById(R.id.item_chat_me_img_message);
            tvMessage = itemView.findViewById(R.id.item_chat_me_tv_message);
        }
    }
}
