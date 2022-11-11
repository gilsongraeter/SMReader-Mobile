package com.starmeasure.absoluto.filemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starmeasure.absoluto.R;

import java.io.File;
import java.util.ArrayList;

public class FileManagerNICAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<File> files;
    private static FileManagerNICAdapter.ClickListener clickListener;

    public FileManagerNICAdapter(@NonNull Context context, @NonNull ArrayList<File> files) {
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileManagerViewHolder(LayoutInflater.from(context).inflate(R.layout.file_manager_nic_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FileManagerViewHolder hold = (FileManagerViewHolder) holder;
        hold.tvName.setText(files.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    private class FileManagerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tvName;

        FileManagerViewHolder(@NonNull View view) {
            super(view);
            tvName = view.findViewById(R.id.fmci_tv_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void deleteFile(File file, int position) {
        boolean result = file.delete();
        if (result) {
            files.remove(file);
            notifyItemRemoved(position);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        FileManagerNICAdapter.clickListener = clickListener;
    }

    public interface ClickListener{
        void onItemClick(int position, View v);
    }

    public File getFile(int position) {
        return files.get(position);
    }
}