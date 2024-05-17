package com.example.testegemini

data class ShoppingItem(
    var id:Int = 0,
    var nome: String,
    var quantidade: Int,
    var preco: Double,
    var isPurchased: Boolean
)