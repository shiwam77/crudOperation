package com.aoezdemir.todoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    EditText Eemail, Epassword;
    Button Blogin, Bsignup;
    String email, password;
    private String globalEmail = "shiwamkarn77@gmail.com";
    private String globalPassword = "123456";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Eemail = findViewById(R.id.inputEmail);
        Epassword = findViewById(R.id.inputPassword);
        Blogin = findViewById(R.id.Login_btnlogin);
        Bsignup = findViewById(R.id.sign_up);

        Bsignup.setOnClickListener((View v) ->{
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            startActivity(intent);


        });
        Blogin.setOnClickListener((View v) -> {

            email = Eemail.getText().toString();
            password = Epassword.getText().toString();


            //if condition for null value in edittext
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Enter username and Password", Toast.LENGTH_SHORT).show();
            }

            //Email pattern and password length checked
            else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Intent intent = new Intent(v.getContext(), OverviewActivity.class);
                startActivity(intent);
            }


            //if email pattern is wrong show this message
            else {
                Toast.makeText(LoginActivity.this, "Please re-enter your email ", Toast.LENGTH_LONG).show();
                Eemail.setError(" Valid email is required");
            }

        });
    }


    public void getemail() {
        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("email",email );
        Ed.apply();
    }
}