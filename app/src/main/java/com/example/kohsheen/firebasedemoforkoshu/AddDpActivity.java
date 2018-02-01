package com.example.kohsheen.firebasedemoforkoshu;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.UUID;

import static com.example.kohsheen.firebasedemoforkoshu.R.id.uploadImageProgressBar;

public class AddDpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_CONTACT = 2;
    private static final int PICK_CONTACT_GALLERY =3 ;
    private Button AddDpButton;
    private TextView NextButton;

    private ImageView Displaypic;
    private Uri postUri;
    private String a;
    private Uri downloadUrl;


    FirebaseStorage storage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dp);

        AddDpButton= (Button)findViewById(R.id.addprofilepic);
        NextButton = (TextView)findViewById(R.id.next);
        Displaypic=(ImageView)findViewById(R.id.displaypic);


        NextButton.setOnClickListener(this);
        AddDpButton.setOnClickListener(this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        postUri=null;

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.addprofilepic:
                AddDp();
                break;
            case R.id.next:
                Skip();
                break;
        }
    }

    private void AddDp() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , PICK_CONTACT_GALLERY);//one can be replaced with any action code



    }



    //just added
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            postUri = data.getData();
            a = postUri.toString();
            Displaypic.setImageURI(postUri);
            Toast.makeText(AddDpActivity.this, a, Toast.LENGTH_SHORT).show();

        }
    }

    private void Skip() {

        startActivity(new Intent(AddDpActivity.this, DisplayImagesActivity.class));

        finish();
    }
}
