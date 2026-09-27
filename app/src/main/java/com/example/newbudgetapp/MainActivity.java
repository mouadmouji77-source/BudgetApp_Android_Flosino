package com.example.newbudgetapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.newbudgetapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TransactionAdapter.OnTransactionClickListener {
    private ActivityMainBinding binding;
    private TransactionAdapter adapter;
    private List<Transaction> transactions;
    private DatabaseHelper dbHelper;
    private static final int ADD_TRANSACTION_REQUEST = 1;
    private static final int EDIT_TRANSACTION_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        transactions = new ArrayList<>();
        setupRecyclerView();
        setupFAB();
        setupStatisticsButton();
        loadTransactions();
    }

    private void loadTransactions() {
        transactions.clear();
        transactions.addAll(dbHelper.getAllTransactions());
        adapter.notifyDataSetChanged();
        updateTotalAmount();
    }

    private void setupRecyclerView() {
        adapter = new TransactionAdapter(transactions, this);
        binding.transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionsRecyclerView.setAdapter(adapter);
    }

    private void setupFAB() {
        binding.addTransactionFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTransactionActivity.class);
            startActivityForResult(intent, ADD_TRANSACTION_REQUEST);
        });
    }

    private void setupStatisticsButton() {
        binding.statisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StatisticsActivity.class);
            intent.putParcelableArrayListExtra("transactions", new ArrayList<>(transactions));
            startActivity(intent);
        });
    }

    @Override
    public void onEditClick(Transaction transaction, int position) {
        Intent intent = new Intent(this, AddTransactionActivity.class);
        intent.putExtra("transaction", transaction);
        intent.putExtra("position", position);
        startActivityForResult(intent, EDIT_TRANSACTION_REQUEST);
    }

    @Override
    public void onDeleteClick(Transaction transaction, int position) {
        new AlertDialog.Builder(this)
            .setTitle("Supprimer la transaction")
            .setMessage("Êtes-vous sûr de vouloir supprimer cette transaction ?")
            .setPositiveButton("Supprimer", (dialog, which) -> {
                dbHelper.deleteTransaction(transaction.getId());
                transactions.remove(position);
                adapter.notifyItemRemoved(position);
                updateTotalAmount();
                showUndoSnackbar(transaction, position);
            })
            .setNegativeButton("Annuler", null)
            .show();
    }

    private void showUndoSnackbar(Transaction transaction, int position) {
        Snackbar.make(binding.getRoot(), "Transaction supprimée", Snackbar.LENGTH_LONG)
            .setAction("ANNULER", v -> {
                long id = dbHelper.addTransaction(transaction);
                transaction.setId(id);
                transactions.add(position, transaction);
                adapter.notifyItemInserted(position);
                updateTotalAmount();
            })
            .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Transaction transaction = data.getParcelableExtra("transaction");
            if (transaction != null) {
                if (requestCode == ADD_TRANSACTION_REQUEST) {
                    long id = dbHelper.addTransaction(transaction);
                    transaction.setId(id);
                    transactions.add(0, transaction);
                    adapter.notifyItemInserted(0);
                } else if (requestCode == EDIT_TRANSACTION_REQUEST) {
                    int position = data.getIntExtra("position", -1);
                    if (position != -1) {
                        dbHelper.updateTransaction(transaction);
                        transactions.set(position, transaction);
                        adapter.notifyItemChanged(position);
                    }
                }
                updateTotalAmount();
            }
        }
    }

    private void updateTotalAmount() {
        double total = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("Income")) {
                total += transaction.getAmount();
            } else {
                total -= transaction.getAmount();
            }
        }
        
        // Mettre à jour le texte et la couleur
        binding.totalBalanceTextView.setText(String.format("%.2f DH", total));
        
        // Changer la couleur en fonction du solde
        int colorResId = total >= 0 ? R.color.income_green : R.color.expense_red;
        binding.totalBalanceTextView.setTextColor(getColor(colorResId));
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
} 