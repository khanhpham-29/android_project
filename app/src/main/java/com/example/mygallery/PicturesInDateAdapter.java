package com.example.mygallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//using RecyclerView
public class PicturesInDateAdapter extends RecyclerView.Adapter<PicturesInDateAdapter.ViewHolder> {
    Cursor PicturesPath;
    Context context;
    int ScreenWidth;
    ArrayList<ArrayList<String>> PictureInADate;
    int numCol;

    public PicturesInDateAdapter(Cursor picturesPath, Context context,  int ScreenWidth) {
        this.PicturesPath = picturesPath;
        this.context = context;
        this.ScreenWidth = ScreenWidth;
        numCol = 3;
        PictureInADate = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < 12; i++) {
            PictureInADate.add(new ArrayList<String>());
        }
        while (this.PicturesPath.moveToNext()) {
            String path = PicturesPath.getString(0);
            File file = new File(path);
            Date lastModDate = new Date(file.lastModified());
            int month = lastModDate.getMonth();
            PictureInADate.get(month).add(path);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = View.inflate(parent.getContext(), R.layout.lv_photos_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (PictureInADate.get(position).size() == 0) {
            holder.itemView.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
        } else {
            //find month name
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, position);
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            String month_name = month_date.format(cal.getTime());

            //Set
            holder.txtDate.setText(month_name);
            int numItems = PictureInADate.get(position).size();
            int imgViewWidth = ScreenWidth/numCol;
            int gridViewHeight = (imgViewWidth)*(numItems/numCol);
            if(numItems % numCol != 0)
            {
                gridViewHeight += imgViewWidth;
            }
            holder.txtSetHeight.setHeight(gridViewHeight);
            holder.txtNumItems.setText(numItems + " items");
            holder.GridPhotoItems.setNumColumns(numCol);
            holder.GridPhotoItems.setMinimumHeight(gridViewHeight);


            PicturesAdapter pa = new PicturesAdapter(context, PictureInADate.get(position), ScreenWidth/numCol);
            holder.GridPhotoItems.setAdapter(pa);
            pa.notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return PictureInADate.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        GridView GridPhotoItems;
        TextView txtDate;
        TextView txtNumItems;
        TextView txtSetHeight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            GridPhotoItems = itemView.findViewById(R.id.GridPhotoItems);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtNumItems = itemView.findViewById(R.id.txtNumItems);
            txtSetHeight = itemView.findViewById(R.id.txtSetHeight);
        }
    }
}
