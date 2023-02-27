package com.example.messenger.DataMessagingNotification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.example.messenger.ClassTool.ConvertImg;
import com.example.messenger.ClassTool.MyApplication;
import com.example.messenger.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // messaging binh thuong
//        String title = remoteMessage.getNotification().getTitle();
//        String message = remoteMessage.getNotification().getBody();

        // get data messaging
        // c1.
        Map<String, String> stringMap = remoteMessage.getData();
        String title = stringMap.get("title");
        String message = stringMap.get("text");
//        String icon = stringMap.get("");

        // c2.
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        if (notification == null) {
//            return;
//        }
//        String title = notification.getTitle();
//        String message = notification.getBody();
//        String icon = notification.getIcon();

        sendNotification(title, message, "icon");
    }

    private void sendNotification(String name, String text, String icon) {
//        ConvertImg.convertBaseStringToIntRource(getApplicationContext(), icon)
        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANEL_ID)
                .setContentTitle(name)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(1, notification);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("token", token);
    }
}


