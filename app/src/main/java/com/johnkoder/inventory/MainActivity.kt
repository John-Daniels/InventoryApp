package com.johnkoder.inventory

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.johnkoder.inventory.databinding.ActivityMainBinding
import com.johnkoder.inventory.services.ProductService
import com.johnkoder.inventory.ui.ProductAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    var productService = ProductService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if(currentUser == null) {
            goToLogin()
        }

        val fabAddProduct = findViewById<FloatingActionButton>(R.id.addProductBtn)
        fabAddProduct.setOnClickListener {
            val intent = Intent(this, CreateNewProduct::class.java)
            startActivity(intent)
        }


        val logoutBtn = findViewById<FloatingActionButton>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            auth.signOut()
            goToLogin()
        }

        loadProducts()

    }

    fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java);
        startActivity(intent);
        finish();
    }


    fun loadProducts() {
        lateinit var products: List<Product>

        runBlocking {
            launch {
//                products = listOf(
//                    Product("ps", 400.0)
//                )
                products = productService.getAllProducts().toList()
                Log.w("products",products.toString())
            }
        }

        updateState(products)

        productService.listenToProductChanges { updatedProducts ->
            // Update your UI with the new list of products
            // ... (e.g., update a RecyclerView adapter)
            updateState(updatedProducts)
        }

    }

    fun updateState(products: List<Product>) {
        val productRecyclerView = findViewById<RecyclerView>(R.id.productRecyclerView)
        productRecyclerView.layoutManager = LinearLayoutManager(this)
        productRecyclerView.adapter = ProductAdapter(products)
    }
}




