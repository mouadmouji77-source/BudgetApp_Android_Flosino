package com.example.newbudgetapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoryStatsAdapter extends RecyclerView.Adapter<CategoryStatsAdapter.ViewHolder> {
    private List<CategoryStats> categoryStats;

    public CategoryStatsAdapter(List<CategoryStats> categoryStats) {
        this.categoryStats = categoryStats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_category_stats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryStats stats = categoryStats.get(position);
        holder.categoryName.setText(stats.getCategory());
        holder.categoryAmount.setText(String.format("%.2f DH", stats.getAmount()));
        holder.categoryPercentage.setText(String.format("%.1f%%", stats.getPercentage()));
    }

    @Override
    public int getItemCount() {
        return categoryStats.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        TextView categoryAmount;
        TextView categoryPercentage;

        ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryAmount = itemView.findViewById(R.id.categoryAmount);
            categoryPercentage = itemView.findViewById(R.id.categoryPercentage);
        }
    }
} 