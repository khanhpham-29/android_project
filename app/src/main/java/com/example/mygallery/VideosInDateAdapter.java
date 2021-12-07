

package com.example.mygallery;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
public class VideosInDateAdapter extends RecyclerView.Adapter<VideosInDateAdapter.ViewHolder> {
    Cursor VideosPath;
    Context context;
    int ScreenWidth;
    ArrayList<ArrayList<String>> VideoInADate;
    int numCol;

    public VideosInDateAdapter(Cursor VideosPath, Context context, int ScreenWidth) {
        this.VideosPath = VideosPath;
        this.context = context;
        this.ScreenWidth = ScreenWidth;
        numCol = 3;
        VideoInADate = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < 12; i++) {
            VideoInADate.add(new ArrayList<String>());
        }
        while (this.VideosPath.moveToNext()) {
            String path = VideosPath.getString(0);
            File file = new File(path);
            Date lastModDate = new Date(file.lastModified());
            int month = lastModDate.getMonth();
            VideoInADate.get(month).add(path);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = View.inflate(parent.getContext(), R.layout.lv_videos_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (VideoInADate.get(position).size() == 0) {
            holder.itemView.setLayoutParams(new AbsListView.LayoutParams(-1, 1));
        } else {
            //find month name
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, position);
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            String month_name = month_date.format(cal.getTime());

            //Set
            holder.txtVidDate.setText(month_name);

            int numItems = VideoInADate.get(position).size();
            int imgViewWidth = ScreenWidth / numCol;
            int gridViewHeight = (imgViewWidth) * (numItems / numCol);
            if (numItems % numCol != 0) {
                gridViewHeight += imgViewWidth;
            }
            holder.txtVidSetHeight.setHeight(gridViewHeight);
            holder.txtVidNumItems.setText(numItems + " items");
            holder.GridVidItems.setNumColumns(numCol);

            VideosAdapter pa = new VideosAdapter(context, VideoInADate.get(position), ScreenWidth / numCol);
            holder.GridVidItems.setAdapter(pa);
            pa.notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return VideoInADate.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        GridView GridVidItems;
        TextView txtVidDate;
        TextView txtVidNumItems;
        TextView txtVidSetHeight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            GridVidItems = itemView.findViewById(R.id.GridVidItems);
            txtVidDate = itemView.findViewById(R.id.txtVidDate);
            txtVidNumItems = itemView.findViewById(R.id.txtVidNumItems);
            txtVidSetHeight = itemView.findViewById(R.id.txtVidSetHeight);
        }
    }
}

