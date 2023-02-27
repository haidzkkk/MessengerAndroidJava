package com.example.messenger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.ClassTool.ConvertImg;
import com.example.messenger.Model.HistoryMess;
import com.example.messenger.Model.User;
import com.example.messenger.Model.UserFont;
import com.example.messenger.R;
import com.example.messenger.RoomChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class MessHistoriHeaderAdapter extends RecyclerView.Adapter<MessHistoriHeaderAdapter.ViewHolder> {

    List<UserFont> list;
    private DatabaseReference reference;
    String myUserName;

    Context context;

    public MessHistoriHeaderAdapter() {

    }

    public void setData(Context context, List<UserFont> list) {
        this.context = context;
        this.list = list;
        reference = FirebaseDatabase.getInstance().getReference();
        myUserName = context.getSharedPreferences("dataUser", Context.MODE_PRIVATE).getString("userName", "");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_main_user_item_header, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserFont user = list.get(position);

        if (user == null) return;

        holder.imgAvt.setImageBitmap(ConvertImg.convertBaseStringToBitmap(user.getImg()));
        holder.tvName.setText(user.getName());

        holder.lnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to message
                if (holder.getAdapterPosition() != 0)   // nếu là 0 thì đó là chính mình thì k thể nt đc
                {
                    checkMessWithYou(user.getUserName());
                }else {
                    //
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgAvt;
        LinearLayout lnMain;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lnMain = itemView.findViewById(R.id.item_main_user_header_main);
            imgAvt = itemView.findViewById(R.id.item_main_user_header_img_avt);
            tvName = itemView.findViewById(R.id.item_main_user_header_tv_name);
        }
    }

    private void checkMessWithYou(String idWithUser) {
        reference.child("User").child(myUserName)
                .child("historiMes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isChat = false;
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            HistoryMess mess = ds.getValue(HistoryMess.class);
                            if (mess != null && mess.getMessWithUser().equals(idWithUser)) {         // check có mess với member rỗng k để kiểm tra
                                startToRoomChat(mess.getMessWithUser(), mess.getIdMess());
                                isChat = false;
                            }
                        }

                        if (!isChat){       // nếu không có đoạn chat nào thì tạo historiMes mới
                            createRoomChatNew(idWithUser);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void createRoomChatNew(String idWithUser) {
        String idRoomMes = getRandomID(15);
        startToRoomChat(idWithUser, idRoomMes);
    }


    private void startToRoomChat(String idWithUser, String idRoomMess){
        Intent intent = new Intent(context, RoomChatActivity.class);
        intent.putExtra("idWithUser", idWithUser);
        intent.putExtra("idRoomMess", idRoomMess);

        context.startActivity(intent);
    }

    private String getRandomID(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Random random = new Random();
        StringBuilder id = new StringBuilder(length);

        for (int i = 0; i < length; i++){
            id.append(AB.charAt(random.nextInt(AB.length())));
        }

        return id.toString();
    }
}
