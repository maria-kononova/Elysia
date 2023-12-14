package com.example.elysia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elysia.R;
import com.example.elysia.entity.Event;
import com.example.elysia.entity.Task;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    private List<Event> eventList;
    private final OnEventClickListener onClickListener;

    public EventAdapter(List<Event> eventList, OnEventClickListener onEventClickListener) {
        this.eventList = eventList;
        this.onClickListener = onEventClickListener;
    }
    public interface OnEventClickListener{
        void deleteEvent(Event event, int position);
        void changeNotification(Event event);

        void calendarUpdate(Event event);
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.name.setText(event.getContentEvent());
        holder.dateTimeTextView.setText(event.getDate() + " " + event.getTime());
        if (event.getNotification()) {
            holder.notificationImageView.setImageResource(R.drawable.ic_notification_on);
        }
        else {
            holder.notificationImageView.setImageResource(R.drawable.ic_notification_off);
        }
        //!!!!!!!!!
        holder.calendarUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.calendarUpdate(event);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.deleteEvent(eventList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        holder.notificationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.changeNotification(eventList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView delete;
        ImageView notificationImageView;
        TextView dateTimeTextView;
        ImageView calendarUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.event_title);
            delete = itemView.findViewById(R.id.deleteEvent);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            notificationImageView = itemView.findViewById(R.id.notification);
            calendarUpdate = itemView.findViewById(R.id.calendarButton);
        }
    }
}
