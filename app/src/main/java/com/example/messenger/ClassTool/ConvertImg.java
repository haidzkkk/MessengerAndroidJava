package com.example.messenger.ClassTool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.example.messenger.R;

import java.io.ByteArrayOutputStream;

public class ConvertImg {
    public static String convertBitmapToBaseString(Bitmap bitmap) {
        ByteArrayOutputStream fileInputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fileInputStream);
         return Base64.encodeToString(fileInputStream.toByteArray(), Base64.DEFAULT);
    }
    public static Bitmap convertBaseStringToBitmap(String baseString) {
        byte b[]=Base64.decode(baseString,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(b,0,b.length);
    }

    public static int convertBaseStringToIntRource(Context context, String baseString){
        int resourceImage = context.getResources().getIdentifier(baseString, "drawable", context.getPackageName());
        if (resourceImage ==0 ) resourceImage = R.mipmap.ic_launcher;
        return resourceImage;
    }
}
