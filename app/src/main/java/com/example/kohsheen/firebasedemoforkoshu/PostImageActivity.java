package com.example.kohsheen.firebasedemoforkoshu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostImageActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText titleEditText;
    private EditText descreptionEditText;
    private ImageView newPostImageView;
    private TextView buttonPost;
    private ProgressBar uploadImageProgressBar;
    private Bitmap bmp;


    private Uri postUri;
    private Uri postUriCamera;
    private Uri downloadUrl;
    String strFilePath;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageRefrence;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        titleEditText=(EditText)findViewById(R.id.titleEditText);
        descreptionEditText=(EditText)findViewById(R.id.descriptionEditText);
        newPostImageView=(ImageView)findViewById(R.id.newPostImageView);
        uploadImageProgressBar=(ProgressBar)findViewById(R.id.uploadImageProgressBar);
        buttonPost=(TextView) findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRefrence = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //user=FirebaseUser.


            Intent intent = getIntent();
            postUri = Uri.parse(intent.getExtras().getString("NEWIMAGE"));

        Toast.makeText(this, postUri.toString(), Toast.LENGTH_SHORT).show();

        newPostImageView.setImageURI(postUri);


    }




    private void postImage() {

        final String title;
        final String description;
        title=titleEditText.getText().toString();
        description=descreptionEditText.getText().toString();

        if(TextUtils.isEmpty(title))
        {
            titleEditText.setError("Enter a title");
            titleEditText.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(description)){
            descreptionEditText.setError("Enter a description");
            descreptionEditText.requestFocus();
            return;
        }
        Toast.makeText(this,"Uploading.. ",Toast.LENGTH_SHORT).show();

        uploadImageProgressBar.setVisibility(View.VISIBLE);

        storageRefrence.child(firebaseAuth.getCurrentUser().getUid()+"/"+postUri.getLastPathSegment()).putFile(postUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        downloadUrl = taskSnapshot.getDownloadUrl();
                        updateDatabase();


                    }

                    private void updateDatabase() {
                        final Post post = new Post(title,description,firebaseAuth.getCurrentUser().getDisplayName(),downloadUrl.toString());

                        databaseReference.child("posts").child(firebaseAuth.getCurrentUser().getUid()+postUri.getLastPathSegment()).setValue(post)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        uploadImageProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(PostImageActivity.this,"Post successful",Toast.LENGTH_SHORT).show();
                                        Toast.makeText(PostImageActivity.this,post.getUrl(),Toast.LENGTH_SHORT ).show();

                                        finish();
                                        startActivity(new Intent(PostImageActivity.this, DisplayImagesActivity.class));

                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uploadImageProgressBar.setVisibility(View.GONE);
                Toast.makeText(PostImageActivity.this,e.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.buttonPost:
                postImage();
        }
    }
}
