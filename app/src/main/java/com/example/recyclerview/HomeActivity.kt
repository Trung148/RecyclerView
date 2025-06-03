package com.example.recyclerview

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
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
        val imgMenu = findViewById<ImageView>(R.id.imgMenu)
        imgMenu.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)

            // Force icons to show in the popup menu
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_profile -> {
                        Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_settings -> {
                        Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_logout -> {
                        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
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
            Product(1, "pharmacy", android.R.drawable.ic_menu_help, "Health", "123-456-7890"),
            Product(2, "registry", android.R.drawable.ic_menu_edit, "Services", "987-654-3210"),
            Product(3, "cartwheel", android.R.drawable.ic_menu_rotate, "Shopping", "555-123-4567"),
            Product(4, "clothing", android.R.drawable.ic_menu_gallery, "Fashion", "123-456-7890"),
            Product(5, "shoes", android.R.drawable.ic_menu_directions, "Fashion", "987-654-3210"),
            Product(6, "accessories", android.R.drawable.ic_menu_preferences, "Fashion", "555-123-4567"),
            Product(7, "baby", android.R.drawable.ic_menu_info_details, "Kids", "123-456-7890"),
            Product(8, "home", android.R.drawable.ic_menu_myplaces, "Home", "987-654-3210"),
            Product(9, "patio & garden", android.R.drawable.ic_menu_compass, "Outdoor", "555-123-4567"),
        )

        productAdapter = ProductAdapter(
            products,
            onItemClick = { product ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("PRODUCT_ID", product.id)
                    putExtra("PRODUCT_NAME", product.name)
                    putExtra("PRODUCT_CATEGORY", product.category)
                    putExtra("PRODUCT_ICON", product.iconResId)
                    putExtra("PRODUCT_PHONE", product.distributorPhone)
                }
                startActivity(intent)
            },
            onCallClick = { phone ->
                // Optionally handle call directly from the list, or leave empty
            }
        )

        recyclerView.adapter = productAdapter
    }
}