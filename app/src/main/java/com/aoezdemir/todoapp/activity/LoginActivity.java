package com.aoezdemir.todoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText etEmail;
    private EditText etPassword;
    private Button bLogin;
    private Button bRegister;
    private ProgressBar pbLogin;
    private TextView tvErrorInfo;
    private String globalEmail = "shiwamkarn77@gmail.com";
    private String globalPassword = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        tvErrorInfo = findViewById(R.id.tvErrorInfo);
        tvErrorInfo.setVisibility(View.INVISIBLE);
        pbLogin = findViewById(R.id.pbLogin);
        pbLogin.setVisibility(View.INVISIBLE);
        bLogin = findViewById(R.id.bLogin);
        bRegister = findViewById(R.id.bRegister);
        enableLoginButton();
        bRegister.setOnClickListener((View v) ->{
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            startActivity(intent);


        });
        bLogin.setOnClickListener((View v) -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            pbLogin.setVisibility(View.VISIBLE);

            if(isValidEmailAddress() && isValidPassword()){
                if(email.trim().equals(globalEmail.trim()) && password.trim().equals(globalPassword.trim())){
                    Intent intent = new Intent(v.getContext(), OverviewActivity.class);
                    startActivity(intent);
                    pbLogin.setVisibility(View.INVISIBLE);
                }else{
                    tvErrorInfo.setVisibility(View.VISIBLE);
                    Toast.makeText(v.getContext(), "Local error: Failed to authenticate user (client error)", Toast.LENGTH_SHORT).show();
                    pbLogin.setVisibility(View.INVISIBLE);
                }
            }else{
                Toast.makeText(v.getContext(), "Email or Password is not valid", Toast.LENGTH_SHORT).show();

            }


        });
    }

    private boolean isValidEmailAddress() {
        String email = etEmail.getText().toString();
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword() {
        String password = etPassword.getText().toString();
        return !password.isEmpty() && password.length() == 6;
    }

    private void enableLoginButton() {
        bLogin.setEnabled(true);
    }

    private void disableLoginButton() {
        bLogin.setEnabled(false);
    }
}