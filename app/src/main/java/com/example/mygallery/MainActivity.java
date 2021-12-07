package com.example.mygallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioBtnPhotos, radioBtnFolder, radioBtnVideos;
 //   ListView LVPhotos;
    RecyclerView LVPhotos;
    RecyclerView GVFolders;
    RecyclerView LVVideos;
    int colorSelected;
    int colorNormal;
    RadioButton previousBtn;
    View previousView;
    Cursor photosPath;
    Cursor videosPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 611);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 611);
        }

        //connect xml with java obj
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioBtnPhotos = (RadioButton) findViewById(R.id.radioBtnPhotos);
        radioBtnFolder = (RadioButton) findViewById(R.id.radioBtnFolder);
        radioBtnVideos = (RadioButton) findViewById(R.id.radioBtnVideos);
        LVPhotos = (RecyclerView) findViewById(R.id.LVPhotos);
        GVFolders = (RecyclerView) findViewById(R.id.GVFolders);
        LVVideos = (RecyclerView) findViewById(R.id.LVVideos);
        colorSelected = ContextCompat.getColor(this, R.color.blue_selected);
        colorNormal = ContextCompat.getColor(this, R.color.grey);

        //Set which Scroll view and button is showing first
        radioBtnPhotos.setChecked(true);
        previousBtn = radioBtnPhotos;
        setBtnIconColor(radioBtnPhotos, colorSelected);
        previousView = LVPhotos;
        photosPath = getAllShownPhotosPath();

        //get Devide size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        //Load pictures to LVPhotos
        PicturesInDateAdapter pa = new PicturesInDateAdapter(photosPath, this, width);
        LVPhotos.setAdapter(pa);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LVPhotos.setLayoutManager(gridLayoutManager);

        //Load pictures to GVFolders
        PicturesInFolderAdapter pFa = new PicturesInFolderAdapter(photosPath, this, width);
        GVFolders.setAdapter(pFa);
        StaggeredGridLayoutManager gridLayoutManager2 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        GVFolders.setLayoutManager(gridLayoutManager2);

        String a = new String();
        for (int i = 0; i <  pFa.sortedArrPath.size(); i++) {
            File file = new File( pFa.sortedArrPath.get(i));
            a = a+"\n" + file.getParent();

        }

        Toast.makeText(this, "" + a, Toast.LENGTH_LONG).show();

        //Load videos to LVVideos
        videosPath =  getAllShownVideosPath();
        VideosInDateAdapter va = new VideosInDateAdapter(videosPath, this, width);
        LVVideos.setAdapter(va);
        StaggeredGridLayoutManager gridLayoutManager1 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LVVideos.setLayoutManager(gridLayoutManager1);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Change color radioBtn - which is checked ;
                setBtnIconColor(previousBtn, colorNormal);
                previousBtn = (RadioButton) findViewById(checkedId);
                setBtnIconColor(previousBtn, colorSelected);
                //Change which scrollView is showing
                previousView.setVisibility(View.GONE);
                if (checkedId == radioBtnPhotos.getId()) {
                    LVPhotos.setVisibility(View.VISIBLE);
                    previousView = LVPhotos;
                }
                if (checkedId == radioBtnFolder.getId()) {
                    GVFolders.setVisibility(View.VISIBLE);
                    previousView = GVFolders;
                }
                if (checkedId == radioBtnVideos.getId()) {
                    LVVideos.setVisibility(View.VISIBLE);
                    previousView = LVVideos;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 611 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }

    private void setBtnIconColor(RadioButton radioButton, int color) {
        Drawable[] drawables = radioButton.getCompoundDrawables();
        drawables[1].setColorFilter(color, PorterDuff.Mode.MULTIPLY);//DrawableTop
    }

    private Cursor getAllShownPhotosPath() {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cs = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, null, null, null);
        return cs;
    }

    private Cursor getAllShownVideosPath() {
        String[] filePathColumn = {MediaStore.Video.Media.DATA};
        Cursor cs = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, filePathColumn, null, null, null);
        return cs;
    }




}