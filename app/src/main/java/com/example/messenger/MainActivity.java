package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.Adapter.ListNavAdapter;
import com.example.messenger.Adapter.MessHistoriAdapter;
import com.example.messenger.ClassTool.ConvertImg;
import com.example.messenger.Fragment.BottomSheetInfomationUserFagement;
import com.example.messenger.Fragment.ChatFragment;
import com.example.messenger.Model.HistoryMess;
import com.example.messenger.Model.User;
import com.example.messenger.Model.UserFont;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigation;
    BottomNavigationView botton_nav;
    Toolbar toolbar;
    RecyclerView rcvNav;

    // nav & header
    ListNavAdapter navAdapter;
    ImageView imgAvtHeader, imgSettingHeader;
    TextView tvNameHeader, tvTitleToolbar;
    RelativeLayout lnMainHeader;

    ProgressDialog progressDialog;

    private DatabaseReference reference;
    private User user;
    private List<HistoryMess> listHistori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        progressDialog.show();
        setUpToolBar();
        setUpNavigation();

        setUpdataUser();

        checkRequestPermistionNotification();

        lnMainHeader.setOnClickListener(v -> {
            detailUser();
        });

    }




    private void initUI() {

        drawerLayout = findViewById(R.id.main_main);
        navigation = findViewById(R.id.main_nav);
        botton_nav = findViewById(R.id.main_bottom_nav);
        toolbar = findViewById(R.id.main_toobar);
        tvTitleToolbar = findViewById(R.id.main_toobar_tv_title);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Dang load");

        listHistori = new ArrayList<>();

        // nav
        imgAvtHeader = navigation.getHeaderView(0).findViewById(R.id.header_img_avt);
        lnMainHeader = navigation.getHeaderView(0).findViewById(R.id.header_rl_main);
        tvNameHeader = navigation.getHeaderView(0).findViewById(R.id.header_tv_name);
        rcvNav = findViewById(R.id.main_nav_rcv_chat);
        navAdapter = new ListNavAdapter();

        reference = FirebaseDatabase.getInstance().getReference();

    }

    private void setUpdataUser() {      // t??m d??? li???u c???a m??nh ????? up l??n profile

        String strUsername = getSharedPreferences("dataUser", MODE_PRIVATE).getString("userName", "");
        reference.child("User").child(strUsername).child("login").setValue(true);
        reference.child("User").child(strUsername).child("status").setValue(true);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dang);
//        String img = ConvertImg.convertBitmapToBaseString(bitmap);
//        reference.child("User").child(strUsername).child("img").setValue(img);

        reference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User userTemp = snapshot.getValue(User.class);

                if (userTemp.getUserName() != null) {                /// check user c?? ph???i l?? user ???o kh??ng (user ???o l?? userfont nh??ng userDeitail l???i k c?? th??ng tin)

                    if (userTemp.getUserName().equals(strUsername)) {
                        user = snapshot.getValue(User.class);
                        Log.e("usser_Main", user.toString());

                        SharedPreferences.Editor editor = getSharedPreferences("dataUser", MODE_PRIVATE).edit();
                        editor.putBoolean("isLogin", true);
                        editor.commit();

                        tvNameHeader.setText(user.getName());
                        imgAvtHeader.setImageBitmap(ConvertImg.convertBaseStringToBitmap(user.getImg()));
                        getListHistori();
                        setUpBottonNavigation();
                        progressDialog.dismiss();


                        // add tam
//                SimpleDateFormat simpleFormatter =  new SimpleDateFormat("HH:mm:ssdd/MM/yyyy");
//                List<HistoryMess> list = new ArrayList<>();
//                list.add(new HistoryMess(0, "ph20645-ph12345", "ph12345", "oi", "ph12345",
//                        false,simpleFormatter.format(Calendar.getInstance().getTime())));
//
//                reference.child("User").child(user.getUserName()).child("historiMes")
//                        .setValue(list);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User userTemp = snapshot.getValue(User.class);

                if (userTemp.getUserName() != null) {                /// check user c?? ph???i l?? user ???o kh??ng (user ???o l?? userfont nh??ng userDeitail l???i k c?? th??ng tin)

                    if (userTemp.getUserName().equals(strUsername)) {
                        user = snapshot.getValue(User.class);
                        Log.e("usser_Main", user.toString());
                        tvNameHeader.setText(user.getName());
                        imgAvtHeader.setImageBitmap(ConvertImg.convertBaseStringToBitmap(user.getImg()));
                    }

                }
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

    private void getListHistori() {
        final int[] countMesSeen = {0};

        reference.child("User").child(user.getUserName()).child("historiMes")
                .addChildEventListener(new ChildEventListener() {
                                           @Override
                                           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                        HistoryMess historyMess = snapshot.getValue(HistoryMess.class);
//                        if (historyMess != null) {
//                            listHistori.add(historyMess);
////                            adapter.notifyDataSetChanged();
//
//                            // check ????? b???n index v??o nav
//                            if (!historyMess.getSeen()) {
//                                countMesSeen[0]++;
//                            }
//                        }
//                        navAdapter.setData(countMesSeen[0]);
//                        Log.e("ListHistotio", listHistori.toString());
                                           }

                                           @Override
                                           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                        HistoryMess historyMess = snapshot.getValue(HistoryMess.class);
//                        if (historyMess == null) {
//                            return;
//                        }
//
//                        for (int i = 0; i < listHistori.size(); i++) {
//                            if (listHistori.get(i).getId() == historyMess.getId()) {
//                                listHistori.set(i, historyMess);
//                            }
//                        }
////                        adapter.notifyDataSetChanged();
//
//                        // check ????? b???n index v??o nav
//                        if (!historyMess.getSeen()) {
//                            countMesSeen[0]++;
//                        }else {
//                            countMesSeen[0]--;
//                        }
//                        navAdapter.setData(countMesSeen[0]);
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
                                       }
                );

    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);


    }

    private void setUpNavigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();     // d??n icon v??o tooolbar

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

        rcvNav.setAdapter(navAdapter);
        rcvNav.setLayoutManager(new GridLayoutManager(this, 1));
    }


    private void setUpBottonNavigation() {
        rePlaceFragment(new ChatFragment());
        tvTitleToolbar.setText(R.string.main_toolbar_title);
        botton_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_botton_nav_chat: {
                        rePlaceFragment(new ChatFragment());
                        tvTitleToolbar.setText(R.string.main_toolbar_title);
                        break;
                    }
                    case R.id.menu_botton_nav_call: {
//                        rePlaceFragment(new ChatFragment());
                        break;
                    }
                    case R.id.menu_botton_nav_phonebook: {

                        break;
                    }
                    default: {
                        rePlaceFragment(new ChatFragment());
                        tvTitleToolbar.setText(R.string.main_toolbar_title);
                    }

                }

                return false;
            }
        });
    }


    private void rePlaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, fragment).commit();
    }

    private void detailUser() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putBoolean("myAccount", true);

        BottomSheetInfomationUserFagement bottomSheet = new BottomSheetInfomationUserFagement();
        bottomSheet.setArguments(bundle);
        bottomSheet.show(this.getSupportFragmentManager(), bottomSheet.getTag());

    }


    // ph????ng th???c b???t l???y action c???a ng?????i d??ng l?? cho ch??p hay t??? ch???i permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BottomSheetInfomationUserFagement.CODE_PERMISSION_READ_STORAGE) {   // n???u requestCode b???ng v???i code da xin quy???n kia th?? ???? cho th?? check action
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Oke123", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Vui l??ng cho ph??p truy c???p th?? vi???n", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 789) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    public void checkRequestPermistionNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {Manifest.permission.POST_NOTIFICATIONS};
                requestPermissions(permission, 789);
            }
        }
    }
}