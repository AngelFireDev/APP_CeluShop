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

class ConfirmacionCompraActivity : AppCompatActivity() {

    private lateinit var btnPay: Button
    private lateinit var btnPay_2: Button
    private lateinit var btnChangeDelivery: Button
    private lateinit var btnCancel: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacion_compra)

        btnPay = findViewById(R.id.btn_pay)
        btnPay_2 = findViewById(R.id.btn_pay2)
        btnChangeDelivery = findViewById(R.id.btn_chance_delivery)
        btnCancel = findViewById(R.id.btn_cancelar)

        btnPay.setOnClickListener{
            //(funcion) para que vaya a PSE
        }

        btnPay_2.setOnClickListener{
           //(funcion) para que vaya a PSE
        }

        btnChangeDelivery.setOnClickListener{
            val intent = Intent(this, EdicionDireccionActivity::class.java)
            startActivity(intent)
        }

        btnCancel.setOnClickListener{
            finish()
        }
    }
}