package com.example.mygallery;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ShowSinglePicture extends AppCompatActivity {

    ImageView imgLarge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_picture);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        imgLarge =(ImageView) findViewById(R.id.imgLarge);
        Intent intent = getIntent();
        String path = intent.getStringExtra("picPath");
        Glide.with(this).load(path).into(imgLarge);
    }
}