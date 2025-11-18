package com.app_celushop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app_celushop.activities.MainActivity
import com.app_celushop.R

class CarritoActivity : AppCompatActivity() {

    private lateinit var btnContinuePay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        btnContinuePay = findViewById(R.id.btnContinue)

        btnContinuePay.setOnClickListener {
            val intent = Intent(this, ConfirmacionCompraActivity::class.java)
            startActivity(intent)
        }
    }
}