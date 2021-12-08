package com.example.mygallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioBtnPhotos, radioBtnFolder, radioBtnVideos;
    RecyclerView LVPhotos;
    RecyclerView GVFolders;
    RecyclerView LVVideos;
    PicturesInDateAdapter picturesInDateAdapter;
    PicturesInFolderAdapter picturesInFolderAdapter;
    VideosInDateAdapter videosInDateAdapter;
    static boolean isSelectedLayout;
    static ArrayList<String> deletePath;
    int colorSelected;
    int colorNormal;
    RadioButton previousBtn;
    View previousView;
    Cursor photosPath;
    Cursor videosPath;
    Menu main_menu;

    static boolean  isSelected(){
        return isSelectedLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>My Gallery </font>"));

        bindWidget();

        //init
        deletePath = new ArrayList<>();
        colorSelected = ContextCompat.getColor(this, R.color.blue_selected);
        colorNormal = ContextCompat.getColor(this, R.color.grey);

        //Set which Scroll view and button is showing first
        radioBtnPhotos.setChecked(true);
        previousBtn = radioBtnPhotos;
        setBtnIconColor(radioBtnPhotos, colorSelected);
        previousView = LVPhotos;

        while (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, PackageManager.PERMISSION_GRANTED);
        }
        loadMediaToView();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        main_menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, main_menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuSetting)
        {
        }
        else if(item.getItemId() == R.id.menuSelect)
        {
            isSelectedLayout = true;
            main_menu.clear();
            getMenuInflater().inflate(R.menu.delete_menu, main_menu);
            radioGroup.setVisibility(View.INVISIBLE);
            loadMediaToView();
        }
        else if (item.getItemId() == R.id.menuDelete)
        {
            isSelectedLayout = false;
            main_menu.clear();
            getMenuInflater().inflate(R.menu.main_menu, main_menu);
            radioGroup.setVisibility(View.VISIBLE);
            deleteMedia();
            loadMediaToView();
            deletePath.clear();
        }
        else if(item.getItemId() == R.id.menuCancel)
        {
            isSelectedLayout = false;
            main_menu.clear();
            getMenuInflater().inflate(R.menu.main_menu, main_menu);
            radioGroup.setVisibility(View.VISIBLE);
            //picturesInDateAdapter.notifyDataSetChanged();
            deletePath.clear();
        }
        return true;
    }

    public void deleteMedia() {
        for (int i = 0; i < deletePath.size(); i++) {
            String path = deletePath.get(i);
            File file = new File(path);
            file.delete();

            final Uri uri = MediaStore.Files.getContentUri("external");
            getContentResolver().delete(uri,
                    MediaStore.Files.FileColumns.DATA + "=?", new String[]{path});
        }
    }

    private void bindWidget(){
        //connect xml with java obj
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioBtnPhotos = (RadioButton) findViewById(R.id.radioBtnPhotos);
        radioBtnFolder = (RadioButton) findViewById(R.id.radioBtnFolder);
        radioBtnVideos = (RadioButton) findViewById(R.id.radioBtnVideos);
        LVPhotos = (RecyclerView) findViewById(R.id.LVPhotos);
        GVFolders = (RecyclerView) findViewById(R.id.GVFolders);
        LVVideos = (RecyclerView) findViewById(R.id.LVVideos);
    }

    private void loadMediaToView(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        //get Devide size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        //Load pictures to LVPhotos
        photosPath = getAllShownPhotosPath();
        picturesInDateAdapter = new PicturesInDateAdapter(photosPath, this, width);
        LVPhotos.setAdapter(picturesInDateAdapter);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LVPhotos.setLayoutManager(gridLayoutManager);

        //Load pictures to GVFolders
        picturesInFolderAdapter = new PicturesInFolderAdapter(photosPath, this, width);
        GVFolders.setAdapter(picturesInFolderAdapter);
        StaggeredGridLayoutManager gridLayoutManager2 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        GVFolders.setLayoutManager(gridLayoutManager2);

        //Load videos to LVVideos
        videosPath =  getAllShownVideosPath();
        videosInDateAdapter = new VideosInDateAdapter(videosPath, this, width);
        LVVideos.setAdapter(videosInDateAdapter);
        StaggeredGridLayoutManager gridLayoutManager1 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LVVideos.setLayoutManager(gridLayoutManager1);
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