package com.example.messenger.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.messenger.AbtractClass.LoadMoreScrollListenner;
import com.example.messenger.Adapter.MessHistoriAdapter;
import com.example.messenger.MainActivity;
import com.example.messenger.Model.HistoryMess;
import com.example.messenger.Model.User;
import com.example.messenger.Model.UserFont;
import com.example.messenger.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    public ChatFragment() {
        // Required empty public constructor
    }



    private String myUserName;
    private MainActivity activity;
    private DatabaseReference reference;
    private RecyclerView rcv;
    private MessHistoriAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    List<HistoryMess> listMess;
    List<UserFont> listUserFont;

    //loadmore
    private boolean isLoading;
    private boolean isCurentPage;
    private static final int totalLoad = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        initUI(view);
        setUpRcv();

        return view;
    }

    private void initUI(View view) {
        activity = (MainActivity) getActivity();
        reference = FirebaseDatabase.getInstance().getReference();
        rcv = view.findViewById(R.id.frg_chat_rcv);

        listMess = new ArrayList<>();
        listUserFont = new ArrayList<>();

        myUserName = activity.getSharedPreferences("dataUser", activity.MODE_PRIVATE).getString("userName", "");
    }

    private void setUpRcv() {


        adapter = new MessHistoriAdapter(activity);
        linearLayoutManager = new LinearLayoutManager(activity);
        getListMessHistory();
        getListUserFont();
        adapter.setData(listUserFont, listMess);

        rcv.setAdapter(adapter);
        rcv.setLayoutManager(linearLayoutManager);

//// check srcoll để load more
//        rcv.addOnScrollListener(new LoadMoreScrollListenner(linearLayoutManager) {
//            @Override
//            public void loadMoreItem() {    // nếu lướt đến thg cuối nó vào đây
//                if (listMess.size() >= totalLoad) {
//                    isLoading = true;           // bằng true để return isloading bên dưới để k spam vào đây nữa (xem bên abtract thì biết)
//                    getListMessHistory();
//                }
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//
//            @Override
//            public boolean isLastPage() {       // nếu không load đc nữa thả cái nay k load nữa
//                return isCurentPage;
//            }
//        });
    }

    private void getListMessHistory() {

/////      add item
//        for (int i = 25; i < 50; i++){
//            HistoryMess mess = new HistoryMess(i, "okeodsf", "dohaiquan", "oke", "dohaiquan" + i, true, "18:44:2218/02/2023");
//
//            reference.child("User")
//                    .child("bekoi10a6")
//                    .child("historiMes")
//                    .child("" + i).setValue(mess);
//        }

////   thêm điều kiện để load
//        Query query = reference.child("User").child(user.getUserName()).child("historiMes")
//                .orderByChild("id")     // queery bằng id
//                .startAfter(listMess.size() - 1)    // bắt đầu lấy > 4
//                .limitToFirst(listMess.size() - 1 + totalLoad); // chỉ load sô item chỉ đinh

        Query query = reference.child("User").child(myUserName)
                .child("historiMes").orderByChild("timeLastSent");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMess.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HistoryMess mess = ds.getValue(HistoryMess.class);
                    if (mess != null) {
                        listMess.add(0,mess);
                    }
                }

//// logic khi load more  sai vcl :v
//                if (listMessTemp.size() == 0) {
//                    isCurentPage = true;
//                } else {
//                    isLoading = false;

                    adapter.notifyDataSetChanged();
                    Log.e("ListHistotio2", listMess.toString());
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getListUserFont() {
        reference.child("UserFont").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUserFont.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    listUserFont.add(ds.getValue(UserFont.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}