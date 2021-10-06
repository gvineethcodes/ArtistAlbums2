package com.example.artistalbums2;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChildRecyclerViewAdapter extends RecyclerView.Adapter<ChildRecyclerViewAdapter.MyViewHolder> {
    ArrayList<ChildModel> childModelArrayList;
    Context context;
    public ChildRecyclerViewAdapter(ArrayList<ChildModel> childModelArrayList, Context context) {
        this.childModelArrayList = childModelArrayList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView albumImage;
        public TextView albumName;

        public MyViewHolder(View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.album_image);
            albumName = itemView.findViewById(R.id.album_name);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_recyclerview_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder  holder, int position) {
        ChildModel albumDetails = childModelArrayList.get(position);

        Picasso.get()
                .load(albumDetails.getAlbumImage())
                .into(holder.albumImage);
        holder.albumName.setText(albumDetails.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return childModelArrayList.size();
    }
}
