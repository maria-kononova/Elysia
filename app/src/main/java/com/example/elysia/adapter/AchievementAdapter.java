package com.example.elysia.adapter;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elysia.MainActivity;
import com.example.elysia.R;
import com.example.elysia.dao.AchievementDao;
import com.example.elysia.entity.Achievement;
import com.example.elysia.entity.Task;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder>{

    private List<Achievement> achievements;
    private final AchievementAdapter.OnAchievementClickListener onClickListener;
    private Context context;

    public AchievementAdapter(List<Achievement> achievements, OnAchievementClickListener onAchievementClickListener) {
        this.achievements = achievements;
        this.onClickListener = onAchievementClickListener;
    }
    public interface OnAchievementClickListener{
        void onAchievementClick(Achievement achievement);


        void onAchievementLongClick(Achievement achievement, int position);
    }

    @NonNull
    @Override
    public AchievementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Achievement achievement = achievements.get(position);
        if(position==0) holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        holder.title.setText(achievements.get(position).getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onAchievementClick(achievement);
                System.out.println("клик");
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickListener.onAchievementLongClick(achievement, position);
                System.out.println("долгий клик");
                return true;
            }
        });

}

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleAchievement);
            cardView = itemView.findViewById(R.id.cardViewAch);
        }
    }
}
