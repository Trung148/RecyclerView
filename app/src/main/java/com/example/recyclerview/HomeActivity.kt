package com.example.recyclerview

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupRecyclerView()
        setupProductList()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rv_products)

        // Custom GridLayoutManager để control chiều cao
        val gridLayoutManager = object : GridLayoutManager(this, 3) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    resources.displayMetrics.heightPixels / 3 // Mỗi hàng = 1/3 màn hình
                )
            }
        }

        recyclerView.layoutManager = gridLayoutManager
    }

    private fun setupProductList() {
        val products = listOf(
            Product(1, "pharmacy", android.R.drawable.ic_menu_help, "Health"),
            Product(2, "registry", android.R.drawable.ic_menu_edit, "Services"),
            Product(3, "cartwheel", android.R.drawable.ic_menu_rotate, "Shopping"),
            Product(4, "clothing", android.R.drawable.ic_menu_gallery, "Fashion"),
            Product(5, "shoes", android.R.drawable.ic_menu_directions, "Fashion"),
            Product(6, "accessories", android.R.drawable.ic_menu_preferences, "Fashion"),
            Product(7, "baby", android.R.drawable.ic_menu_info_details, "Kids"),
            Product(8, "home", android.R.drawable.ic_menu_myplaces, "Home"),
            Product(9, "patio & garden", android.R.drawable.ic_menu_compass, "Outdoor")
        )

        productAdapter = ProductAdapter(products) { product ->
            // Navigate to DetailActivity when item is clicked
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id)
                putExtra("PRODUCT_NAME", product.name)
                putExtra("PRODUCT_CATEGORY", product.category)
                putExtra("PRODUCT_ICON", product.iconResId)
            }
            startActivity(intent)
        }

        recyclerView.adapter = productAdapter
    }
}