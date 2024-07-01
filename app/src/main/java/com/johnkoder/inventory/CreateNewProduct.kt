package com.johnkoder.inventory

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.johnkoder.inventory.databinding.ActivityCreateNewProductBinding
import com.johnkoder.inventory.services.ProductService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CreateNewProduct : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewProductBinding
    val productService = ProductService();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.createProductButton.setOnClickListener {
            it.isEnabled = false

            val name = binding.name.text.toString()
            val price = binding.price.text.toString()

            if (name.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val newProduct = Product(name, price.toDouble())

            runBlocking {
                    launch {
                        try {
                            productService.addProduct(newProduct)
                            Toast.makeText(this@CreateNewProduct, "Product created!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@CreateNewProduct, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } catch (e: Exception) {
                            it.isEnabled = true
                            Toast.makeText(this@CreateNewProduct, "Failed to create product", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}