package com.example.newbudgetapp;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newbudgetapp.databinding.ActivityStatisticsBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {
    private ActivityStatisticsBinding binding;
    private List<Transaction> transactions;
    private Map<String, Double> categoryTotals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        transactions = getIntent().getParcelableArrayListExtra("transactions");
        if (transactions != null) {
            calculateStatistics();
            setupPieChart();
            setupCategoryList();
        }
    }

    private void calculateStatistics() {
        double totalIncome = 0;
        double totalExpense = 0;
        categoryTotals = new HashMap<>();

        for (Transaction transaction : transactions) {
            double amount = transaction.getAmount();
            if (transaction.getType().equals("Income")) {
                totalIncome += amount;
            } else {
                totalExpense += amount;
                // Ajouter au total de la catégorie
                String category = transaction.getCategory();
                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
            }
        }

        double balance = totalIncome - totalExpense;

        // Mettre à jour les TextViews
        binding.totalIncomeText.setText(String.format("%.2f DH", totalIncome));
        binding.totalExpenseText.setText(String.format("%.2f DH", totalExpense));
        binding.balanceText.setText(String.format("%.2f DH", balance));
        binding.balanceText.setTextColor(balance >= 0 ? getColor(R.color.income_green) : getColor(R.color.expense_red));
    }

    private void setupPieChart() {
        PieChart pieChart = binding.pieChart;
        List<PieEntry> entries = new ArrayList<>();
        
        // Ajouter les entrées pour chaque catégorie
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Catégories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setCenterText("Dépenses par\nCatégorie");
        pieChart.setCenterTextSize(16f);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void setupCategoryList() {
        RecyclerView categoryRecyclerView = binding.categoryRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        List<CategoryStats> categoryStatsList = new ArrayList<>();
        double totalExpenses = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            double percentage = (entry.getValue() / totalExpenses) * 100;
            categoryStatsList.add(new CategoryStats(
                entry.getKey(),
                entry.getValue(),
                percentage
            ));
        }
        
        CategoryStatsAdapter adapter = new CategoryStatsAdapter(categoryStatsList);
        categoryRecyclerView.setAdapter(adapter);
    }
} 