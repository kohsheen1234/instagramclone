package com.example.kohsheen.firebasedemoforkoshu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ChooseImageACtivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_CONTACT_GALLERY = 2 ;
    private Button choosefromcamera;
    private Button choosefromlibrary;
    private TextView btnnext;
    private ImageView imageView;
    private Uri postUri;
    private Bitmap bitmap;
    private String a;
    private boolean isimagechosen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image_activity);
        choosefromcamera=(Button)findViewById(R.id.choosefromcamera);
        choosefromlibrary=(Button)findViewById(R.id.choosefromlibrary);
        imageView=(ImageView)findViewById(R.id.imageView);
        btnnext=(TextView) findViewById(R.id.next);
        a=null;
        bitmap=null;


       choosefromcamera.setOnClickListener(this);
        choosefromlibrary.setOnClickListener(this);
        btnnext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.choosefromcamera:
                choosefromcamerafunc();
                break;
            case R.id.choosefromlibrary:
                choosefromlibraryfunc();
                break;
            case R.id.next:

                nextfunc();
                break;
        }
    }

    private void nextfunc() {

        if(a!=null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent .setClass(ChooseImageACtivity.this, ApplyFiltersActivity.class);
            intent .putExtra("KEY",postUri.toString());
            startActivity(intent);
            Toast.makeText(this,"from gallery",Toast.LENGTH_SHORT).show();
        }



        else if(a==null && bitmap!=null){
            Toast.makeText(this,"from camera",Toast.LENGTH_SHORT).show();
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] byteArray = bStream.toByteArray();

            Intent anotherIntent = new Intent(ChooseImageACtivity.this, ApplyFiltersActivity.class);
            anotherIntent.putExtra("image", byteArray);
            startActivity(anotherIntent);
            finish();
        }
        else if(a==null && bitmap==null)
        {
            Toast.makeText(this,"Select an image to share!",Toast.LENGTH_SHORT).show();
        }


    }



    private void choosefromcamerafunc() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void choosefromlibraryfunc() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , PICK_CONTACT_GALLERY);//one can be replaced with any action code
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_CONTACT_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    postUri = data.getData();
                    a =postUri.toString();
                    imageView.setImageURI(postUri);
                    Toast.makeText(ChooseImageACtivity.this , a, Toast.LENGTH_SHORT).show();

                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    postUri = data.getData();
                    imageView.setImageURI(postUri);
                    imageView.setImageBitmap(bitmap);
                }
                break;

        }
    }
}
