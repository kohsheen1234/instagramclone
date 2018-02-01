package com.example.kohsheen.firebasedemoforkoshu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText emEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView navigateToSignUpTextView;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(this,DisplayImagesActivity.class));
            finish();
        }


            emEditText = (EditText) findViewById(R.id.loginEmailEditText);
            passwordEditText = (EditText) findViewById(R.id.loginPasswordEditText);
            loginButton = (Button) findViewById(R.id.loginButton);
            navigateToSignUpTextView = (TextView) findViewById(R.id.navigateToSignUpTextView);
            progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
            loginButton.setOnClickListener(this);
            navigateToSignUpTextView.setOnClickListener(this);
        }


    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.loginButton:
                login();
                break;
            case R.id.navigateToSignUpTextView:
                navigateToSignUp();
                break;
        }
    }
    private void navigateToSignUp() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    private void login() {
        String email = emEditText.getText().toString().trim();
        String password= passwordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            emEditText.setError("Enter an email Id");
            emEditText.requestFocus();
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emEditText.setError("Enter a valid emailId");
            emEditText.requestFocus();
            return;

        }

        if(TextUtils.isEmpty(password))
        {
            passwordEditText.setError("Enter a password");
            passwordEditText.requestFocus();
            return;
        }

        if(password.length()< 6)
        {
            passwordEditText.setError("Enter a password of atleast 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);



        //oncomplete listner is trigerred when the signin is completed(success or failure)
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"Successfully Logged in",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,DisplayImagesActivity.class));

//                   startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                    finish();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



}
