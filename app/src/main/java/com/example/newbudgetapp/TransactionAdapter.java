package com.example.newbudgetapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;
    private OnTransactionClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OnTransactionClickListener {
        void onEditClick(Transaction transaction, int position);
        void onDeleteClick(Transaction transaction, int position);
    }

    public TransactionAdapter(List<Transaction> transactions, OnTransactionClickListener listener) {
        this.transactions = transactions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        
        holder.amountText.setText(String.format("%.2f DH", transaction.getAmount()));
        holder.descriptionText.setText(transaction.getDescription());
        holder.dateText.setText(dateFormat.format(transaction.getDate()));
        
        // Définir la couleur en fonction du type
        int color = transaction.getType().equals("Income") ? 
            holder.itemView.getContext().getColor(R.color.income_green) :
            holder.itemView.getContext().getColor(R.color.expense_red);
        holder.amountText.setTextColor(color);

        // Configuration des boutons
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(transaction, holder.getAdapterPosition());
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(transaction, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView amountText;
        TextView descriptionText;
        TextView dateText;
        ImageButton editButton;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountText = itemView.findViewById(R.id.amountText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            dateText = itemView.findViewById(R.id.dateText);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
} 