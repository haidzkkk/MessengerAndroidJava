package com.example.messenger;

import com.example.messenger.Model.HistoryMess;
import com.example.messenger.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DuLieuGocKhiMatDuLieu {
    // dữ liệu realtime database của firebase
    // muốn dùng app thì nên setUp dữ liệu trước không sẽ có lỗi

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public void setUpDataUser(){
        List<User> listUser = new ArrayList<>();
        listUser.add( new User("ph20645", "Do Thanh Hai", "bekoi10a6", "thanhhaiaaA", "bekoi10a6@gmail.com", "", false, false));
        listUser.add( new User("ph23231", "Vu Van Toan", "toancon", "toancon", "toancon@gmail.com", "", false, false));
        listUser.add( new User("ph20881", "Do Thanh Hai", "giangxinh", "giangxinh", "giangxinh@gmail.com", "", false, false));

        for (int i = 0; i < listUser.size(); i++){
            reference.child("User").child(listUser.get(i).getUserName()).setValue(listUser.get(i));
        }
    }

//    public void setUpdataHistoriBekoi10a6(){
//        HistoryMess mess = new HistoryMess("ph20645-ph23231", "")
//    }
}
