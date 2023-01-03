package com.aoezdemir.todoapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {


    EditText Efname, Elname, Eemail, Epassword, Ecpassword;
    Button rbtn;
    TextView redirecttologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //For input value
        Efname = findViewById(R.id.fname);
        Elname = findViewById(R.id.lname);
        Eemail = findViewById(R.id.Email);
        Epassword = findViewById(R.id.rpassword);
        Ecpassword = findViewById(R.id.rconfirmpassword);


        //For Registration and Login redirection Button
        rbtn = findViewById(R.id.btnRegister);
        redirecttologin = findViewById(R.id.btnRegister);

        //Clicklistner add for register button
        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //define string for input
                String fname = Efname.getText().toString();
                String lname = Elname.getText().toString();
                String email = Eemail.getText().toString();
                String password = Epassword.getText().toString();
                String cpassword = Ecpassword.getText().toString();


                //Emtpy value checked
                if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || password.isEmpty() || cpassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter all data", Toast.LENGTH_SHORT).show();
                } else {

                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        //password length checked
                        if (password.length() >= 8) {

                            //password and confirm password checked
                            if (password.equals(cpassword)) {
                                Intent intent = new Intent(view.getContext(), OverviewActivity.class);
                                startActivity(intent);
                            }
                            //incase password and confirm password
                            else {
                                Ecpassword.setError("confirm password doesn't match");
                                Toast.makeText(RegisterActivity.this, "confirm password doesn't match", Toast.LENGTH_SHORT).show();
                            }
                        }

                        //if password length doesn't match
                        else {
                            Epassword.setError("minimum 8 length");
                            Toast.makeText(RegisterActivity.this, "Enter 8 digit password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //if user didn't enter valid email address
                    else {
                        Eemail.setError("enter valid email");
                        Toast.makeText(RegisterActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}