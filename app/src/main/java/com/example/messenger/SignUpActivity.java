package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messenger.Model.User;
import com.example.messenger.Model.UserFont;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout tilUsername, tilPass, tilEmail;
    EditText edtFirstname, edtLastname;
    Button btnSignUp;

    ProgressDialog dialog;

    DatabaseReference reference;

    boolean isSame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();

        btnSignUp.setOnClickListener(v -> {
            dialog.show();
            signUpAccount();
        });

        tilUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String struserName = tilUsername.getEditText().getText().toString().trim();
                checkIsUser(struserName);
            }
        });
    }

    private void initUI() {
        tilUsername = findViewById(R.id.signup_til_username);
        tilPass = findViewById(R.id.signup_til_pass);
        tilEmail = findViewById(R.id.signup_til_email);
        edtFirstname = findViewById(R.id.signup_edt_firstname);
        edtLastname = findViewById(R.id.signup_edt_lastname);
        btnSignUp = findViewById(R.id.signup_btn_signup);
        dialog = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference();


    }

    private void signUpAccount() {

        String strname = edtFirstname.getText().toString().trim() + " " + edtLastname.getText().toString().trim();
        String struserName = tilUsername.getEditText().getText().toString().trim();
        String strpass = tilPass.getEditText().getText().toString().trim();
        String stremail = tilEmail.getEditText().getText().toString().trim();
        String id = getRandomID(7);
        boolean isLogin = false;
        boolean isStatus = false;

        if (strname.trim().equals("") | checkIsEmty(tilUsername) | checkIsEmty(tilEmail) | checkIsEmty(tilPass)
            | id.equals("") | isSame) {
            Toast.makeText(this, "Sign up is faile", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            User user = new User(id, strname, struserName, strpass, stremail, "", isLogin, isStatus);
            UserFont userFont = new UserFont(strname, struserName, "");
            reference.child("User").child(struserName).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        SharedPreferences.Editor editor = getSharedPreferences("dataUser", MODE_PRIVATE).edit();
                        editor.putString("userName", user.getUserName());
                        editor.commit();

                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finishAffinity();
                        dialog.dismiss();
                    }
                }
            });
            dialog.dismiss();
            reference.child("UserFont").child(struserName).setValue(userFont);
        }

    }

    private void checkIsUser(String userName) {
        reference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isEmty = false;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    User userTemp = ds.getValue(User.class);

                    if (userTemp != null && userTemp.getUserName() != null){
                        if (userTemp.getUserName().equals(userName)) {
                            isEmty = true;
                            tilUsername.setError("The user already use someone else");
                            return;
                        }
                    }
                    Log.e("sdsadas", " " + userTemp.getUserName() );
                }

                isSame = isEmty;
                if (isEmty) {

                } else {
                    tilUsername.setError(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getRandomID(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Random random = new Random();
        StringBuilder id = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            id.append(AB.charAt(random.nextInt(AB.length())));
        }

        return id.toString();
    }

    private boolean checkIsEmty(TextInputLayout textInputLayout) {
        if (textInputLayout.getEditText().getText().toString().trim().equals("")) {
            textInputLayout.setHelperText("Mandatori");
            return true;
        }
        textInputLayout.setHelperText(null);
        return false;
    }

    private void getTestEverythink() {
//        HashMap<String, Object> objectHashMap = new HashMap<>();
        List<User> list = new ArrayList<>();
        reference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
//                    String key = ds.getKey();
                    User u = ds.getValue(User.class);
//                    objectHashMap.put(key, u);
                    list.add(u);
                }

                Log.e("dsfasdsa", list.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}