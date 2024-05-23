package com.example.testegemini

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class ShoppingItemAdapter(context: Context, items: List<ShoppingItem>)
    : ArrayAdapter<ShoppingItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        textView.text = "${item?.nome}: ${item?.quantidade} x ${item?.preco}"
        val color = if (item?.isPurchased == true) android.R.color.holo_green_dark else android.R.color.black
        textView.setTextColor(ContextCompat.getColor(context, color))

        return view
    }
}