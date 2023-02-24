package com.example.messenger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.ClassTool.ConvertImg;
import com.example.messenger.Model.HistoryMess;
import com.example.messenger.Model.User;
import com.example.messenger.Model.UserFont;
import com.example.messenger.R;
import com.example.messenger.RoomChatActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessHistoriAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ssdd/MM/YYYY");
    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY");
    SimpleDateFormat formatDay = new SimpleDateFormat("EEE");  // thu trong tuan
    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BODY = 1;
    private static final int TYPE_LOAD_MORE = 2;

    Context context;
    List<UserFont> listHeader;
    List<HistoryMess> listBody;
    private boolean isLoading;

    public MessHistoriAdapter(Context context) {
        this.context = context;

    }

    public void setData(List<UserFont> listHeader, List<HistoryMess> listBody) {
        this.listHeader = listHeader;
        this.listBody = listBody;
    }

    @Override
    public int getItemViewType(int position) {
        if (listHeader != null && position == 0) {
            return TYPE_HEADER;
        } else if (listBody != null && isLoading && position == listBody.size() - 1) {
            return TYPE_LOAD_MORE;
        }
        return TYPE_BODY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_main_user_header, null);
                return new HeaderViewHoler(view);
            }
            case TYPE_BODY: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_main_user_body, null);
                return new BodyViewHoler(view);
            }
            case TYPE_LOAD_MORE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, null);
                return new LoadMoreViewHoler(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // layout header
        if (holder.getItemViewType() == TYPE_HEADER) {
            HeaderViewHoler mHoler = (HeaderViewHoler) holder;
            MessHistoriHeaderAdapter adapter = new MessHistoriHeaderAdapter();
            adapter.setData(context,listHeader);

            mHoler.rcvHeader.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            mHoler.rcvHeader.setAdapter(adapter);


         // layout body tin nhan
        } else if (holder.getItemViewType() == TYPE_BODY) {
            BodyViewHoler mHoler = (BodyViewHoler) holder;

            HistoryMess mess = listBody.get(position - 1);
            getUserFont(mHoler,mess);



            // chuyển timeLastSent từ số unix về dạng Date
            String timeLastSent = "";
            Long unixTime = Long.parseLong(mess.getTimeLastSent());         // số unixTime giúp firebase sắp xếp thời gian
            Date date = new Date(unixTime * 1000);                          // convert lại số unixTime về dạng Date

            // check ngày hôm nay
            boolean isDay = formatDate.format(Calendar.getInstance().getTime()).equalsIgnoreCase(formatDate.format(date));
            if (isDay){
                timeLastSent = formatTime.format(date.getTime());
            }else   {
                timeLastSent = formatDay.format(date.getTime());
            }

            // check ai gửi
            boolean isYouSent = mess.getMessWithUser().equalsIgnoreCase(mess.getIdUserLastSent());
            if (isYouSent)
                mHoler.tvMess.setText(mess.getMessLastSent() + "  " + timeLastSent);
            else mHoler.tvMess.setText("Ban: " + mess.getMessLastSent() + "  " + timeLastSent);

            // check đã xem chưa
            if (mess.getSeen()){
                mHoler.cvSeen.setVisibility(View.GONE);
                mHoler.tvMess.setTextColor(Color.GRAY);
                mHoler.tvMess.setTypeface(null, Typeface.BOLD);
            } else {
                mHoler.cvSeen.setVisibility(View.VISIBLE);
                mHoler.tvMess.setTextColor(Color.BLACK);
                mHoler.tvMess.setTypeface(mHoler.tvMess.getTypeface(), Typeface.BOLD);
            }

            mHoler.rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RoomChatActivity.class);
                    intent.putExtra("idWithUser", mess.getMessWithUser());
                    intent.putExtra("idRoomMess", mess.getIdMess());

                    context.startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {

        if (listHeader != null && listBody != null) {
            return listBody.size() + 1;
        } else if (listHeader == null && listBody != null) {
            return listBody.size();
        } else if (listHeader != null && listBody == null) {
            return 1;
        }
        return 0;
    }

    public class HeaderViewHoler extends RecyclerView.ViewHolder {

        RecyclerView rcvHeader;

        public HeaderViewHoler(@NonNull View itemView) {
            super(itemView);
            rcvHeader = itemView.findViewById(R.id.item_main_user_header_main);
        }
    }

    public class BodyViewHoler extends RecyclerView.ViewHolder {
        TextView tvName, tvMess;
        ImageView imgAvt;
        RelativeLayout rlMain;
        CardView cvSeen;

        public BodyViewHoler(@NonNull View itemView) {
            super(itemView);

            rlMain = itemView.findViewById(R.id.item_main_user_body_main);
            cvSeen = itemView.findViewById(R.id.item_main_user_body_cv_seen);
            tvName = itemView.findViewById(R.id.item_main_user_body_tv_name);
            tvMess = itemView.findViewById(R.id.item_main_user_body_tv_message);
            imgAvt = itemView.findViewById(R.id.item_main_user_body_img_avt);

        }
    }


    public class LoadMoreViewHoler extends RecyclerView.ViewHolder {

        public LoadMoreViewHoler(@NonNull View itemView) {
            super(itemView);
        }
    }


    private void getUserFont(BodyViewHoler mHoler, HistoryMess mess) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("UserFont").child(mess.getMessWithUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserFont userFont = snapshot.getValue(UserFont.class);

                if (userFont == null) return;

                if (!userFont.getImg().equalsIgnoreCase("")) mHoler.imgAvt.setImageBitmap(ConvertImg.convertBaseStringToBitmap(userFont.getImg()));
                mHoler.tvName.setText(userFont.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onRelease(){
        this.context = null;
    }

    public void addFooterLoading(){
        isLoading = true;
        listBody.add(new HistoryMess());
    }

    public void removeFooterLoading(){
        isLoading = false;
        int position = listBody.size() -1;
        if (listBody != null){
            listBody.remove(position);
            notifyItemRemoved(position);
        }
    }
}
