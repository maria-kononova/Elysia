package com.example.elysia.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elysia.R;
import com.example.elysia.entity.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> taskList;
    private List<Task> taskListComplete;
    int index;
    private final OnTaskClickListener onClickListener;

    public TaskAdapter(List<Task> taskList,  OnTaskClickListener onTaskClickListener) {
            this.taskList = taskList;
            this.onClickListener = onTaskClickListener;
    }
    public interface OnTaskClickListener{
        void onTaskClick(Task task, int position);
        void setComplete(Task task, int position, Boolean isChecked);
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
        void deleteTask(Task task, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Task task = taskList.get(position);
        holder.titleTextView.setText(taskList.get(position).getTitle());
        holder.descriptionTextView.setText(taskList.get(position).getDescription());
        if (taskList.get(position).isDone()) {
            holder.checkBox.setChecked(true);
            holder.titleTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.checkBox.setChecked(false);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.deleteTask(taskList.get(holder.getAdapterPosition()), index);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onClickListener.onTaskClick(taskList.get(holder.getAdapterPosition()), index);
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onClickListener.setComplete(taskList.get(holder.getAdapterPosition()), index,  holder.checkBox.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;
        CheckBox checkBox;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            titleTextView = itemView.findViewById(R.id.task_title);
            descriptionTextView = itemView.findViewById(R.id.task_description);
            delete = itemView.findViewById(R.id.deleteTask);
        }
    }
}

