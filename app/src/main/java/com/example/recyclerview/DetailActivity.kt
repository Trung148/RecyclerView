package com.example.recyclerview

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class DetailActivity : AppCompatActivity() {

    private lateinit var productIcon: ImageView
    private lateinit var productName: TextView
    private lateinit var productCategory: TextView
    private var distributorPhone: String? = null
    private val REQUEST_CALL_PHONE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()
        displayProductDetails()
        distributorPhone = intent.getStringExtra("PRODUCT_PHONE")
        val callButton = findViewById<Button>(R.id.btn_call_distributor)
        callButton.setOnClickListener { makePhoneCall() }
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
    private fun makePhoneCall() {
        distributorPhone?.let { phone ->
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED
            ) {
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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PHONE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            makePhoneCall()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
