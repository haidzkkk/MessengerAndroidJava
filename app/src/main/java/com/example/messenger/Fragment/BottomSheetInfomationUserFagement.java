package com.example.messenger.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.messenger.ClassTool.ConvertImg;
import com.example.messenger.LogInActivity;
import com.example.messenger.MainActivity;
import com.example.messenger.Model.User;
import com.example.messenger.R;
import com.example.messenger.SplahScreenActivity2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;

public class BottomSheetInfomationUserFagement extends BottomSheetDialogFragment {

    public static final int CODE_PERMISSION_READ_STORAGE = 10;

    private RelativeLayout rLayout;
    private Bitmap bitmapImgEdit;
    private TextView tvFinish, tvName, tvUserName, tvEmail;
    private ImageView imgAvt;
    private Button btnEdit, btnLogout;

    private FragmentActivity activity;

    private boolean isEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_bottomsheet_detail_user, null);

        rLayout = view.findViewById(R.id.detail_user_layout_option);
        tvFinish = view.findViewById(R.id.detail_user_tv_finish);
        tvName = view.findViewById(R.id.detail_user_tv_name);
        tvUserName = view.findViewById(R.id.detail_user_tv_username);
        tvEmail = view.findViewById(R.id.detail_user_tv_email);
        imgAvt = view.findViewById(R.id.detail_user_img_avt);
        btnEdit = view.findViewById(R.id.detail_user_btn_edit);
        btnLogout = view.findViewById(R.id.detail_user_btn_logout);
        activity = getActivity();
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) view.getParent()).setBackgroundColor(Color.TRANSPARENT);


        Bundle bundle = getArguments();
        boolean myAccount = bundle.getBoolean("myAccount");
        User user = (User) bundle.getSerializable("user");
        if (user == null) {
            return;
        }

        tvFinish.setOnClickListener(v -> {
            this.dismiss();
        });
        stateUser(myAccount);
        tvName.setText(user.getName());
        tvUserName.setText(user.getUserName());
        tvEmail.setText(user.getEmail());

        imgAvt.setImageBitmap(ConvertImg.convertBaseStringToBitmap(user.getImg()));

        if (myAccount) {  // nếu của mình với đc sử dụng chức năng

            imgAvt.setOnClickListener(v -> {
                BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(R.layout.dialog_bottom_sheet_image_avata);

                dialog.findViewById(R.id.dialog_bottom_sheet_imgavt_thuvien).setOnClickListener(view1 -> {
                    moThuVien();
                    dialog.dismiss();
                });
                dialog.findViewById(R.id.dialog_bottom_sheet_imgavt_cammera).setOnClickListener(view1 -> {
                    checkRequestPermistion();
                    dialog.dismiss();
                });
                dialog.show();
            });

            btnLogout.setOnClickListener(v -> {
                setUpLogout();
            });

            btnEdit.setOnClickListener(v -> {
                if (isEdit){
                    updateUser(user);
                }

            });
        }else {
            imgAvt.setOnClickListener(v -> {
                moZoomAnh(user.getImg());
            });
        }
    }


    private void setUpLogout() {

        startActivity(new Intent(activity, LogInActivity.class));
        activity.finishAffinity();

        SharedPreferences.Editor editor = activity.getSharedPreferences("dataUser", activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();


    }

    private void moZoomAnh(String strImg) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_zoom_image);
        dialog.show();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView imageView = dialog.findViewById(R.id.dialog_zoom_image);
        imageView.setImageBitmap(ConvertImg.convertBaseStringToBitmap(strImg));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkRequestPermistion() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            moCamera();
        }
//         check người dùng cho sử dụng quyền READ_EXTERNAL_STORAGE chưa
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            moCamera();
        } else {
            String[] permission = {Manifest.permission.CAMERA};
            getActivity().requestPermissions(permission, CODE_PERMISSION_READ_STORAGE);
        }
    }

    public void moCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 999);

    }

    public void moThuVien() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        activityResultLauncher.launch(intent);
        startActivityForResult(intent, 888);
    }

// cách lấy ảnh cũ không nên dùng
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == activity.RESULT_OK && data != null) {
            if (requestCode == 999){    // camera
                bitmapImgEdit = (Bitmap) data.getExtras().get("data");
                imgAvt.setImageBitmap(bitmapImgEdit);
            }else if( requestCode == 888){  // thư viện
                Uri uri = data.getData();
                try { bitmapImgEdit = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);} catch (IOException e) {throw new RuntimeException(e);   }
                imgAvt.setImageBitmap(bitmapImgEdit);
            }
            isEdit = true;
        }
    }

// cách lấy ảnh kiểu mới ( khuyên dùng ) nhưng kh biết lấy như nào :v, chỉ lấy được mỗi ảnh từ thư viện đc còn từ camera k biết lấy :v
//        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) { // sẽ trả về cho sau khi chọn ảnh
//
//                    // xử lý kết quả trả về
//                    Uri uri;
//                    if (result.getResultCode() == getActivity().RESULT_OK) {
//                        Intent intent = result.getData();
//
//                        if (intent == null) {
//                            uri = (Uri) result.getData().getExtras().get("data");
//                        } else {
//                            uri = intent.getData();
//                        }
//
//                        try {
//                            bitmapImgEdit = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                            imgAvt.setImageBitmap(bitmapImgEdit);
//                            stateUser(true);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }
//    );


    private void stateUser(boolean myAccount) {
        if (!myAccount) {
            rLayout.setVisibility(View.GONE);
        }else {
            rLayout.setVisibility(View.VISIBLE);
        }

    }

    private void updateUser(User user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("User").child(user.getUserName()).child("img").
                setValue(ConvertImg.convertBitmapToBaseString(bitmapImgEdit))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Update Success", Toast.LENGTH_SHORT).show();
                            isEdit = false;
                        }
                    }
                });

        reference.child("UserFont").child(user.getUserName()).child("img")
                . setValue(ConvertImg.convertBitmapToBaseString(bitmapImgEdit));
    }

}
