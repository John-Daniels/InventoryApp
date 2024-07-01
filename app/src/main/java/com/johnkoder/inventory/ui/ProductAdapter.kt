package com.johnkoder.inventory.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.johnkoder.inventory.Product
import com.johnkoder.inventory.R
import com.johnkoder.inventory.services.ProductService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val deleteButton: View = itemView.findViewById(R.id.deleteButton)
        // ... (Get reference to the delete button if needed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_card_item, parent, false)
        return ProductViewHolder(itemView)
    }
    var productService = ProductService()
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = products[position]
        // Set data to the views in the holder (e.g., holder.productNameTextView.text = currentProduct.name)
        holder.productNameTextView.text = currentProduct.name
        holder.productPriceTextView.text = "$" + String.format("%.2f", currentProduct.price) // F
        holder.deleteButton.setOnClickListener {
            it.isEnabled = false;

            runBlocking {
                launch {
                    productService.deleteProduct(currentProduct.id)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}