package com.example.newbudgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.newbudgetapp.databinding.ActivityAddTransactionBinding;

public class AddTransactionActivity extends AppCompatActivity {
    private ActivityAddTransactionBinding binding;
    private Transaction existingTransaction;
    private int position = -1;
    private ArrayAdapter<CharSequence> expenseCategoriesAdapter;
    private ArrayAdapter<CharSequence> incomeCategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupCategoryAdapters();
        setupRadioGroupListener();

        // Vérifier si nous sommes en mode édition
        existingTransaction = getIntent().getParcelableExtra("transaction");
        position = getIntent().getIntExtra("position", -1);

        if (existingTransaction != null) {
            // Mode édition - remplir les champs avec les données existantes
            binding.amountInput.setText(String.format("%.2f", existingTransaction.getAmount()));
            binding.descriptionInput.setText(existingTransaction.getDescription());
            boolean isIncome = existingTransaction.getType().equals("Income");
            binding.incomeRadio.setChecked(isIncome);
            binding.expenseRadio.setChecked(!isIncome);
            
            // Sélectionner la bonne catégorie
            ArrayAdapter<CharSequence> adapter = isIncome ? incomeCategoriesAdapter : expenseCategoriesAdapter;
            binding.categorySpinner.setAdapter(adapter);
            int categoryPosition = findCategoryPosition(adapter, existingTransaction.getCategory());
            if (categoryPosition >= 0) {
                binding.categorySpinner.setSelection(categoryPosition);
            }
            
            setTitle("Modifier la transaction");
        } else {
            setTitle("Nouvelle transaction");
            // Par défaut, on commence avec les catégories de dépenses
            binding.categorySpinner.setAdapter(expenseCategoriesAdapter);
        }

        binding.saveButton.setOnClickListener(v -> saveTransaction());
    }

    private void setupCategoryAdapters() {
        // Créer les adaptateurs pour les catégories
        expenseCategoriesAdapter = ArrayAdapter.createFromResource(this,
            R.array.expense_categories, android.R.layout.simple_spinner_item);
        expenseCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        incomeCategoriesAdapter = ArrayAdapter.createFromResource(this,
            R.array.income_categories, android.R.layout.simple_spinner_item);
        incomeCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setupRadioGroupListener() {
        binding.typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Changer les catégories en fonction du type sélectionné
            if (checkedId == R.id.incomeRadio) {
                binding.categorySpinner.setAdapter(incomeCategoriesAdapter);
            } else {
                binding.categorySpinner.setAdapter(expenseCategoriesAdapter);
            }
        });
    }

    private int findCategoryPosition(ArrayAdapter<CharSequence> adapter, String category) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(category)) {
                return i;
            }
        }
        return -1;
    }

    private void saveTransaction() {
        String amountStr = binding.amountInput.getText().toString();
        String description = binding.descriptionInput.getText().toString();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer une description", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format de montant invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount <= 0) {
            Toast.makeText(this, "Le montant doit être supérieur à 0", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = binding.incomeRadio.isChecked() ? "Income" : "Expense";
        String category = binding.categorySpinner.getSelectedItem().toString();

        Transaction transaction = new Transaction(type, amount, description);
        transaction.setCategory(category);
        
        if (existingTransaction != null) {
            // En mode édition, conserver l'ID et la date d'origine
            transaction.setId(existingTransaction.getId());
            transaction.setDate(existingTransaction.getDate());
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("transaction", transaction);
        if (position != -1) {
            resultIntent.putExtra("position", position);
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }
} 