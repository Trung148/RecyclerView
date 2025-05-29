package com.example.recyclerview

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var productIcon: ImageView
    private lateinit var productName: TextView
    private lateinit var productCategory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()
        displayProductDetails()
    }

    private fun initViews() {
        productIcon = findViewById(R.id.iv_detail_icon)
        productName = findViewById(R.id.tv_detail_name)
        productCategory = findViewById(R.id.tv_detail_category)
    }

    private fun displayProductDetails() {
        val productId = intent.getIntExtra("PRODUCT_ID", 0)
        val productNameStr = intent.getStringExtra("PRODUCT_NAME") ?: ""
        val productCategoryStr = intent.getStringExtra("PRODUCT_CATEGORY") ?: ""
        val productIconRes = intent.getIntExtra("PRODUCT_ICON", 0)

        productName.text = productNameStr
        productCategory.text = "Category: $productCategoryStr"

        if (productIconRes != 0) {
            productIcon.setImageResource(productIconRes)
        }

        // Set up toolbar with back button
        supportActionBar?.apply {
            title = productNameStr
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
