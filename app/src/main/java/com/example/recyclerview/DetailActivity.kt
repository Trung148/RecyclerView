package com.example.recyclerview

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class DetailActivity : AppCompatActivity() {

    private lateinit var productIcon: ImageView
    private lateinit var productName: TextView
    private lateinit var productCategory: TextView
    private var distributorPhone: String? = null

    private val REQUEST_CALL_PHONE = 1
    private val REQUEST_NOTIFICATION_PERMISSION = 2

    private var pendingProductNameForNotification: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()
        displayProductDetails()
        distributorPhone = intent.getStringExtra("PRODUCT_PHONE")

        findViewById<Button>(R.id.btn_call_distributor).setOnClickListener {
            makePhoneCall()
        }

        findViewById<Button>(R.id.btn_add_to_cart).setOnClickListener {
            addToCart()
        }
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

        supportActionBar?.apply {
            title = productNameStr
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun makePhoneCall() {
        distributorPhone?.let { phone ->
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CALL_PHONE
                )
            } else {
                val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
                startActivity(callIntent)
            }
        }
    }

    private fun addToCart() {
        val product = Product(
            id = intent.getIntExtra("PRODUCT_ID", 0),
            name = intent.getStringExtra("PRODUCT_NAME") ?: "",
            category = intent.getStringExtra("PRODUCT_CATEGORY") ?: "",
            iconResId = intent.getIntExtra("PRODUCT_ICON", 0),
            distributorPhone = intent.getStringExtra("PRODUCT_PHONE") ?: ""
        )
        CartManager.addProduct(product)
        CartManager.saveCart(this)
        Toast.makeText(this, "Added to cart: ${product.name}", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                pendingProductNameForNotification = product.name
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            } else {
                showAddToCartNotification(product.name)
            }
        } else {
            showAddToCartNotification(product.name)
        }
    }

    private fun showAddToCartNotification(productName: String) {
        val intent = Intent(this, CartActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val builder = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Product added to cart")
            .setContentText("$productName has been added to your cart.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(this).notify(1001, builder.build())
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
                Toast.makeText(this, "Notification permission not granted!", Toast.LENGTH_SHORT).show()
            }
        } else {
            NotificationManagerCompat.from(this).notify(1001, builder.build())
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Gửi lại thông báo nếu được cấp quyền
            pendingProductNameForNotification?.let {
                showAddToCartNotification(it)
                pendingProductNameForNotification = null
            }
            Toast.makeText(this, "Notification permission granted!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
