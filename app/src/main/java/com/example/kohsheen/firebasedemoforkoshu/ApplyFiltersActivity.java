package com.example.kohsheen.firebasedemoforkoshu;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.alhazmy13.imagefilter.ImageFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class ApplyFiltersActivity extends AppCompatActivity implements View.OnClickListener{
    Bitmap bmp;
    private ImageView imageView;
    private ImageView ImageViewGrey;
    private ImageView ImageViewOld;
    private ImageView ImageViewLomo;
    private ImageView ImageViewHdr;
    private ImageView ImageViewSketch;
    private ImageView ImageViewLight;
    private ImageView ImageViewBlock;
    private ImageView ImageViewTv;
    private ImageView ImageViewNone;
    private ImageView ImageViewSoftglow;
    private TextView btnNext;



    BitmapDrawable drawable ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_filters);

        imageView=(ImageView)findViewById(R.id.imageView);
        ImageViewGrey=(ImageView)findViewById(R.id.grey);
        ImageViewNone=(ImageView)findViewById(R.id.none);
        ImageViewOld=(ImageView)findViewById(R.id.old);
        ImageViewLomo=(ImageView)findViewById(R.id.lomo);
        ImageViewHdr=(ImageView)findViewById(R.id.hdr);
        ImageViewLight=(ImageView)findViewById(R.id.light);
        ImageViewSketch=(ImageView)findViewById(R.id.sketch);
        ImageViewBlock=(ImageView)findViewById(R.id.block);
        ImageViewTv=(ImageView)findViewById(R.id.tv);
        ImageViewSoftglow=(ImageView)findViewById(R.id.softglow);
        btnNext=(TextView)findViewById(R.id.next);


        if(getIntent().hasExtra("image")) {

            Bundle extras = getIntent().getExtras();
            byte[] byteArray = extras.getByteArray("image");
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageView image = (ImageView) findViewById(R.id.imageView);
            ImageViewNone.setImageBitmap(bmp);

            image.setImageBitmap(bmp);

        }
        else
        {
            Intent intent = getIntent();
            String image_path= intent.getStringExtra("KEY");
            Uri fileUri = Uri.parse(image_path);
            imageView.setImageURI(fileUri);
            ImageViewNone.setImageURI(fileUri);
        }
        drawable= (BitmapDrawable) imageView.getDrawable();
        bmp = drawable.getBitmap();

        setfilter();


        ImageViewNone.setOnClickListener(this);
        ImageViewOld.setOnClickListener(this);
        ImageViewGrey.setOnClickListener(this);
        ImageViewLomo.setOnClickListener(this);
        ImageViewHdr.setOnClickListener(this);
        ImageViewSketch.setOnClickListener(this);
        ImageViewLight.setOnClickListener(this);
        ImageViewBlock.setOnClickListener(this);
        ImageViewTv.setOnClickListener(this);
        ImageViewSoftglow.setOnClickListener(this);
        btnNext.setOnClickListener(this);



        }

    private void setfilter() {

        ImageViewGrey.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.GRAY));
        ImageViewOld.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.OLD));
        ImageViewLomo.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.LOMO));
        ImageViewHdr.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.HDR));
        ImageViewSketch.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.SKETCH));
        ImageViewLight.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.LIGHT));
        ImageViewBlock.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.BLOCK));
        ImageViewTv.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.TV));
        ImageViewSoftglow.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.SOFT_GLOW));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.none:
                imageView.setImageBitmap(bmp);
                break;
            case R.id.grey:
                imageView.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.GRAY));
                break;
            case R.id.old:
                imageView.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.OLD));
                break;
            case R.id.lomo:
                imageView.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.LOMO));
                break;
            case R.id.hdr:
                imageView.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.HDR));
                break;
            case R.id.sketch:
                imageView.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.SKETCH));
                break;
            case R.id.light:
                imageView.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.LIGHT));
                break;
            case R.id.block:
                imageView.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.BLOCK));
                break;
            case R.id.tv:
                imageView.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.TV));
                break;
            case R.id.softglow:
                imageView.setImageBitmap(ImageFilter.applyFilter(bmp, ImageFilter.Filter.SOFT_GLOW));
                break;
            case R.id.next:

                nextfunc();
                break;

        }
    }

    private void nextfunc() {

        drawable= (BitmapDrawable) imageView.getDrawable();
        bmp = drawable.getBitmap();

        Uri uri = makeUriForImage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(ApplyFiltersActivity.this, PostImageActivity.class);
        intent.putExtra("NEWIMAGE",uri.toString());
        startActivity(intent);
               
    }

    private Uri makeUriForImage() {

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images",MODE_PRIVATE);

        final int min = 0;
        final int max = 8000;
        final int random = new Random().nextInt((max - min) + 1) + min;

        // Create a file to save the image
        file = new File(file,  random+"");

        try{

            OutputStream stream = null;

            stream = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);

            stream.flush();

            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri

         Uri savedImageURI = Uri.fromFile(file);

        Log.d("makeUriForImage: ", savedImageURI.toString());
        return savedImageURI;
    }


}

