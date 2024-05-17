package com.example.testegemini

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: ArrayAdapter<ShoppingItem>
    private lateinit var editTextNome: EditText
    private lateinit var editTextQuantidade: EditText
    private lateinit var editTextPreco: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        dbHelper.writableDatabase

        val items = dbHelper.getAllItems()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dbHelper.getAllItems())
        val listViewItems: ListView = findViewById(R.id.listViewItems)
        listViewItems.adapter = adapter

        editTextNome = findViewById(R.id.editTextNome)
        editTextQuantidade = findViewById(R.id.editTextQuantidade)
        editTextPreco = findViewById(R.id.editTextPreco)

        val buttonAdd: Button = findViewById(R.id.buttonAdd)

        buttonAdd.setOnClickListener {
            addItem()
        }

        listViewItems.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position)
            item?.let {
                item.isPurchased = !item.isPurchased
                dbHelper.updateItem(item)
                adapter.notifyDataSetChanged()
            }
        }

        listViewItems.setOnItemLongClickListener { _, _, position, _ ->
            val item = adapter.getItem(position)
            item?.let {
                if (dbHelper.deleteItem(item.id)) {
                    adapter.remove(item)
                    Toast.makeText(this, "Item deletado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Falha ao deletar item", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    private fun addItem() {
        val nome = editTextNome.text.toString()
        val quantidade = editTextQuantidade.text.toString().toIntOrNull() ?: 0
        val preco = editTextPreco.text.toString().toDoubleOrNull() ?: 0.0

        if (nome.isNotEmpty() && quantidade > 0 && preco > 0.0) {
            val item = ShoppingItem(nome = nome, quantidade = quantidade, preco = preco, isPurchased = false)
            if (dbHelper.addItem(item)) {
                adapter.add(item)
                clearFields()
                Toast.makeText(this, "Item adicionado com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Falha em adicionar Item", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Por favor entre com dado Válido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        editTextNome.text.clear()
        editTextQuantidade.text.clear()
        editTextPreco.text.clear()
    }
}