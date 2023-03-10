package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.Adapter.MessageAdaper;
import com.example.messenger.ClassTool.ConvertImg;
import com.example.messenger.ClassTool.MyApplication;
import com.example.messenger.DataMessagingNotification.FcmNotificationsSender;
import com.example.messenger.Fragment.BottomSheetInfomationUserFagement;
import com.example.messenger.Model.HistoryMess;
import com.example.messenger.Model.Messages;
import com.example.messenger.Model.User;
import com.example.messenger.Model.UserFont;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RoomChatActivity extends AppCompatActivity {

    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ssdd/MM/YYYY");
    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/YYYY");
    SimpleDateFormat formatDay = new SimpleDateFormat("EEE");  // thu trong tuan
    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    ImageView imgBack, imgAvt, imgCall, imgVideo, imgCamera, imgPhoto, imgSend;
    TextView tvName;
    EditText edtMessage;
    RecyclerView rcv;

    DatabaseReference reference;

    List<Messages> list;
    List<String> listMember;
    MessageAdaper adaper;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout rlYou;

    private String myUserName;
    private String idWithUser;
    private String idRoomMess;

    private String strBitMapSend = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        initUI();
        setUpdata();

        rlYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailUser();
            }
        });

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRequestPermistion();
            }
        });

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moThuVien();
            }
        });

//        FirebaseMessaging.getInstance().subscribeToTopic("all");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String  token = task.getResult();
                            Log.e("tokenFire", token);
                        }

                    }
                });


    }


    private void setUpdata() {
        Intent intent = getIntent();

        idWithUser = intent.getStringExtra("idWithUser");
        idRoomMess = intent.getStringExtra("idRoomMess");

        myUserName = getSharedPreferences("dataUser", MODE_PRIVATE).getString("userName", "");

        adaper.setData(list, idWithUser);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(adaper);

        getUserWithxMes(idWithUser);
        getMessFromDataBase(idRoomMess);
        updateDataToMember(true, null);

        Log.e("messwtihUser", idWithUser + " | " + "Myuset: " + myUserName);
    }


    private void initUI() {
        imgBack = findViewById(R.id.room_chat_img_back);
        imgAvt = findViewById(R.id.room_chat_img_avt);
        imgCall = findViewById(R.id.room_chat_img_call);
        imgVideo = findViewById(R.id.room_chat_img_videocam);
        imgCamera = findViewById(R.id.room_chat_img_camera);
        imgPhoto = findViewById(R.id.room_chat_img_photo);
        imgSend = findViewById(R.id.room_chat_img_send);
        tvName = findViewById(R.id.room_chat_tv_name);
        rlYou = findViewById(R.id.room_chat_rl_you);
        edtMessage = findViewById(R.id.room_chat_edt_message);
        rcv = findViewById(R.id.room_chat_rcv);

        list = new ArrayList<>();
        listMember = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();

        adaper = new MessageAdaper(this);
        linearLayoutManager = new LinearLayoutManager(this);


    }

    private void detailUser() {

        reference.child("User").child(idWithUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null){
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putBoolean("myAccount", false);

                BottomSheetInfomationUserFagement bottomSheet = new BottomSheetInfomationUserFagement();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getUserWithxMes(String nameUser) {
        reference.child("UserFont").child(nameUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserFont userFont = snapshot.getValue(UserFont.class);

                        if (userFont == null) return;

                        if (!userFont.getImg().equalsIgnoreCase(""))
                            imgAvt.setImageBitmap(ConvertImg.convertBaseStringToBitmap(userFont.getImg()));
                        tvName.setText(userFont.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void getMessFromDataBase(String idMess) {
        reference.child("Messages").child(idMess)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Messages messages = snapshot.getValue(Messages.class);

                        if (messages != null) {
                            list.add(messages);
                            adaper.notifyDataSetChanged();
                            rcv.scrollToPosition(list.size() - 1);
                        }
                        Log.e("asdsad", list.toString());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // ph????ng th???c b???t l???y action c???a ng?????i d??ng l?? cho ch??p hay t??? ch???i permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 998) {   // n???u requestCode b???ng v???i code xin quy???n kia th?? ???? cho th?? check action
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                moCamera();
            } else {
                Toast.makeText(RoomChatActivity.this, "Vui l??ng cho ph??p truy c???p th?? vi???n", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkRequestPermistion() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            moCamera();
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            moCamera();
        } else {
            String[] permission = {Manifest.permission.CAMERA};
            requestPermissions(permission, 998);
        }
    }

    private void moThuVien() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 888);
    }

    private void moCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 999) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                strBitMapSend = ConvertImg.convertBitmapToBaseString(bitmap);
                edtMessage.setHint("Da co anh");
            } else if (requestCode == 888) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    strBitMapSend = ConvertImg.convertBitmapToBaseString(bitmap);
                    edtMessage.setHint("Da co anh");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    private void sendData() {

        int id = 0;
        if (list.size() > 0) {
            id = list.get(list.size() - 1).getId() + 1;
        }

        String sentBy = myUserName;
        String text = "";
        int typeText = 0;
        String time = format.format(Calendar.getInstance().getTime());

        if (!strBitMapSend.equalsIgnoreCase("")) {
            text = strBitMapSend;
            typeText = 2;
        } else {
            text = edtMessage.getText().toString().trim();
            typeText = 1;
        }
        Messages messagesAdd = null;
        if (!text.equalsIgnoreCase("")) {
            messagesAdd = new Messages(id, sentBy, text, typeText, time);
            reference.child("Messages").child(idRoomMess).child(messagesAdd.getId() + "")
                    .setValue(messagesAdd);

            strBitMapSend = "";
            edtMessage.setText("");
            edtMessage.setHint("Aa");

            updateDataToMember(false, messagesAdd);
        }
    }

    private void updateDataToMember(boolean isSeen, Messages messages) {


        if (listMember.size() < 2) {
            reference.child("User").child(myUserName).child("historiMes").child(idRoomMess).child("member")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String member = ds.getValue(String.class);
                                if (member != null) {
                                    listMember.add(member);
                                }
                                Log.e("memenrer", listMember.toString());
                            }

                            if (listMember.size() == 0) {        // n???u m?? t??m member trong historiMes m?? k c??, th?? ch??? c?? historiMes m???i n??n add chay th??? ???? c?? :v
                                listMember.add(myUserName);
                                listMember.add(idWithUser);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        if (isSeen) {

        } else {
            if (messages == null) return;

            for (int i = 0; i < listMember.size(); i++) {
                String idMess = idRoomMess;
                String idUserLastSent = messages.getSentBy();
                String messLastSent = "";
                if (messages.getTypeText() == 1) messLastSent = messages.getText();
                else messLastSent = "???? gi??? ???nh";

                String messWithUser = "";
                boolean seen;
                if (!listMember.get(i).equals(myUserName)) {
                    messWithUser = myUserName;
                    seen = false;
                } else {
                    seen = true;
                    messWithUser = idWithUser;
                }

                Date date = new Date();
                long unixTime1 = date.getTime() / 1000;     // chuy???n th??nh unixTime ????? s???p x??p cho d???
                String timeLastSent = String.valueOf(unixTime1);


                HistoryMess sentMessData = new HistoryMess(idMess, idUserLastSent, messLastSent, messWithUser, seen, timeLastSent, listMember);

                reference.child("User").child(listMember.get(i)).child("historiMes").child(idRoomMess)
                        .setValue(sentMessData);
            }

            sendNotification(listMember.get(0), list.get(0).getText());
        }
    }

    private void sendNotification(String name, String text) {

//     Notification ngh???ch
//        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANEL_ID)
//                .setContentTitle(name)
//                .setContentText(text)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .build();
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        managerCompat.notify(1, notification);



//      send data to firebase
//        String token = "e43bJWQrQtele8dYs_Tznh:APA91bEURbsp_2HTEJyJTaHuirdEg8EQz1-k76hNLQvD7IdWBHjScO2CK9y6cB31j0yTYVyUfbgKz_XPEkc_wZpAuFRljiUH9dJDyL7cMZsHdMi4MjnNkVBPqsZdwGWr4XRXSkucjtbE";
//
////        FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender("/topics/all",
//        FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(token,
//                name, text,
//                getApplicationContext(), RoomChatActivity.this);
//        fcmNotificationsSender.SendNotifications();
    }

}