package com.example.messenger.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.messenger.AbtractClass.LoadMoreScrollListenner;
import com.example.messenger.Adapter.MessHistoriAdapter;
import com.example.messenger.ClassTool.RecycleviewItemTouchHelper;
import com.example.messenger.Interface.ItemTouchHeplerListenner;
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


public class ChatFragment extends Fragment implements ItemTouchHeplerListenner {

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

//// check srcoll ????? load more
//        rcv.addOnScrollListener(new LoadMoreScrollListenner(linearLayoutManager) {
//            @Override
//            public void loadMoreItem() {    // n???u l?????t ?????n thg cu???i n?? v??o ????y
//                if (listMess.size() >= totalLoad) {
//                    isLoading = true;           // b???ng true ????? return isloading b??n d?????i ????? k spam v??o ????y n???a (xem b??n abtract th?? bi???t)
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
//            public boolean isLastPage() {       // n???u kh??ng load ??c n???a th??? c??i nay k load n???a
//                return isCurentPage;
//            }
//        });

        ItemTouchHelper.SimpleCallback simpleCallback = new RecycleviewItemTouchHelper(
                0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcv);
    }

    //get data item ???? ??c vu???t ????? x??? l??
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        HistoryMess mess = listMess.get(viewHolder.getAdapterPosition() -1);
        int indexDelete = viewHolder.getAdapterPosition() ;

        Log.e("index",indexDelete + " | list size: " + listMess.size() );
        new AlertDialog.Builder(activity)
                .setTitle("B???n c?? ch???c mu???n x??a cu???c tr?? chuy???n n??y kh??ng ?")
                .setPositiveButton("C??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference.child("User").child(myUserName).child("historiMes")
                                .child(mess.getIdMess()).removeValue();
                    }
                })
                .setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemChanged(indexDelete);
                    }
                })
                .setCancelable(false)
                .show();
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

////   th??m ??i???u ki???n ????? load
//        Query query = reference.child("User").child(user.getUserName()).child("historiMes")
//                .orderByChild("id")     // queery b???ng id
//                .startAfter(listMess.size() - 1)    // b???t ?????u l???y > 4
//                .limitToFirst(listMess.size() - 1 + totalLoad); // ch??? load s?? item ch??? ??inh

        Query query = reference.child("User").child(myUserName)
                .child("historiMes").orderByChild("timeLastSent");

//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listMess.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    HistoryMess mess = ds.getValue(HistoryMess.class);
//                    if (mess != null) {
//                        listMess.add(0,mess);
//                    }
//                }
//
//                    adapter.notifyDataSetChanged();
//                    Log.e("ListHistotio2", listMess.toString());
////                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HistoryMess mess = snapshot.getValue(HistoryMess.class);
                    if (mess != null) {
                        listMess.add(0,mess);

                    adapter.notifyDataSetChanged();
                    Log.e("ListHistotio2", listMess.toString());
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HistoryMess mess = snapshot.getValue(HistoryMess.class);
                if (mess == null && listMess.isEmpty()) {
                    return;
                }

                for (int i =0 ; i< listMess.size(); i++){
                    if (listMess.get(i).getIdMess().equals(mess.getIdMess())){
                        listMess.remove(i);
                        listMess.add(0, mess);
                        adapter.notifyDataSetChanged();
                        Log.e("ListHistotio2", listMess.toString());
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                HistoryMess mess = snapshot.getValue(HistoryMess.class);
                if (mess == null && listMess.isEmpty()) {
                    return;
                }

                for (int i =0 ; i< listMess.size(); i++){
                    if (listMess.get(i).getIdMess().equals(mess.getIdMess())){
                        listMess.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
                    UserFont userFont = ds.getValue(UserFont.class);

                    if (userFont == null | userFont.getUserName() == null) return;

                    if (userFont.getUserName().equals(myUserName)){     // neu la minh thi cho len dau
                        listUserFont.add(0, userFont);
                    }else listUserFont.add(userFont);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        reference.onDisconnect();
    }
}