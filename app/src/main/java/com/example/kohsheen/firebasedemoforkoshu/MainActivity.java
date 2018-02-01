package com.example.kohsheen.firebasedemoforkoshu;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG ="FACELOG" ;
    private EditText emEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private TextView navigateToLoginTextView;
    private ProgressBar progressBar;
    private CallbackManager mCallbackManager;
    private Button mFacebookBtn;
    private EditText usernameEditText;
    private String username;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            startActivity(new Intent(this,DisplayImagesActivity.class));
            finish();
        }

        emEditText = (EditText)findViewById(R.id.signUpEmailEditText);
        passwordEditText=(EditText)findViewById(R.id.signUpPasswordEditText);
        signUpButton=(Button)findViewById(R.id.signUpButton);
        navigateToLoginTextView=(TextView)findViewById(R.id.navigateToLoginTextView);
        progressBar=(ProgressBar)findViewById(R.id.signUpProgressBar);
        mFacebookBtn = (Button)findViewById(R.id.mFacebookBtn);
        usernameEditText=(EditText)findViewById(R.id.userNameEditText);
//        final String username = usernameEditText.getText().toString().trim();
        //LoginButton loginButton = (LoginButton)findViewById(R.id.button_facebook_login);

        signUpButton.setOnClickListener(this);
        navigateToLoginTextView.setOnClickListener(this);
        mFacebookBtn.setOnClickListener(this);

        // Initialize Facebook Login button
         mCallbackManager = CallbackManager.Factory.create();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.kohsheen.firebasedemoforkoshu",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }



    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.signUpButton:
                signUp();
                break;
            case R.id.navigateToLoginTextView:
                navigateToLogin();
                break;
            case R.id.mFacebookBtn:
                navigatetoWallActivity();
        }
    }

    private void navigatetoWallActivity() {
        username = usernameEditText.getText().toString().trim();

        if(TextUtils.isEmpty(username))
        {
            usernameEditText.setError("Enter a username");
            usernameEditText.requestFocus();
            return;
        }

        mFacebookBtn.setEnabled(false);

        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });



    }


    private void navigateToLogin() {
        startActivity(new Intent(this,LoginActivity.class));
    }

    private void signUp() {
        String email = emEditText.getText().toString().trim();
        String password= passwordEditText.getText().toString().trim();
        final String _username = usernameEditText.getText().toString().trim();

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

        if(TextUtils.isEmpty(_username))
        {
            usernameEditText.setError("Enter a username");
            usernameEditText.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);


        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(_username).build();
                   // firebaseUser.updateProfile(profileUpdates);

                    Toast.makeText(MainActivity.this,"Successfully Signed Up",Toast.LENGTH_SHORT).show();


                    //just added

                    startActivity(new Intent(MainActivity.this,AddDpActivity.class));
                    finish();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });

    }




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        if(currentUser!=null) {
            updateUI();
        }
    }

    private void updateUI() {

        Toast.makeText(MainActivity.this,"you are logged in",Toast.LENGTH_SHORT).show();
        Intent wallACtivity = new Intent(MainActivity.this,DisplayImagesActivity.class);
        startActivity(wallACtivity);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }



    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            firebaseUser = firebaseAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username).build();
                            firebaseUser.updateProfile(profileUpdates);

                            mFacebookBtn.setEnabled(true);

                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            mFacebookBtn.setEnabled(true);
                            updateUI();
                        }

                        // ...
                    }
                });
    }


}
