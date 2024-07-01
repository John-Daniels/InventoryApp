package com.johnkoder.inventory.services

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.johnkoder.inventory.Product
import kotlinx.coroutines.tasks.await

class ProductService {
    private var db: FirebaseFirestore = Firebase.firestore
    private val productsCollection = db.collection("products")

    constructor()
    constructor(db: FirebaseFirestore){
        this.db = db
    }

    // CREATE
    suspend fun addProduct(product: Product) {
        productsCollection.add(product).await()
    }

    // READ (Get all products)
    suspend fun getAllProducts(): List<Product> {
        val snapshot = productsCollection.get().await()
        return snapshot.documents.map { document ->
            Product(
                name = document.getString("name") ?: "",
                price = document.getDouble("price") ?: 0.0,
                quantity = document.getLong("quantity")?.toInt() ?: 0,
                id = document.id
            )
        }
    }

    // READ (Get a single product by ID)
    suspend fun getProductById(productId: String): Product? {
        val document = productsCollection.document(productId).get().await()
        return if (document.exists()) Product(
            name = document.getString("name") ?: "",
            price = document.getDouble("price") ?: 0.0,
            quantity = document.getLong("quantity")?.toInt() ?: 0,
            id = document.id
        ) else null
    }

    // UPDATE
    suspend fun updateProduct(productId: String, updatedProduct: Product) {
        productsCollection.document(productId).set(updatedProduct).await()
    }

    // DELETE
    suspend fun deleteProduct(productId: String) {
        productsCollection.document(productId).delete().await()
    }

    fun listenToProductChanges(onProductsUpdated: (List<Product>) -> Unit) {
        productsCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle the exception (e.g., log the error)
                println("Error listening to product changes: ${exception.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val updatedProducts = snapshot.documents.map { document ->
                    Product(
                        name = document.getString("name") ?: "",
                        price = document.getDouble("price") ?: 0.0,
                        quantity = document.getLong("quantity")?.toInt() ?: 0,
                        id = document.id
                    )
                }
                onProductsUpdated(updatedProducts)
            }
        }
    }
}