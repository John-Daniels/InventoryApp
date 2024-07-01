package com.johnkoder.inventory

//data class Product(val name: String, val price: Double)

data class Product(
    var name: String = "", // Provide default values for all properties
    var price: Double = 0.0,
    var quantity: Int = 0,
    var id: String = "" // If you're using a database, you might need an ID
)