
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

public class VideosAdapter extends BaseAdapter {

    public VideosAdapter(Context context, ArrayList<String> path, int imgViewWidth) {
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
        view = View.inflate(parent.getContext(), R.layout.grid_videos_items, null);
        ImageView imgVidGridPic = view.findViewById(R.id.imgVidGridPic);
        ImageView imgVidPlayIcon = view.findViewById(R.id.imgVidPlayIcon);
        ImageView imgVidCheckIcon = view.findViewById(R.id.imgVidCheckIcon);

        imgVidGridPic.getLayoutParams().height = imgViewWidth;
        imgVidGridPic.getLayoutParams().width = imgViewWidth;
        imgVidGridPic.requestLayout();

        imgVidPlayIcon.getLayoutParams().height = imgViewWidth/3;
        imgVidPlayIcon.getLayoutParams().width = imgViewWidth/3;
        imgVidPlayIcon.requestLayout();

        imgVidCheckIcon.getLayoutParams().height = imgViewWidth/5;
        imgVidCheckIcon.getLayoutParams().width = imgViewWidth/5;
        imgVidCheckIcon.requestLayout();

        Glide.with(context).load(path.get(position)).centerCrop().into(imgVidGridPic);
        Glide.with(context).load(R.drawable.ic_baseline_unchecked_24).centerCrop().into(imgVidCheckIcon);

        if(MainActivity.isSelected()){
            imgVidCheckIcon.setVisibility(View.VISIBLE);
        }

        imgVidGridPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isSelected()){
                    if (MainActivity.deletePath.contains(path.get(position))) {
                        Glide.with(context).load(R.drawable.ic_baseline_unchecked_24).centerCrop().into(imgVidCheckIcon);
                        MainActivity.deletePath.remove(path.get(position));

                    }else{
                        Glide.with(context).load(R.drawable.ic_baseline_check_24).centerCrop().into(imgVidCheckIcon);
                        MainActivity.deletePath.add(path.get(position));
                    }
                }else {
//                    Intent intent = new Intent(context, ShowSinglePicture.class);
//                    intent.putExtra("picPath",path.get(position) );
//                    context.startActivity(intent);
                }

            }
        });

        return view;
    }
}
