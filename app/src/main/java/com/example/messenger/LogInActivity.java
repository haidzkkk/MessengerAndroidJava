package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.messenger.Model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogInActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextInputLayout tilUserName, tilPass;
    private LinearLayout lnSignup;

    private ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initUI();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUserName = tilUserName.getEditText().getText().toString().trim();
                String strPass = tilPass.getEditText().getText().toString().trim();
                dialog.show();

                getData(strUserName, strPass);
            }
        });

        lnSignup.setOnClickListener(v ->{
            startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
        });


    }

    private void initUI() {

        btnLogin = findViewById(R.id.login_btn_login);
        tilUserName = findViewById(R.id.login_til_userName);
        tilPass = findViewById(R.id.login_til_pass);
        lnSignup = findViewById(R.id.login_ln_sign_up);

        dialog = new ProgressDialog(this);
    }

    private void getData(String userName, String pass) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(userName);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null || !user.getPass().equals(pass)){
                    Toast.makeText(LogInActivity.this, R.string.login_faile, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                // lưu user name sau còn check
                SharedPreferences.Editor editor = getSharedPreferences("dataUser", MODE_PRIVATE).edit();
                editor.putString("userName", user.getUserName());
                editor.commit();

                reference.onDisconnect();
                dialog.dismiss();
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                finish();
                Log.e("infoUsser", user.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogInActivity.this, R.string.login_faile, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

}