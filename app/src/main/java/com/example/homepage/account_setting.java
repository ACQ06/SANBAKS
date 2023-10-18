package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class account_setting extends AppCompatActivity {
    EditText Username,password;
    TextView SignUp;
    String user;
    Button LogIn,back;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        init();
    }


    public void init() {

        // database  method here


        Username = findViewById(R.id.etUserName);
        password = findViewById(R.id.etPassword);
        SignUp = findViewById(R.id.tvSignUp);
        LogIn = findViewById(R.id.btnLogIn);
        back = findViewById(R.id.btnBackAccountS);

        //if username and pass is correct then the user can log in
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn();
            }
        });

        // goes to signup page
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
}
    public void LogIn() {
            // fetch from database yung pass confirmation with naka switch case

                Intent h = new Intent(account_setting.this, MainActivity.class);
                user = Username.getText().toString();
                h.putExtra("username", user);
                startActivity(h);

    }
    private void SignUp() {
        Intent SignUp = new Intent(account_setting.this, account_creation.class);
        startActivity(SignUp);
    }


}