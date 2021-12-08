package com.example.mygallery;

import android.content.Context;
import android.content.Intent;
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
    static final int _CHECKER = 1;
    static final int _UNCHECKER = 0;


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
        ImageView imgCheckIcon = view.findViewById(R.id.imgCheckIcon);

        imgGridPic.getLayoutParams().height = imgViewWidth;
        imgGridPic.getLayoutParams().width = imgViewWidth;

        imgCheckIcon.getLayoutParams().height = imgViewWidth/5;
        imgCheckIcon.getLayoutParams().width = imgViewWidth/5;

        imgGridPic.requestLayout();
        imgCheckIcon.requestLayout();
        Glide.with(context).load(path.get(position)).centerCrop().into(imgGridPic);

        Glide.with(context).load(R.drawable.ic_baseline_unchecked_24).centerCrop().into(imgCheckIcon);


        if(MainActivity.isSelected()){
            imgCheckIcon.setVisibility(View.VISIBLE);
        }

        imgGridPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isSelected()){
                    if (MainActivity.deletePath.contains(path.get(position))) {
                        Glide.with(context).load(R.drawable.ic_baseline_unchecked_24).centerCrop().into(imgCheckIcon);
                        MainActivity.deletePath.remove(path.get(position));

                    }else{
                        Glide.with(context).load(R.drawable.ic_baseline_check_24).centerCrop().into(imgCheckIcon);
                        MainActivity.deletePath.add(path.get(position));
                    }
                }else {
                    Intent intent = new Intent(context, ShowSinglePicture.class);
                    intent.putExtra("picPath",path.get(position) );
                    context.startActivity(intent);
                }

            }
        });
        return view;
    }
}
