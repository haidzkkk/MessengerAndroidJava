package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.messenger.Model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout tilUsername, tilPass, tilEmail;
    EditText edtFirstname, edtLastname;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();

        btnSignUp.setOnClickListener( v ->{
            signUpAccount();
        });

    }

    private void initUI() {
        tilUsername = findViewById(R.id.signup_til_username);
        tilPass = findViewById(R.id.signup_til_pass);
        tilEmail = findViewById(R.id.signup_til_email);
        edtFirstname = findViewById(R.id.signup_edt_firstname);
        edtLastname = findViewById(R.id.signup_edt_lastname);
        btnSignUp = findViewById(R.id.signup_btn_signup);

    }

    private void signUpAccount() {

        String strname = edtFirstname.getText().toString().trim() + " " + edtLastname.getText().toString().trim();
        String struserName = tilUsername.getEditText().getText().toString().trim();
        String strpass = tilPass.getEditText().getText().toString().trim();
        String stremail = tilEmail.getEditText().getText().toString().trim();
        String id = getRandomID(7);
        boolean isLogin = false;
        boolean isStatus = false;
        User user = new User(id, strname, struserName, strpass, stremail, "", isLogin, isStatus);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(struserName);
        reference.setValue(user);
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

    private void checkIsEmty(){

    }

}