package com.example.elysia.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elysia.R;
import com.example.elysia.entity.Achievement;
import com.example.elysia.entity.Resource;

import java.io.File;
import java.util.List;

public class ResourseAdapter extends RecyclerView.Adapter<ResourseAdapter.ViewHolder> {
    private List<Resource> resources;
    private final ResourseAdapter.OnResourseClickListener onClickListener;

    public ResourseAdapter(List<Resource> resources, OnResourseClickListener onClickListener) {
        this.resources = resources;
        this.onClickListener = onClickListener;
    }


    public interface OnResourseClickListener {
        void openFile(Resource resource);

        void deleteFile(Resource resource);
    }

    @NonNull
    @Override
    public ResourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);
        return new ResourseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourseAdapter.ViewHolder holder, int position) {
        Resource resource = resources.get(position);
        String path = resource.getPathResource();
        Uri uri = Uri.parse(resource.getPathResource());
        holder.textViewName.setText(uri.getPath().toString().substring(uri.getPath().toString().lastIndexOf('/') + 1));
        if (check(".doc", path) || check(".doc", path)) {
            holder.imageViewFile.setImageResource(R.drawable.type_doc);
        } else if (check(".txt", path)) {
            holder.imageViewFile.setImageResource(R.drawable.type_txt);
        } else if (check(".xlsx", path)) {
            holder.imageViewFile.setImageResource(R.drawable.type_xlsx);
        } else if (check(".jpg", path) || check(".png", path)) {
            holder.imageViewFile.setImageResource(R.drawable.img);
        }
        holder.deleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.deleteFile(resource);
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.openFile(resource);
            }
        });
    }

    public boolean check(String type, String string) {
        return string.contains(type);

    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        ImageView deleteFile;
        CardView cardView;
        ImageView imageViewFile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            deleteFile = itemView.findViewById(R.id.deleteFile);
            cardView = itemView.findViewById(R.id.cardView);
            imageViewFile = itemView.findViewById(R.id.imageViewFile);
        }
    }
}
