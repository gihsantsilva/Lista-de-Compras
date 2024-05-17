package com.example.testegemini

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "shopping_list.db"
        private const val TABLE_NAME = "shopping_items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOME = "nome"
        private const val COLUMN_QUANTIDADE = "quantidade"
        private const val COLUMN_PRECO = "preco"
        private const val COLUMN_IS_PURCHASED = "is_purchased"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NOME + " TEXT,"
                + COLUMN_QUANTIDADE + " INTEGER,"
                + COLUMN_PRECO + " REAL,"
                + COLUMN_IS_PURCHASED + " INTEGER DEFAULT 0)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addItem(item: ShoppingItem): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOME, item.nome)
        values.put(COLUMN_QUANTIDADE, item.quantidade)
        values.put(COLUMN_PRECO, item.preco)
        values.put(COLUMN_IS_PURCHASED, if (item.isPurchased) 1 else 0)

        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

    fun updateItem(item: ShoppingItem): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOME, item.nome)
        values.put(COLUMN_QUANTIDADE, item.quantidade)
        values.put(COLUMN_PRECO, item.preco)
        values.put(COLUMN_IS_PURCHASED, if (item.isPurchased) 1 else 0)

        val success =
            db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(item.id.toString())).toLong()
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

    fun deleteItem(itemId: Int): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(itemId.toString())).toLong()
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

    fun getAllItems(): ArrayList<ShoppingItem> {
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        val items = ArrayList<ShoppingItem>()

        cursor.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex(COLUMN_ID)
                val nomeIndex = it.getColumnIndex(COLUMN_NOME)
                val quantidadeIndex = it.getColumnIndex(COLUMN_QUANTIDADE)
                val precoIndex = it.getColumnIndex(COLUMN_PRECO)
                val isPurchasedIndex = it.getColumnIndex(COLUMN_IS_PURCHASED)

                do {
                    val id = it.getInt(idIndex)
                    val name = it.getString(nomeIndex) ?: ""
                    val quantity = it.getInt(quantidadeIndex)
                    val price = it.getDouble(precoIndex)
                    val isPurchased = it.getInt(isPurchasedIndex) == 1

                    val item = ShoppingItem(id, name, quantity, price, isPurchased)
                    items.add(item)
                } while (it.moveToNext())
            }
            cursor.close()
            db?.close()
            return items
        }
    }
}