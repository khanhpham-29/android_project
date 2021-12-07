package com.example.mygallery;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PicturesAdapter extends BaseAdapter {

    public PicturesAdapter(Context context, ArrayList<String> path, int imgViewWidth) {
        this.context = context;
        this.path = path;
        this.imgViewWidth = imgViewWidth;
    }

    Context context;
    int imgViewWidth;
    ArrayList<String> path;

    @Override
    public int getCount() {
        return path.size();
    }

    @Override
    public Object getItem(int position) {
        return path.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        view = View.inflate(parent.getContext(), R.layout.grid_photos_items, null);
        ImageView imgGridPic = view.findViewById(R.id.imgGridPic);
        imgGridPic.getLayoutParams().height = imgViewWidth;
        imgGridPic.getLayoutParams().width = imgViewWidth;
        imgGridPic.requestLayout();
        Glide.with(context).load(path.get(position)).centerCrop().into(imgGridPic);
        return view;
    }
}
