package com.example.kohsheen.firebasedemoforkoshu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.alhazmy13.imagefilter.ImageFilter;

import java.util.ArrayList;
import java.util.List;

public class DisplayImagesActivity extends AppCompatActivity {


    // Creating RecyclerView.Adapter.
    private RecyclerView.Adapter adapter ;
    private Toolbar toolbar;
    private String image_path;
    // Creating List of ImageUploadInfo class.
    private List<Post> list = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //just added

        toolbar = (Toolbar) findViewById(R.id.filterstoolbar);
        //toolbar.setTitle("");
        //toolbar.getElevation();
        setSupportActionBar(toolbar);









        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);
        imageView=(ImageView)findViewById(R.id.dp);






        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button mLogoutBtn = (Button)findViewById(R.id.mLogoutBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");



        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {


            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                list.clear();
                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Post imageUploadInfo = postSnapshot.getValue(Post.class);

                    list.add(imageUploadInfo);
                }

                adapter = new RecyclerViewAdapter(getApplicationContext(), list);
                mRecyclerView.setAdapter(adapter);
                Toast.makeText(DisplayImagesActivity.this,"working",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DisplayImagesActivity.this,"not getting uploaded",Toast.LENGTH_SHORT).show();
            }
        });







        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();

                updateUI();

            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivity(new Intent(DisplayImagesActivity.this,ChooseImageACtivity.class));
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }








    public class InstaViewHolder extends RecyclerView.ViewHolder{

        public InstaViewHolder(View itemView) {
            super(itemView);
        }
    }







        @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        if(currentUser==null) {
            updateUI();
        }
    }

    private void updateUI() {
        Toast.makeText(DisplayImagesActivity.this,"you are logged out",Toast.LENGTH_SHORT).show();
        Intent DisplayImagesActivity = new Intent(DisplayImagesActivity.this,MainActivity.class);
        startActivity(DisplayImagesActivity);
        finish();
    }

    //just added
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {

            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();

            updateUI();
        }
        return super.onOptionsItemSelected(item);
    }




    }
