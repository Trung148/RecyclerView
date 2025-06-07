package com.example.recyclerview



import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CartManager {
    private val cart: MutableList<Product> = mutableListOf()

    fun getCart(): MutableList<Product> = cart

    fun addProduct(product: Product) {
        if (cart.none { it.id == product.id }) {
            cart.add(product)
        }
    }

    fun isCartEmpty(): Boolean = cart.isEmpty()

    fun clearCart() {
        cart.clear()
    }

    fun saveCart(context: Context) {
        val sharedPref = context.getSharedPreferences("cart_pref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val json = Gson().toJson(cart)
        editor.putString("cart_items", json)
        editor.apply()
    }

    fun loadCart(context: Context) {
        val sharedPref = context.getSharedPreferences("cart_pref", Context.MODE_PRIVATE)
        val json = sharedPref.getString("cart_items", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<Product>>() {}.type
            val savedCart: MutableList<Product> = Gson().fromJson(json, type)
            cart.clear()
            cart.addAll(savedCart)
        }
    }
}
