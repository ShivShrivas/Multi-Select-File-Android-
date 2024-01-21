package com.app.multipartjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder> {
    Context context;
    ArrayList<File> selectedFilesList;
    public ImageViewAdapter(Context context, ArrayList<File> selectedFilesList) {
    this.context=context;
     this.selectedFilesList=selectedFilesList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.imagecard,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Uri uri = Uri.fromFile(selectedFilesList.get(position));
        holder.imageview.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return selectedFilesList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.imageview);
        }
    }
}
