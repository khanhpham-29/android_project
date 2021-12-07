package com.example.mygallery;

import android.content.Context;
import android.database.Cursor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class PicturesInFolderAdapter extends RecyclerView.Adapter<PicturesInFolderAdapter.ViewHolder> {
    Cursor PicturesPath;
    Context context;
    int ScreenWidth;
    ArrayList<ArrayList<String>> PictureInFolder;
    int numCol;
    ArrayList<String> sortedArrPath;


    public PicturesInFolderAdapter(Cursor picturesPath, Context context, int ScreenWidth) {
        this.PicturesPath = picturesPath;
        this.context = context;
        this.ScreenWidth = ScreenWidth;
        numCol = 3;

        PictureInFolder = new ArrayList<ArrayList<String>>();
        PictureInFolder.add(new ArrayList<String>());
        sortedArrPath = new ArrayList<>();

        if(this.PicturesPath.moveToFirst()){
            sortedArrPath.add(PicturesPath.getString(0));
            //Sort path
            while (this.PicturesPath.moveToNext()){
                sortedArrPath.add(PicturesPath.getString(0));
            }

            //Group picture by path
            String path = sortedArrPath.get(0);
            File file = new File(path);
            String previousDirectoryPath = file.getParent();
            int curFolder = 0;
            PictureInFolder.get(curFolder).add(path);

            for (int i = 1; i < sortedArrPath.size(); i++) {

                file = new File(sortedArrPath.get(i));
                if(file.getParent().compareTo(previousDirectoryPath) != 0 ){
                    previousDirectoryPath = file.getParent();
                    curFolder++;
                    PictureInFolder.add(new ArrayList<String>());
                }
                PictureInFolder.get(curFolder).add(sortedArrPath.get(i));
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = View.inflate(parent.getContext(), R.layout.lv_folder_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String path = PictureInFolder.get(position).get(0);

        File file = new File(path);
        path = file.getParent();
        file = new File(path);
        path = file.getName();

        if(path.compareTo("0")== 0){
            path = "SDcard";
        }

        //Set
        holder.txtPath.setText(path);
        int numItems = PictureInFolder.get(position).size();
        int imgViewWidth = ScreenWidth / numCol;
        int gridViewHeight = (imgViewWidth) * (numItems / numCol);
        if (numItems % numCol != 0) {
            gridViewHeight += imgViewWidth;
        }
        holder.txtFolderSetHeight.setHeight(gridViewHeight);
        holder.txtFolderNumItems.setText(numItems + " items");
        holder.GridFolderPhotoItems.setNumColumns(numCol);

        PicturesAdapter pa = new PicturesAdapter(context, PictureInFolder.get(position), ScreenWidth / numCol);
        holder.GridFolderPhotoItems.setAdapter(pa);
        pa.notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return PictureInFolder.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        GridView GridFolderPhotoItems;
        TextView txtPath;
        TextView txtFolderNumItems;
        TextView txtFolderSetHeight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            GridFolderPhotoItems = itemView.findViewById(R.id.GridFolderPhotoItems);
            txtPath = itemView.findViewById(R.id.txtPath);
            txtFolderNumItems = itemView.findViewById(R.id.txtFolderNumItems);
            txtFolderSetHeight = itemView.findViewById(R.id.txtFolderSetHeight);
        }
    }
}
