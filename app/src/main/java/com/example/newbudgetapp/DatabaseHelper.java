package com.example.newbudgetapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "flosino.db";
    private static final int DATABASE_VERSION = 2;

    // Table Transactions
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CATEGORY = "category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " INTEGER,"
                + COLUMN_CATEGORY + " TEXT DEFAULT 'Autre'"
                + ")";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Ajouter la colonne category pour la version 2
            db.execSQL("ALTER TABLE " + TABLE_TRANSACTIONS + 
                      " ADD COLUMN " + COLUMN_CATEGORY + " TEXT DEFAULT 'Autre'");
        }
    }

    // Ajouter une transaction
    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_TYPE, transaction.getType());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_DESCRIPTION, transaction.getDescription());
        values.put(COLUMN_DATE, transaction.getDate().getTime());
        values.put(COLUMN_CATEGORY, transaction.getCategory());

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return id;
    }

    // Récupérer toutes les transactions
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                    cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                );
                transaction.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                transaction.setDate(new java.util.Date(cursor.getLong(cursor.getColumnIndex(COLUMN_DATE))));
                transaction.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactions;
    }

    // Supprimer une transaction
    public void deleteTransaction(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Mettre à jour une transaction
    public int updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TYPE, transaction.getType());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        values.put(COLUMN_DESCRIPTION, transaction.getDescription());
        values.put(COLUMN_DATE, transaction.getDate().getTime());
        values.put(COLUMN_CATEGORY, transaction.getCategory());

        int rowsAffected = db.update(TABLE_TRANSACTIONS, values, 
            COLUMN_ID + " = ?", new String[]{String.valueOf(transaction.getId())});
        db.close();
        return rowsAffected;
    }
} 