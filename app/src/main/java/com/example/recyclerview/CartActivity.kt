package com.example.recyclerview

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val container = findViewById<LinearLayout>(R.id.container_cart_items)
        val emptyCartText = findViewById<TextView>(R.id.tv_empty_cart)

        val cart = CartManager.getCart()

        if (cart.isEmpty()) {
            emptyCartText.visibility = View.VISIBLE
        } else {
            emptyCartText.visibility = View.GONE

            for (product in cart) {
                val itemView = layoutInflater.inflate(R.layout.item_cart_product, container, false)

                val tvName = itemView.findViewById<TextView>(R.id.tv_product_name)
                val tvCategory = itemView.findViewById<TextView>(R.id.tv_product_category)
                val ivIcon = itemView.findViewById<ImageView>(R.id.iv_product_icon)

                tvName.text = product.name
                tvCategory.text = "Category: ${product.category}"
                ivIcon.setImageResource(product.iconResId)

                container.addView(itemView)
            }
        }
    }
}