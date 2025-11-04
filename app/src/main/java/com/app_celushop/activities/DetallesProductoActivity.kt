package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.activities.MainActivity
import com.app_celushop.R

class DetallesProductoActivity : AppCompatActivity() {
    private lateinit var btn_carrito: Button
    private lateinit var btn_comprar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_producto)

        btn_carrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
        }

        btn_comprar.setOnClickListener {
            val intent = Intent(this, ConfirmacionCompraActivity::class.java)
            startActivity(intent)
        }
    }
}