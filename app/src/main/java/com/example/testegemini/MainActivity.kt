package com.example.testegemini

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var editTextNome: EditText
    private lateinit var editTextQuantidade: EditText
    private lateinit var editTextPreco: EditText
    private lateinit var textViewSummary: TextView
    private lateinit var adapter: ShoppingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Lista de Compras"

        dbHelper = DBHelper(this)
        dbHelper.writableDatabase

        val items = dbHelper.getAllItems()
        val adapter = ShoppingItemAdapter(this, items)
        val listViewItems: ListView = findViewById(R.id.listViewItems)
        listViewItems.adapter = adapter

        editTextNome = findViewById(R.id.editTextNome)
        editTextQuantidade = findViewById(R.id.editTextQuantidade)
        editTextPreco = findViewById(R.id.editTextPreco)
        textViewSummary = findViewById(R.id.textViewSummary)

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
                updateSummary()
            }
        }

        listViewItems.setOnItemLongClickListener { _, _, position, _ ->
            val item = adapter.getItem(position)
            item?.let {
                if (dbHelper.deleteItem(item.id)) {
                    adapter.remove(item)
                    Toast.makeText(this, "Item deletado com sucesso!", Toast.LENGTH_SHORT).show()
                    updateSummary()
                } else {
                    Toast.makeText(this, "Falha ao deletar item", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        updateSummary()
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
                updateSummary()
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

    private fun updateSummary() {
        val items = dbHelper.getAllItems()
        val summary = items.joinToString("\n") { item ->
            val status = if (item.isPurchased) "(Comprado)" else "(Não Comprado)"
            "${item.nome}: ${item.quantidade} x ${item.preco} $status"
        }
        val boldText = "ITENS ADICIONADOS:\n"
        val fullText = boldText + summary
        val spannableString = SpannableString(fullText)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            boldText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textViewSummary.text = spannableString
    }
}
